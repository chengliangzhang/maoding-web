package com.maoding.attach.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 文件路径，如果是fastdfs,fastdfsUrl为fastdfs服务器的地址
 */
@Configuration
@PropertySource(value = {"classpath:properties/system.properties"})
public class FastdfsUrlServer {

    public static String fastdfsUrl;

    @Bean
    public FastdfsUrlServer getFastUrlBean(@Value("${fastdfs.url}") String fastdfsUrl){
        this.fastdfsUrl = fastdfsUrl;
        return new FastdfsUrlServer();
    }
}
