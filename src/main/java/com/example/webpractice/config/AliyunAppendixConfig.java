package com.example.webpractice.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS 附件bucket配置类
 * bucket下有两个目录:
 * /正文文件
 * /附件
 *
 * @Author MengYuxin
 * @Date 2021/10/29 18:09
 */


@Component
@ConfigurationProperties(prefix = "aliyunappendix")
//指定配置文件
@PropertySource(value = "classpath:aliyunAppendix.properties")
@Data
@Slf4j
public class AliyunAppendixConfig {

    private String endPoint;// 地域节点

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;// OSS的Bucket名称

    private String urlPrefix;// Bucket 域名


    // 将OSS 客户端交给Spring容器托管
    @Bean
    public OSS OSSClient1() {
        return new OSSClient(endPoint, accessKeyId, accessKeySecret);
    }


}
