package com.example.webpractice;

import com.example.webpractice.bl.LibraryCreateService;
import com.example.webpractice.bl.prepare.BeforeRunService;
import com.example.webpractice.config.MainConfig;
import com.example.webpractice.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * @Author MengYuxin
 * @Date 2021/10/26 17:28
 */

@Component
public class DemoApplicationRunner implements ApplicationRunner {

    @Autowired
    LibraryCreateService libraryCreateService;

    @Autowired
    BeforeRunService beforeRunService;


    /**
     * 读取爬取的数据
     *
     * @param args
     * @throws Exception
     */

    @Override
    public void run(ApplicationArguments args) throws Exception {
        beforeRunService.deleteFile();
        beforeRunService.deleteOssFile();
        try {
            libraryCreateService.writeInDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
