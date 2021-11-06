package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.AnalyseDAO;
import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.DAO.UserDAO;
import com.example.webpractice.bl.AnalyseService;
import com.example.webpractice.config.AliyunAppendixConfig;
import com.example.webpractice.config.MainConfig;
import com.example.webpractice.po.Analyse;
import com.example.webpractice.po.Papers;
import com.example.webpractice.util.*;
import com.example.webpractice.vo.AnalyseVO;
import com.example.webpractice.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    //解释文件本地临时存储目录
    private static final String AnalyseLocalDir = FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
            MainConfig.ANALYSE);

    //pdf文件本地临时存储目录
    private static final String PdfLocalDir = FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
            MainConfig.Pdf);

    private static final String ossAnalyseDir = "内化文件/";

    /**
     * 得到内化内容
     *
     * @param id
     * @return
     */
    @Override
    public ResponseVO getAnalyseById(int id) {
        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }
        Analyse analyse = analyseDAO.findById(id).get();
        //正文文件的名称
        String fileName = analyse.getContent();
        //这是文件在阿里云中的存储位置
        String ossPath = ossAnalyseDir + fileName;

        //本地路径
        String path = FileUtil.makeFile(AnalyseLocalDir, fileName);
        // System.out.println(path);
        File file = new File(path);
        //将文件下载到本地临时存储位置

        ossFileManager.downloadFile(aliyunAppendixConfig.getBucketName(),
                ossPath, aliyunAppendixConfig.OSSClient1(), file);
        analyse.setContent(ossFileManager.readWord(path));
        if (analyse.getContent() == null) {
            return ResponseVO.buildFailure("读取正文失败");
        }
        //读取完删除临时文件
        FileUtil.deleteDirRecursion(path);
        AnalyseVO analyseVO = new AnalyseVO(analyse);
        analyseVO.setInput_user(userDAO.getUsernameById(analyse.getUser_id()));
        return ResponseVO.buildSuccess(analyseVO);
    }

    /**
     * 上传内化内容
     *
     * @param title
     * @param number
     * @param category
     * @param interpret
     * @param input_user
     * @param input_time
     * @param multipartFile
     * @param paper_id
     * @return
     */
    @Override
    public ResponseVO addAnalyse(String title, String number, String category, String interpret, String input_user, String input_time, MultipartFile multipartFile, String paper_id) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }

        int userId = Integer.parseInt(input_user);

        String fileName = userId + "-" + multipartFile.getOriginalFilename();
        if (analyseDAO.numOfFileName(fileName) != 0) {
            return ResponseVO.buildFailure("正文文件名已存在");
        }
        if (analyseDAO.numOfTitle(title, userId) != 0) {
            return ResponseVO.buildFailure("标题已存在");
        }
        if (userDAO.UserExistsById(Integer.parseInt(input_user)) == 0) {
            return ResponseVO.buildFailure("用户名不存在");
        }
        if (multipartFile.isEmpty()) {
            return ResponseVO.buildFailure("文件不可为空");
        }

        //本地临时存储正文文件夹的路径
        //正文文件的完整路径
        String fullPath = FileUtil.makeFile(AnalyseLocalDir, fileName);
        if (!FileUtil.saveFile(multipartFile, fullPath)) {
            return ResponseVO.buildFailure("未知错误 正文文件存储失败");
        }
        Timestamp input = new Timestamp(DateUtil.dateToStamp(input_time));

        Analyse analyse = new Analyse(title, number, category, interpret, userId, input, fileName);
        int id = analyseDAO.save(analyse).getId();
        //把本地的文件存入阿里云
        //本地临时文件
        File file = new File(fullPath);
        String ossPath = ossAnalyseDir + fileName;
        ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(), ossPath, file, aliyunAppendixConfig.OSSClient1());
        //删除本地的临时文件
        FileUtil.deleteDirRecursion(fullPath);

        paperDAO.analyze(Integer.parseInt(paper_id), id);  //更新paper
        return ResponseVO.buildSuccess(id);
    }

    /**
     * 更新内化内容
     *
     * @param id
     * @param title
     * @param number
     * @param category
     * @param interpret
     * @param input_user
     * @param input_time
     * @param multipartFile
     * @return
     */
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

            String content = paperDAO.findContentById(id);
            //本地完整路径
            String fullPath = FileUtil.makeFile(AnalyseLocalDir, fileName);
            //先要在oss上删除旧的文件
            String oldOssPath = ossAnalyseDir + content;
            String newOssPath = ossAnalyseDir + fileName;
            ossFileManager.deleteFile(aliyunAppendixConfig.getBucketName(),
                    oldOssPath, aliyunAppendixConfig.OSSClient1());
            //然后上传新文件
            if (!FileUtil.saveFile(multipartFile, fullPath)) {
                return ResponseVO.buildFailure("未知错误 正文文件存储失败");
            }
            File file = new File(fullPath);
            ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(),
                    newOssPath, file, aliyunAppendixConfig.OSSClient1());
            //删除本地的临时文件
            FileUtil.deleteDirRecursion(fullPath);

            //数据库的正文字段就存储文件名
            analyseDAO.updateWithFile(title, number, category, interpret, userId, input, fileName, id);
        }
        return ResponseVO.buildSuccess();
    }

    /**
     * 获取解释结果的pdf文件
     *
     * @param id
     * @param response
     */
    @Override
    public void downloadResultFile(int id, HttpServletResponse response) {
        Analyse analyse = analyseDAO.findById(id).get();
        String sqlFileName = analyse.getContent();
        String fileName = sqlFileName.substring(sqlFileName.indexOf(':') + 1);
        String ContentPath = FileUtil.makeFile(AnalyseLocalDir, fileName);
        String ossPath = ossAnalyseDir + fileName;
        File ContentFile = new File(ContentPath);
        ossFileManager.downloadFile(aliyunAppendixConfig.getBucketName(),
                ossPath, aliyunAppendixConfig.OSSClient1(), ContentFile);
        AnalyseVO analyseVO = new AnalyseVO(analyse);
        analyseVO.setContent(ossFileManager.readWord(ContentPath));

        //读取完删除临时文件
        FileUtil.deleteDirRecursion(ContentPath);

        File folder = new File(PdfLocalDir);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        String name = analyse.getPaper_number() + ".pdf";
        String PdfPath = FileUtil.jointPath(PdfLocalDir, name);
        PdfUtil.make(PdfPath, analyseVO);
        File file = new File(PdfPath);
        FileInputStream bis = null;
        OutputStream bos = null;
        try {
            response.setContentType("application/pdf");
            bis = new FileInputStream(file);

            bos = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("导出附件文件{}失败", name);

        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null)
                        try {
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (!file.delete()) {
                                log.warn("文件{}删除失败", PdfPath);
                            }
                        }
                }
        }


    }
}
