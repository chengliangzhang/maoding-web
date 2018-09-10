package com.maoding.project.constDefine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:properties/system.properties"})
public class EnterpriseServer {
    @Value("${yongyoucloud.enterpriseUrl}")
    public  String yongyouCloudServerUrl;

   // public static String URL_ENTERPRISE_HANDLE = "/enterpriseSearch";
     String URL_ENTERPRISE_QUERY_DETAIL = "/enterpriseSearch/queryDetail";
     String URL_ENTERPRISE_QUERY_FULL = "/enterpriseSearch/queryFull";

    @Bean
    public EnterpriseServer getImServer(@Value("${yongyoucloud.enterpriseUrl}") String yongyouCloudServerUrl) {
        //URL_ENTERPRISE_QUERY_DETAIL = yongyouCloudServerUrl + "/enterpriseSearch/queryDetail";
        //URL_ENTERPRISE_QUERY_FULL =  yongyouCloudServerUrl +  "/enterpriseSearch/queryFull";
        return new EnterpriseServer();
    }

    public String getQueryFull(){
        return yongyouCloudServerUrl +URL_ENTERPRISE_QUERY_FULL;
    }

    public String getQueryDetail(){
        return yongyouCloudServerUrl +URL_ENTERPRISE_QUERY_DETAIL;
    }


}
