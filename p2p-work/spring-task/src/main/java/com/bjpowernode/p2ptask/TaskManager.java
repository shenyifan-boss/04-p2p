package com.bjpowernode.p2ptask;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.IncomeRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
import java.util.Date;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/4
 **/
//让spring容器创建对象
@Component
public class TaskManager {
    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0",check = false)
    private IncomeRecordService incomeRecordService;

//    @Scheduled(cron = "*/5 * * * * ?")
//    public void test(){
//        System.out.println("执行了定时任务"+new Date());
//    }
    //生成收益计划测试
//    @Scheduled(cron = "*/5 * * * * ?")
    public void generateIncomePlan(){
        incomeRecordService.generateIncomePan();
    }

    public void generateIncomeBack(){
        incomeRecordService.generateIncomeBack();
    }

}
