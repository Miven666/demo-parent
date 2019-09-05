package com.miven;

import com.hccake.simpleredis.EnableSimpleCache;
import com.miven.component.AspectRedisOperationComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *  启动器
 * @author mingzhi.xie
 * @date 2019/9/4
 * @since 1.0
 */
@EnableSimpleCache
@SpringBootApplication
public class CacheLauncher {

    public static void main(String[] args) {
        SpringApplication.run(CacheLauncher.class, args);
    }

    @Bean
    CommandLineRunner redisOperation(AspectRedisOperationComponent component) {
        return args -> {
            component.getFruit(100);
            component.getBook("ISBN 978-7-302-30639-9");

            System.exit(0);
        };
    }
}
