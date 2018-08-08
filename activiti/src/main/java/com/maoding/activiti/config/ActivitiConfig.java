package com.maoding.activiti.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.rest.common.application.ContentTypeResolver;
import org.activiti.rest.common.application.DefaultContentTypeResolver;
import org.activiti.rest.service.api.RestResponseFactory;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
@Configuration
@ComponentScan("org.activiti.rest.editor,org.activiti.rest.service")
public class ActivitiConfig {


    @Autowired
    @Qualifier("druidDataSource")
    private DataSource druidDataSource;

    @Autowired
    @Qualifier("transactionManager")
    private DataSourceTransactionManager transactionManager;

    @Bean
    ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    @Bean
    IdGenerator getUuidGenerator(){
        return new StrongUuidGenerator();
    }

    @Bean
    @Qualifier("processEngine")
    ProcessEngineFactoryBean getProcessEngine(){
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration());
        return processEngineFactoryBean;
    }

    @Bean
    public ProcessEngineConfigurationImpl springProcessEngineConfiguration() {
       // return this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
        SpringProcessEngineConfiguration processEngineConfiguration =  new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(druidDataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);
        processEngineConfiguration.setDatabaseSchemaUpdate("false");
        processEngineConfiguration.setJobExecutorActivate(true);
        processEngineConfiguration.setHistory("full");
        processEngineConfiguration.setProcessDefinitionCacheLimit(10);
        processEngineConfiguration.setDbIdentityUsed(false);
        return processEngineConfiguration;
    }

    @Bean
    HistoryService getHistoryService(){
        return getProcessEngine().getProcessEngineConfiguration().getHistoryService();
    }

    @Bean
    RepositoryService getRepositoryService(){
        return getProcessEngine().getProcessEngineConfiguration().getRepositoryService();
    }



    @Bean
    RuntimeService getRuntimeService(){
        return getProcessEngine().getProcessEngineConfiguration().getRuntimeService();
    }

    @Bean
    FormService getFormService(){
        return getProcessEngine().getProcessEngineConfiguration().getFormService();
    }

    @Bean
    IdentityService getIdentityService(){
        return getProcessEngine().getProcessEngineConfiguration().getIdentityService();
    }

    @Bean
    TaskService getTaskService(){
        return getProcessEngine().getProcessEngineConfiguration().getTaskService();
    }

    @Bean
    ManagementService getManagementService(){
        return getProcessEngine().getProcessEngineConfiguration().getManagementService();
    }

    @Bean
    RestResponseFactory getRestResponseFactory(){
        return new RestResponseFactory();
    }

    @Bean
    ContentTypeResolver getContentTypeResolver(){
        return new DefaultContentTypeResolver();
    }
}


