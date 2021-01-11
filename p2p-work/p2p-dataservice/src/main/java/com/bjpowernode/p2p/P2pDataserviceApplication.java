package com.bjpowernode.p2p;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootApplication
@EnableDubboConfiguration
//事务注解
@EnableTransactionManagement
@MapperScan(basePackages = "com.bjpowernode.p2p.mapper")
public class P2pDataserviceApplication implements CommandLineRunner {

    @Resource
    private RedisTemplate redisTemplate;
    public static void main(String[] args) throws IOException {
        SpringApplication.run(P2pDataserviceApplication.class, args);
        System.in.read();
    }


    @Override
    public void run(String... args) throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
    }
}
