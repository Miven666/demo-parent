package com.miven.demo.concurrent.rest;

import com.alibaba.fastjson.JSON;
import com.miven.demo.concurrent.rest.vo.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author mingzhi.xie
 * @date 2020/3/19
 * @since 1.0
 */
@Slf4j
@SpringBootApplication
public class ConcurrentRestLauncher {

    public static void main(String[] args) {
        SpringApplication.run(ConcurrentRestLauncher.class, args);
    }

    @Resource
    private RestTemplateBuilder restTemplateBuilder;

    @Bean
    public CommandLineRunner rest() {
        return args -> {
            RestTemplate restTemplate = restTemplateBuilder.build();
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(3);
            executor.setMaxPoolSize(60);
            executor.setQueueCapacity(10);
            executor.setThreadNamePrefix("concurrent-rest-task-");
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
            executor.initialize();

            for (int i = 0; i < 100; i++) {
                int deviceId = new Random().nextInt();
                int finalI = i;
                executor.execute(() -> {
                    String uri = "http://localhost:20095/login/miguTokenLogin/v2?clientId=sssss--s-ss";
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(APPLICATION_JSON);
                    headers.set("sourceId", "203001");
                    headers.set("appType", "6");
                    headers.set("businessType", "BT01");

                    HashMap<String, Object> request = new HashMap<>(3);
                    request.put("miguToken", "nlp66666dfdfhdks");
                    request.put("deviceId", "uuuuuuuu" + deviceId);
                    request.put("timestamp", System.currentTimeMillis());
                    HttpEntity<Object> httpEntity = new HttpEntity<>(request, headers);
                    BaseResponse response = restTemplate.postForEntity(uri, httpEntity, BaseResponse.class).getBody();
                    log.info(JSON.toJSONString(response));

                    log.info(finalI + "-" + Thread.currentThread().getName() + "-" + deviceId);
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                log.info("QueueCapacity === " + executor.getThreadPoolExecutor().getQueue().size());
                log.info(String.valueOf(i));
                Thread.sleep(500);
            }

        };
    }
}
