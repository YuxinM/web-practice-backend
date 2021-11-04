package com.example.webpractice.blImpl;

import com.aliyun.oss.model.SimplifiedObjectMeta;
import com.example.webpractice.DAO.AppendixDAO;
import com.example.webpractice.DAO.UserDAO;
import com.example.webpractice.bl.AppendixService;
import com.example.webpractice.config.AliyunAppendixConfig;
import com.example.webpractice.config.MainConfig;
import com.example.webpractice.po.Appendix;
import com.example.webpractice.util.FileUtil;
import com.example.webpractice.util.OssFileManager;
import com.example.webpractice.util.SessionManager;
import com.example.webpractice.vo.AppendixVO;
import com.example.webpractice.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/31 19:49
 */

@Service
@Slf4j
public class AppendixServiceImpl implements AppendixService {

    private static final String AppendixLocalDir = FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
            MainConfig.APPENDIX);

    private static final String ossAppendixDir = "附件/";

    @Autowired
    AppendixDAO appendixDAO;

    @Autowired
    OssFileManager ossFileManager;

    @Autowired
    AliyunAppendixConfig aliyunAppendixConfig;


    /**
     * 上传附件
     *
     * @param paperId
     * @param files
     * @return
     */
    @Override
    public ResponseVO uploadAppendix(int paperId, MultipartFile[] files) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }

        int userId = SessionManager.getLoginUser().getId(); //用户id
        String user_name = SessionManager.getLoginUser().getUsername();

        if (files == null || files.length == 0) {
            return ResponseVO.buildSuccess("文件不能为空");
        }
        File folder = new File(AppendixLocalDir);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        for (MultipartFile multipartFile : files) {
            String name = userId + "-" + multipartFile.getOriginalFilename();

            if (appendixDAO.findByFileName(name).size() != 0) {
                return ResponseVO.buildFailure("文件名已存在");
            }
            String fullPath = FileUtil.jointPath(AppendixLocalDir, name);
            if (!FileUtil.saveFile(multipartFile, fullPath)) {
                return ResponseVO.buildFailure("未知错误 正文文件存储失败");
            }
            File file = new File(fullPath);
            String ossPath = ossAppendixDir + name;
            //将文件上传到阿里云
            ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(),
                    ossPath, file, aliyunAppendixConfig.OSSClient1());
            //删除本地临时文件
            FileUtil.deleteDirRecursion(fullPath);
            Appendix appendix = new Appendix(name, user_name, paperId);
            appendixDAO.save(appendix);
        }
        return ResponseVO.buildSuccess();
    }

    /**
     * 获取附件
     *
     * @param paperId
     * @return
     */
    @Override
    public ResponseVO getAppendix(int paperId) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }
        List<Appendix> appendices = appendixDAO.findByPaperId(paperId);
        List<AppendixVO> appendixVOS = new ArrayList<>();
        if (appendices == null) {
            return ResponseVO.buildSuccess(appendixVOS);
        }
        for (Appendix appendix : appendices) {
            String ossPath = ossAppendixDir + appendix.getFile_name();
            SimplifiedObjectMeta meta = ossFileManager.getFileInfo(
                    aliyunAppendixConfig.getBucketName(), ossPath,
                    aliyunAppendixConfig.OSSClient1()
            );
            long size = meta.getSize();
            AppendixVO appendixVO = new AppendixVO(appendix.getId(), appendix.getFile_name(),
                    String.valueOf(size), appendix.getUser_name());
            appendixVOS.add(appendixVO);
        }
        return ResponseVO.buildSuccess(appendixVOS);
    }


    /**
     * 删除附件
     *
     * @param id
     * @return
     */
    @Override
    public ResponseVO deleteAppendix(int id) {

        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }

        Appendix appendix = appendixDAO.findAppendixById(id);
        String ossPath = ossAppendixDir + appendix.getFile_name();
        //在阿里云中删除文件
        ossFileManager.deleteFile(aliyunAppendixConfig.getBucketName(),
                ossPath, aliyunAppendixConfig.OSSClient1());
        appendixDAO.deleteById(id);
        return ResponseVO.buildSuccess();
    }

    @Override
    public void downloadAppendix(int id, HttpServletResponse response) {

        String fileName = appendixDAO.findFilenameById(id);
        String ossPath = ossAppendixDir + fileName;
        File folder = new File(AppendixLocalDir);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        //本地路径
        String path = FileUtil.jointPath(AppendixLocalDir, fileName);
        File file = new File(path);
        //将文件下载到本地临时存储位置
        ossFileManager.downloadFile(aliyunAppendixConfig.getBucketName(),
                ossPath, aliyunAppendixConfig.OSSClient1(), file);

        FileInputStream bis = null;
        OutputStream bos = null;
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            bis = new FileInputStream(file);

            bos = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("导出附件文件{}失败", fileName);

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
                                log.warn("文件{}删除失败", path);
                            }
                        }
                }
        }

    }
}
