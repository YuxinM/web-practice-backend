package com.example.webpractice.blImpl.prepare;

import com.example.webpractice.bl.prepare.BeforeRunService;
import com.example.webpractice.config.AliyunAppendixConfig;
import com.example.webpractice.config.MainConfig;
import com.example.webpractice.util.FileUtil;
import com.example.webpractice.util.OssFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/11/3 23:30
 */

@Service
public class BeforeRunServiceImpl implements BeforeRunService {

    @Autowired
    OssFileManager ossFileManager;

    @Autowired
    AliyunAppendixConfig aliyunAppendixConfig;

    /**
     * 删除本地临时的文件
     */
    @Override
    public void deleteFile() {
        FileUtil.deleteDirRecursion(FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
                MainConfig.USER_DATA_DIR_NAME));
        FileUtil.deleteDirRecursion(FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
                MainConfig.CONTENT));
        FileUtil.deleteDirRecursion(FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
                MainConfig.APPENDIX));
    }

    /**
     * 删除阿里云oss上残留的文件
     */
    @Override
    public void deleteOssFile() {
        List<String> fileNames= ossFileManager.getFileNames(aliyunAppendixConfig.getBucketName(),
                aliyunAppendixConfig.OSSClient1());
        for(String name:fileNames){
            ossFileManager.deleteFile(aliyunAppendixConfig.getBucketName(),name,
                    aliyunAppendixConfig.OSSClient1());
        }
    }
}
