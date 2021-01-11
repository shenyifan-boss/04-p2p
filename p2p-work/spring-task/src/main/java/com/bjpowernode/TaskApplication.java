package com.bjpowernode;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.bjpowernode.p2ptask.TaskManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDubboConfiguration
public class TaskApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext cxt = SpringApplication.run(TaskApplication.class, args);
        TaskManager bean = cxt.getBean(TaskManager.class);
        bean.generateIncomeBack();
    }

}
