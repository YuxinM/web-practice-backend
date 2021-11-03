package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.AppendixDAO;
import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.DAO.UserDAO;
import com.example.webpractice.bl.AppendixService;
import com.example.webpractice.bl.PaperService;
import com.example.webpractice.config.AliyunAppendixConfig;
import com.example.webpractice.config.MainConfig;
import com.example.webpractice.po.Appendix;
import com.example.webpractice.po.ChartData;
import com.example.webpractice.po.Papers;
import com.example.webpractice.util.DateUtil;
import com.example.webpractice.util.FileUtil;
import com.example.webpractice.util.OssFileManager;
import com.example.webpractice.util.SessionManager;
import com.example.webpractice.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 18:43
 */

@Service
public class PaperServiceImpl implements PaperService {


    @Autowired
    PaperDAO paperDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    AppendixDAO appendixDAO;

    @Autowired
    OssFileManager ossFileManager;

    @Autowired
    AliyunAppendixConfig aliyunAppendixConfig;

    @Autowired
    AppendixService appendixService;

    //正文文件本地临时存储目录
    private static final String ContentLocalDir = FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
            MainConfig.CONTENT);

    private static final String ossContentDir="正文文件/";


    /**
     * 根据id获取法规详情
     *
     * @param id
     * @return
     */
    @Override
    public ResponseVO getPaperById(int id) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }

        List<Papers> papers = paperDAO.getPapersById(id);
        if (papers.size() == 0) {
            return ResponseVO.buildFailure("对应id的法律文书不存在");
        } else {
            Papers target = papers.get(0);
            String release_time = DateUtil.StampToDate(target.getRelease_time());
            String implement_time = DateUtil.StampToDate(target.getImplement_time());
            String input_time = DateUtil.StampToDate(target.getInput_time());
            String input_user = userDAO.getUsernameById(target.getUser_id());
            String content = "";
            /*
             * 如果正文字段是正文说明是爬取数据，存在数据库
             * 如果是文件名则去阿里云来读文件  filename: 文件路径
             */
            if (target.getContent().startsWith("filename")) {
                //正文文件的名称
                String fileName = target.getContent().substring(target.getContent().indexOf(':') + 1);
                //这是文件在阿里云中的存储位置
                String ossPath = ossContentDir + fileName;
                //System.out.println(ossPath);
                //从阿里云获取文件的流

                File folder = new File(ContentLocalDir);
                if (!folder.exists() && !folder.isDirectory()) {
                    folder.mkdirs();
                }
                //本地路径
                String path = FileUtil.jointPath(ContentLocalDir, fileName);
                // System.out.println(path);
                File file = new File(path);
                //System.out.println(file.exists());
                //将文件下载到本地临时存储位置

                ossFileManager.downloadContent(aliyunAppendixConfig.getBucketName(),
                        ossPath, aliyunAppendixConfig.OSSClient1(), file);
                content = ossFileManager.readWord(path);
                if (content == null) {
                    return ResponseVO.buildFailure("读取正文失败");
                }
                //读取完删除临时文件
                FileUtil.deleteDirRecursion(path);
            } else {
                content = target.getContent();
            }
            PaperVO paperVO = new PaperVO(target.getId(), target.getTitle(),
                    target.getPaper_number(), target.getCategory(), target.getDepartment(),
                    release_time, implement_time, target.getGrade(), target.getInterpret_department(),
                    input_user, input_time, content, target.getStatus() == 1, target.getAnalyse_id());
            return ResponseVO.buildSuccess(paperVO);

        }
    }

    /**
     * 添加一条法规
     *
     * @param title
     * @param number
     * @param category
     * @param department
     * @param grade
     * @param release_time
     * @param implement_time
     * @param interpret
     * @param input_user
     * @param input_time
     * @param multipartFile  文件
     * @param status
     * @param analyse_id
     * @return
     */
    @Override
    public ResponseVO addPaper(String title, String number, String category,
                               String department, String grade, String release_time,
                               String implement_time, String interpret, String input_user,
                               String input_time, MultipartFile multipartFile, String status, String analyse_id) {


        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }
        System.out.println(input_user);

        int userId = Integer.parseInt(input_user);

        String fileName = userId + "-" + multipartFile.getOriginalFilename();
        String sqlFileName = "filename:" + fileName;
        if (paperDAO.numOfFileName(sqlFileName) != 0) {
            return ResponseVO.buildFailure("正文文件名已存在");
        }
        if (paperDAO.numOfTitle(title, userId) != 0) {
            return ResponseVO.buildFailure("标题已存在");
        }
        if (userDAO.UserExistsById(Integer.parseInt(input_user)) == 0) {
            return ResponseVO.buildFailure("用户名不存在");
        }
        if (multipartFile.isEmpty()) {
            return ResponseVO.buildFailure("文件不可为空");
        }

        //本地临时存储正文文件夹的路径
        File folder = new File(ContentLocalDir);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        //正文文件的完整路径
        String fullPath = FileUtil.jointPath(ContentLocalDir, fileName);
        if (!FileUtil.saveFile(multipartFile, fullPath)) {
            return ResponseVO.buildFailure("未知错误 正文文件存储失败");
        }
        Timestamp release = new Timestamp(DateUtil.dateToStamp(release_time));
        Timestamp implement = new Timestamp(DateUtil.dateToStamp(implement_time));
        Timestamp input = new Timestamp(DateUtil.dateToStamp(input_time));
        int st = status.equals("true") ? 1 : 0;

        Papers papers = new Papers(title, number, category, department,
                release, implement, grade, interpret, userId, input, sqlFileName, st, -1);
        int id = paperDAO.save(papers).getId();
        //把本地的文件存入阿里云
        //本地临时文件
        File file = new File(fullPath);
        String ossPath = ossContentDir + fileName;
        ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(),
                ossPath, file, aliyunAppendixConfig.OSSClient1());
        //删除本地的临时文件
        FileUtil.deleteDirRecursion(fullPath);
        return ResponseVO.buildSuccess(id);
    }

    /**
     * 批量废除法规
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseVO abolish(List<Integer> ids) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }

        if (ids == null) {
            return ResponseVO.buildFailure("id数组为空");
        }
        for (Integer id : ids) {
            paperDAO.updateStatus(0, id);
        }
        return ResponseVO.buildSuccess();
    }

    /**
     * 批量发布法规
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseVO publish(List<Integer> ids) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }
        if (ids == null) {
            return ResponseVO.buildFailure("id数组为空");
        }
        for (Integer id : ids) {
            paperDAO.updateStatus(1, id);
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO updatePaper(int id, String title, String number,
                                  String category, String department, String grade,
                                  String release_time, String implement_time, String interpret,
                                  String input_user, String input_time, MultipartFile multipartFile,
                                  String status, String analyse_id) {


        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }

        if (paperDAO.numOfId(id) == 0) {
            return ResponseVO.buildFailure("id不存在");
        }
        Timestamp release = new Timestamp(DateUtil.dateToStamp(release_time));
        Timestamp implement = new Timestamp(DateUtil.dateToStamp(implement_time));
        Timestamp input = new Timestamp(DateUtil.dateToStamp(input_time));
        int userId = Integer.parseInt(input_user);
        int st = status.equals("true") ? 1 : 0;
        int analyseId = Integer.parseInt(analyse_id);
        //正文文件不更新的情况
        if (multipartFile == null) {
            paperDAO.updateWithNoFile(title, number, category, department, release,
                    implement, grade, interpret, userId, input, st, id);
        } else {
            String fileName = userId + "-" + multipartFile.getOriginalFilename();
            String sqlName = "filename:" + fileName;//数据库正文字段的值(存文件名)
            String content = paperDAO.findContentById(id);
            //本地完整路径
            String fullPath = FileUtil.jointPath(ContentLocalDir, fileName);
            //本地临时存储正文文件夹的路径
            File folder = new File(ContentLocalDir);
            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }

            //如果原来是以文件形式存在
            if (content.startsWith("filename:")) {
                //先要在oss上删除旧的文件
                String oldName = content.substring(content.indexOf(':') + 1);
                String oldOssPath = ossContentDir + oldName;
                String newOldOssPath = ossContentDir + fileName;
                ossFileManager.deleteFile(aliyunAppendixConfig.getBucketName(),
                        oldOssPath, aliyunAppendixConfig.OSSClient1());
                //然后上传新文件
                if (!FileUtil.saveFile(multipartFile, fullPath)) {
                    return ResponseVO.buildFailure("未知错误 正文文件存储失败");
                }
                File file = new File(fullPath);
                ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(),
                        newOldOssPath, file, aliyunAppendixConfig.OSSClient1());
                //删除本地的临时文件
                FileUtil.deleteDirRecursion(fullPath);

            } else {
                //原来是以文字内容存在
                String ossPath = ossContentDir + fileName;
                if (!FileUtil.saveFile(multipartFile, fullPath)) {
                    return ResponseVO.buildFailure("未知错误 正文文件存储失败");
                }
                File file = new File(fullPath);
                ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(),
                        ossPath, file, aliyunAppendixConfig.OSSClient1());
                //删除本地的临时文件
                FileUtil.deleteDirRecursion(fullPath);
            }
            //数据库的正文字段就存储文件名
            paperDAO.updateWithFile(title, number, category, department,
                    release, implement, grade, interpret, userId, input,
                    sqlName, st, analyseId, id);
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO delete(List<Integer> ids) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }

        if (ids == null) {
            return ResponseVO.buildFailure("id数组为空");
        }
        for (Integer id : ids) {
            //删除相关的附件
            List<Appendix> appendices = appendixDAO.findByPaperId(id);
            for (Appendix appendix : appendices) {
                appendixService.deleteAppendix(appendix.getId());
            }
            Papers papers = paperDAO.findPapersById(id);
            if (papers.getContent().startsWith("filename")) {
                String fileName = papers.getContent().substring(papers.getContent().indexOf(':') + 1);
                String ossPath = ossContentDir + fileName;
                if (!ossFileManager.deleteFile(aliyunAppendixConfig.getBucketName(),
                        ossPath, aliyunAppendixConfig.OSSClient1())) {
                    return ResponseVO.buildFailure("删除阿里云上文件失败");

                }
            }
            paperDAO.deleteById(id);

        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO getStatisticalData() {
        //分类饼图
        List<ChartData> categoryPie = new ArrayList<>();
        List categoryPieList = paperDAO.getChartByCategory();
        for (Object row : categoryPieList) {
            Object[] cells = (Object[]) row;
            ChartData chartData = new ChartData();
            chartData.setName((String) cells[0]);
            chartData.setValue(((Number) cells[1]).longValue());
            categoryPie.add(chartData);
        }
        //每年法规
        List<ChartData> yearLine = new ArrayList<>();
        List yearLineList = paperDAO.getChartByYear();
        for (Object row : yearLineList) {
            Object[] cells = (Object[]) row;
            ChartData chartData = new ChartData();
            chartData.setName(String.valueOf(cells[0]));
            chartData.setValue(((Number) cells[1]).longValue());
            yearLine.add(chartData);
        }
        return ResponseVO.buildSuccess(new StatisticalDataVO(categoryPie, yearLine));
    }

    @Override
    public ResponseVO analyse(int id) {
        return ResponseVO.buildSuccess(paperDAO.analyse(id));
    }
}

