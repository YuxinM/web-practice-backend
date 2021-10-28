package com.example.webpractice.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置类
 *
 * @Author MengYuxin
 * @Date 2021/10/25 19:35
 */


@Component
@ConfigurationProperties(prefix = "aliyun")
//指定配置文件
@PropertySource(value = "classpath:aliyun.properties")
@Data
@Slf4j
public class AliyunConfig {


    private String endPoint;// 地域节点

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;// OSS的Bucket名称

    private String urlPrefix;// Bucket 域名


    // 将OSS 客户端交给Spring容器托管
    @Bean
    public OSS OSSClient() {
        return new OSSClient(endPoint, accessKeyId, accessKeySecret);
    }
}
