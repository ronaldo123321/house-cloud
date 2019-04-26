package com.anytec.userservice.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidConfig {


    @ConfigurationProperties(prefix = "spring.druid")
    @Bean(initMethod = "init",destroyMethod = "close")//平滑开启和关闭
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return dataSource;
    }

    //配置开启慢查询日志
    @Bean
    public Filter statFilter(){
        StatFilter statFilter = new StatFilter();
        //配置超过x时间即视为慢查询sql
        statFilter.setSlowSqlMillis(1000);
        //打印慢日志
        statFilter.setLogSlowSql(true);
        //合并慢日志
        statFilter.setMergeSql(true);
        return statFilter;
    }

    //添加durid监控功能
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }

}
