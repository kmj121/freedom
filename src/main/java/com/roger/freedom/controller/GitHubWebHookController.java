package com.roger.freedom.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.roger.freedom.entity.KnowledgeBaseRelateProject;
import com.roger.freedom.entity.KnowledgeBaseRelateProjectDetail;
import com.roger.freedom.entity.MasterBranchRecords;
import com.roger.freedom.entity.ProdBranchRecords;
import com.roger.freedom.service.ResultProcessor;
import com.roger.freedom.service.impl.KnowledgeBaseRelateProjectDetailServiceImpl;
import com.roger.freedom.service.impl.KnowledgeBaseRelateProjectServiceImpl;
import com.roger.freedom.service.impl.MasterBranchRecordsServiceImpl;
import com.roger.freedom.service.impl.ProdBranchRecordsServiceImpl;
import com.roger.freedom.utils.MailUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"github添加webhook并根据webhook返回数据进行业务处理"})
@RequestMapping("/github")
@Slf4j
public class GitHubWebHookController {
    private static final Logger LOG = LoggerFactory.getLogger(GitHubWebHookController.class);

    @Autowired
    private KnowledgeBaseRelateProjectServiceImpl projectService;

    @Autowired
    private KnowledgeBaseRelateProjectDetailServiceImpl projectDetailService;

    @Autowired
    private ProdBranchRecordsServiceImpl prodBranch;

    @Autowired
    private MasterBranchRecordsServiceImpl masterBranch;

    @Autowired
    private MailUtil mailUtil;

    /**
     * 初始化操作：
     * # 1. 切换到放代码的目录下
     *   cd /root/base/code/code
     * # 2. clone，拉取下来所有分支的代码到本地仓库，并且默认拉取master分支代码到本地工作区，并且创建本地master分支
     *   git clone http://kanmeijie:kmj123456@17zhiliao.com:8088/bgcheck/ewe-bgcheck.git
     * # 3. 进入git项目
     * cd ewe-bgcheck
     * # 4. 从本地仓库拉取远程prod分支的代码，并创建本地分支prod
     * git checkout -b prod origin/prod
     *
     * @param request
     */
    @PostMapping("/webhook")
    public void webhook(HttpServletRequest request) {
        /**
         * 1. 获取webhook传入参数
         */
        JSONObject jsonObject = getParamsIntoJsonObject(request);
        // 校验参数
        if (jsonObject == null) {
            checkNotify("webhook传入参数错误，request中取出json为null");
            return;
        }
        System.out.println(jsonObject.toJSONString());
        /**
         * 一层数据
         */
        // 仓库信息
        JSONObject repository = jsonObject.getJSONObject("repository");
        // 校验参数
        if (repository == null) {
            checkNotify("webhook传入参数错误，request中取出json中repository为null");
            return;
        }
        // 分支名称
        String ref = jsonObject.getOrDefault("ref", "").toString();
        // 校验参数
        if (StringUtils.isBlank(ref)) {
            checkNotify("webhook传入参数错误，request中取出json中ref参数错误");
            return;
        }
        System.out.println("ref=" + ref);
        // commit信息：用户的每次提交，以及merge也作为一次提交，区别在于commits[][distinct]值，如果是merge，则为true，否则为false
        JSONArray commitsJsonArray = jsonObject.getJSONArray("commits");
        // 校验参数
        if (!checkCommits(commitsJsonArray)) {
            checkNotify("webhook传入参数错误，request中取出json中commits参数错误");
            return;
        }
        String before = jsonObject.getOrDefault("before", "").toString();
        String after = jsonObject.getOrDefault("after", "").toString();
        // 校验参数
        if (StringUtils.isBlank(before) || StringUtils.isBlank(after)) {
            checkNotify("webhook传入参数错误，request中取出json中before/after参数错误，before=" + before + "，after=" + after);
            return;
        }
        System.out.println("before=" + before + ",after=" + after);
        /**
         * 二层数据
         */
        // 项目名称
        String name = repository.getOrDefault("name", "").toString();
        System.out.println("name=" + name);
        // 仓库url地址，例如：https://github.com/kmj121/demo
        String svnUrl = repository.getOrDefault("svn_url", "").toString();
        System.out.println("svnUrl=" + svnUrl);
        if (StringUtils.isBlank(name) || StringUtils.isBlank(svnUrl)) {
            checkNotify("webhook传入参数错误，request中取出json中repository下name/svn_url参数错误，name=" + name + ", svnUrl" + svnUrl);
            return;
        }

        // 判断是否为prod分支或者master分支，如果不是，不作处理
        if (!ref.equals("refs/heads/prod") && !ref.equals("refs/heads/master")) {
            LOG.info("不是prod或者master分支，不做处理，ref=" + ref);
            return;
        }

        // 通过svn_url判断项目是否在知识库维护范围内，如果不在，不作处理
        KnowledgeBaseRelateProject project = projectService.selectBySvnUrl(svnUrl);
        if (project == null) {
            LOG.info("项目：" + name +"，不在维护范围内");
            return;
        }

        /**
         * 针对master分支和prod分支进行响应逻辑处理：
         *
         *  master分支：
         *      1. 执行脚本，拉取gitlab最新代码
         *      2. 根据维护中的项目名、文件名、文件中的开始标志、文件中的结束标志 去代码文件夹中搜索，
         *         搜索到，则截取开始标志与结束标志之间的内容，与原内容进行比较，如果有改动，则更新到
         *         数据库中，如果数据库中原来存放截取内容的字段为空，则说明是第一次将截取内容存放到表中。
         *  prod分支：
         *      1. 将每次提交都记录到表中
         */

        /**
         * 问题：
         * 1. 一次拉取的可能是多次commit的内容，没有办法确定是哪次commit去做的目标文件的修改，所以无法确定修改人。只能通过
         * 管理员手动去选中被改动方法所有内容，右键 -> Git -> Show History for Selection，查看具体修改人。
         *
         * 目前只能实现，检测到目标文件有改动，通知管理员去更新知识库。
         *
         * 寻求解决方案：
         * 1. github能不能感知自定义的内容发生变化，通过接口通知出来？这样就可以定位到最小的提交单元，即一个commit。
         */
        // 如果是master分支
        if (ref.equals("refs/heads/master")) {
            // 脚本存放的路径
            String webhookShellPath = "/Users/kanmeijie/Desktop/webhook.sh";
            List<String> shellResultList = new ArrayList<>();
            /**
             * 待解决：（拉取时有可能两次merge一次性拉取下来，当第一个merge拉取没有成功，然后有一个merge过来，此时第二次重试会拉取两次需要merge的代码）
             *
             * 三次重试机制以及判断是否需要重试说明：
             *
             * git拉去代码，shell脚本打印的内容：
             *      没有拉取到（网络不稳定或者其他原因）：
             *          Hi, demo
             *          cd /Users/kanmeijie/Desktop/demo
             *          Your branch is up to date with 'origin/prod'.
             *          Script execution complete
             *      拉取到：
             *          Hi, demo
             *          cd /Users/kanmeijie/Desktop/demo
             *          Your branch is up to date with 'origin/prod'.
             *          Updating d479107..c849b0a
             *          Fast-forward
             *          README.md | 2 ++
             *          1 file changed, 2 insertions(+)
             *          Script execution complete
             *
             * 所以根据返回结果中是否有"Fast-forward"内容，来判断是否拉取到最新代码，
             */
            // 三次重试
            Boolean flag = null;
            for (int i = 0; i < 3; i++) {
                flag = true;
                shellResultList = executeWebhookShell(project.getProjectName(), webhookShellPath);
                if (!CollectionUtils.isEmpty(shellResultList) && shellResultList.contains("Fast-forward")) {
                    break;
                } else {
                    flag = false;
                }
            }
            logList(shellResultList);
            // 如果三次重试，仍然失败，则发送邮件通知
            if (!flag) {
                // 邮件通知，三次重试，仍然无法拉取到最新代码
                mailUtil.sendMail("18516314504@163.com", "git拉取最新代码出错", "三次重试，仍然无法拉取到最新代码");
                LOG.info("三次重试，仍然无法拉取到最新代码");
                return;
            }

            // 查询knowledge_base_relate_project_detail表，获取项目下维护的文件名
            List<KnowledgeBaseRelateProjectDetail> detailList = projectDetailService.selectByProjectId(project.getId());
            // 遍历
            for (KnowledgeBaseRelateProjectDetail item : detailList) {
                File file = new File(item.getFileName());
                if (!file.exists() || !file.isFile()) {
                    // 邮件通知，维护的文件不存在，可能被删除了
                    mailUtil.sendMail("18516314504@163.com", "维护的文件不存在", "维护的文件不存在，可能被删除了");
                    LOG.info("维护的文件不存在，可能被删除了");
                    continue;
                }
                // todo 读取文件内容
                Map resultMap = readFileIntoStringArrList(file);
                // 读取内容出错
                if ("9999".equals(resultMap.get("code"))) {
                    // 邮件通知，读取文件内容出错
                    mailUtil.sendMail("18516314504@163.com", "读取文件内容出错", "读取文件内容出错");
                    LOG.info("读取文件内容出错");
                    continue;
                }
                // 内容为空
                List<String> contentList = (List<String>) resultMap.get("content");
                if (CollectionUtils.isEmpty(contentList)) {
                    // 邮件通知，文件内容为空
                    mailUtil.sendMail("18516314504@163.com", "读取文件内容为空", "读取文件内容为空");
                    LOG.info("读取文件内容为空");
                    continue;
                }
                // 确保每一个标志都是唯一的
                List<String> beginList = contentList.stream().filter(u -> u.equals(item.getBeginAnnotation())).collect(Collectors.toList());
                List<String> endList = contentList.stream().filter(u -> u.equals(item.getEndAnnotation())).collect(Collectors.toList());
                // 校验开始标志结束标志
                if (CollectionUtils.isEmpty(beginList) || beginList.size() > 1
                        || CollectionUtils.isEmpty(endList) || endList.size() > 1) {
                    // 邮件通知，开始标志/结束标志有问题，可能不存在，或者存在多个
                    mailUtil.sendMail("18516314504@163.com", "开始标志/结束标志错误", "开始标志/结束标志错误，可能不存在，或者存在多个");
                    LOG.info("开始标志/结束标志错误，可能不存在，或者存在多个");
                    continue;
                }
                // 截取开始标志和结束标志之间的内容(左闭右开)，与数据库的进行比对
                Integer beginIndex = contentList.indexOf(beginList.get(0));
                Integer endIndex = contentList.indexOf(endList.get(0));
                List<String> subList = contentList.subList(beginIndex + 1, endIndex);

                //list转String
                String contentNew = StringUtils.join(subList.toArray(), "\\n");
                String contentOld = item.getContent();
                String md5New = new String(DigestUtils.md5Digest(contentNew.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                String md5Old = new String(DigestUtils.md5Digest(contentOld.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                if (!md5New.equals(md5Old)) {
                    // 邮件通知，知识库维护内容发生了改变（邮件中写出改变的内容所在的具体文件路径（路径中包含项目的文件夹）），并将新的内容更新到数据库
                    mailUtil.sendMail("18516314504@163.com", "知识库维护内容发生了改变", "文件所在服务器路径：" + item.getFileName());
                    LOG.info("知识库维护内容发生了改变，文件所在服务器路径：" + item.getFileName());
                    item.setContent(contentNew);
                    projectDetailService.updateOne(item);
                }
            }
        } else { // prod分支
            // begin annotation freedom 111
            for (Object item : commitsJsonArray) {
                ProdBranchRecords prodBranchRecords = new ProdBranchRecords();
                prodBranchRecords.setBefore(before);
                prodBranchRecords.setAfter(after);
                prodBranchRecords.setCommitId(((JSONObject) item).getOrDefault("id", "").toString());
                prodBranchRecords.setDistinct((Boolean) ((JSONObject) item).get("distinct") == true ? 1 : 0);
                prodBranchRecords.setCreateTime(new Date());
                prodBranch.insert(prodBranchRecords);
            }
            // end annotation freedom 111
        }
    }

    /**
     *执行shell脚本
     *
     * @param webhookShellPath 脚本路径
     * @param processor 处理器，处理执行脚本返回的数据
     * @param param 参数数组
     * @return
     */
    private List<String> runCommand(String webhookShellPath, ResultProcessor processor, String... param) {
        List<String> resultList = new ArrayList<String>();
        try {
            String[] cmd = new String[]{webhookShellPath};
            //为了解决参数中包含空格
            cmd = ArrayUtils.addAll(cmd, param);

            Long time1 = System.currentTimeMillis();
            System.out.println("======执行shell脚本开始时间：" + time1);
            //解决脚本没有执行权限
            ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", webhookShellPath);
            Process process = builder.start();
            process.waitFor();

            Process pro = Runtime.getRuntime().exec(cmd);
            pro.waitFor();

            InputStream in = pro.getInputStream();
            Long time2 = System.currentTimeMillis();
            System.out.println("======执行shell脚本结束时间：" + time2);
            System.out.println("======执行shell脚本花费时间：" + (time2 - time1));
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String line = null;
            System.out.println("======处理shell脚本返回数据开始时间：" + time2);
            while ((line = read.readLine()) != null) {
                processor.process(resultList, line);
            }
            Long time3 = System.currentTimeMillis();
            System.out.println("======处理shell脚本返回数据结束时间：" + time3);
            System.out.println("======处理shell脚本返回数据花费时间：" + (time3 - time2));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * 当代码push到prod分支上，执行webhook脚本
     */
    public List<String> executeWebhookShell(String projectName, String webhookShellPath){
        List<String> resultList = runCommand(webhookShellPath, new ResultProcessor() {
            @Override
            public void process(List<String> textList, String text) {
                // 将执行完shell脚本的返回内容返回给用户，打印出来
                textList.add(text);
            }
        }, projectName);
        return resultList;
    }

    /**
     * list日志打印
     */
    private void logList(List<String> resultList){
        if (resultList == null || resultList.size() == 0) {
            System.out.println("resultList 为空");
            return;
        }
        for (String result : resultList) {
            System.out.println(result);
        }
    }

    /**
     * 功能：Java读取文件的内容
     *      步骤：
     *          1：先获得文件句柄
     *          2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     *          3：读取到输入流后，需要读取生成字节流
     *          4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
     *
     * @param file: 文件
     * @return 将这个文件按照每一行切割成数组存放到contentList中。
     */
    public static Map readFileIntoStringArrList(File file) {
        Map map = new HashMap();
        List<String> contentList = new ArrayList<String>();
        try {
            String encoding = "utf-8";
            // 考虑到编码格式
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;

            while ((lineTxt = bufferedReader.readLine()) != null) {
                contentList.add(lineTxt);
            }

            bufferedReader.close();
            read.close();
            map.put("code", "0000");
            map.put("content", contentList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", "9999");
            map.put("content", "读取文件内容出错");
        }

        return map;
    }

    public Boolean checkCommits(JSONArray commitsJsonArray) {
        if (CollectionUtils.isEmpty(commitsJsonArray)) {
            return false;
        }
        for (Object item : commitsJsonArray) {
            JSONObject commitJsonObject = (JSONObject) item;
            String commitId = commitJsonObject.getOrDefault("id", "").toString();
            Boolean distinct = (Boolean) commitJsonObject.get("distinct");
            if (StringUtils.isBlank(commitId)) {
                LOG.info("commitId为空或者null");
                return false;
            }
            if (distinct == null) {
                LOG.info("commitId为空或者null");
                return false;
            }
        }
        return true;
    }

    public void checkNotify(String errorMsg) {
        // 邮件通知
        mailUtil.sendMail("18516314504@163.com", "参数校验错误", errorMsg);
        System.out.println(errorMsg);
    }

    /**
     * request转换为json
     */
    private JSONObject getParamsIntoJsonObject(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                sb.append(inputStr);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return JSONObject.parseObject(sb.toString());
    }
}