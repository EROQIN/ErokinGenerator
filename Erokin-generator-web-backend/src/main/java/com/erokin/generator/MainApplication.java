package com.erokin.generator;

import com.erokin.generator.vertx.VertxManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 主类（项目启动入口）
 *
 * @author <a href="https://github.com/EROQIN">Erokin</a>
 *   
 */
@SpringBootApplication
@MapperScan("com.erokin.generator.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {

    @Resource
    private VertxManager vertxManager;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @PostConstruct
    public void init() {
        vertxManager.init();
    }

}
