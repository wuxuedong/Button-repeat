package com.button.demo1.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient getRedisson(){
        Config config = new Config();
        //单机模式  依次设置redis地址和密码
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("E6J6eO50FBuPops0");
        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }
}
