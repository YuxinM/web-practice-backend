package com.example.webpractice;

import com.example.webpractice.bl.LibraryCreateService;
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
    @Override
    public void run(ApplicationArguments args) throws Exception {
        libraryCreateService.writeInDatabase();
    }
}
