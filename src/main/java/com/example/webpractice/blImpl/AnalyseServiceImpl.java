package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.AnalyseDAO;
import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.DAO.UserDAO;
import com.example.webpractice.bl.AnalyseService;
import com.example.webpractice.config.AliyunAppendixConfig;
import com.example.webpractice.config.MainConfig;
import com.example.webpractice.po.Analyse;
import com.example.webpractice.po.Papers;
import com.example.webpractice.util.DateUtil;
import com.example.webpractice.util.FileUtil;
import com.example.webpractice.util.OssFileManager;
import com.example.webpractice.util.SessionManager;
import com.example.webpractice.vo.AnalyseVO;
import com.example.webpractice.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;

/**
 * @Author PanYue
 * @Date 2021/11/3 14:16
 */

@Service
@Slf4j
public class AnalyseServiceImpl implements AnalyseService {

    @Autowired
    AnalyseDAO analyseDAO;

    @Autowired
    PaperDAO paperDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    OssFileManager ossFileManager;

    @Autowired
    AliyunAppendixConfig aliyunAppendixConfig;

    //正文文件本地临时存储目录
    private static final String ContentLocalDir = FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
            MainConfig.CONTENT);

    private static final String ossContentDir = "正文文件/";

    @Override
    public ResponseVO getAnalyseById(int id) {
        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }
        Analyse analyse = analyseDAO.findById(id).get();
        if (analyse.getContent().startsWith("filename")) {
            //正文文件的名称
            String fileName = analyse.getContent().substring(analyse.getContent().indexOf(':') + 1);
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

            ossFileManager.downloadFile(aliyunAppendixConfig.getBucketName(),
                    ossPath, aliyunAppendixConfig.OSSClient1(), file);
            analyse.setContent(ossFileManager.readWord(path));
            if (analyse.getContent() == null) {
                return ResponseVO.buildFailure("读取正文失败");
            }
            //读取完删除临时文件
            FileUtil.deleteDirRecursion(path);
        }
        AnalyseVO analyseVO = new AnalyseVO(analyse);
        analyseVO.setInput_user(userDAO.getUsernameById(analyse.getUser_id()));
        return ResponseVO.buildSuccess(analyseVO);
    }

    @Override
    public ResponseVO addAnalyse(String title, String number, String category, String interpret, String input_user, String input_time, MultipartFile multipartFile, String paper_id) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }
//        System.out.println(input_user);

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
        Timestamp input = new Timestamp(DateUtil.dateToStamp(input_time));

        Analyse analyse = new Analyse(title, number, category, interpret, userId, input, sqlFileName);
        int id = analyseDAO.save(analyse).getId();
        //把本地的文件存入阿里云
        //本地临时文件
        File file = new File(fullPath);
        String ossPath = ossContentDir + fileName;
        ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(), ossPath, file, aliyunAppendixConfig.OSSClient1());
        //删除本地的临时文件
        FileUtil.deleteDirRecursion(fullPath);

        paperDAO.analyze(Integer.parseInt(paper_id), id);  //更新paper
        return ResponseVO.buildSuccess(id);
    }

    @Override
    public ResponseVO updateAnalyse(int id, String title, String number, String category, String interpret, String input_user, String input_time, MultipartFile multipartFile) {
        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }

        Timestamp input = new Timestamp(DateUtil.dateToStamp(input_time));
        int userId = Integer.parseInt(input_user);
        //正文文件不更新的情况
        if (multipartFile == null) {
            analyseDAO.updateWithNoFile(title, number, category, interpret, userId, input, id);
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
            analyseDAO.updateWithFile(title, number, category, interpret, userId, input, sqlName, id);
        }
        return ResponseVO.buildSuccess();
    }
}
