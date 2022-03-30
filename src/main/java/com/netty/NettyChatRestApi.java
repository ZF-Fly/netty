package com.netty;

import com.netty.auto.ConfigManager;
import com.netty.auto.InChatServer;
import com.netty.support.FromServerServiceImpl;
import com.netty.support.InChatInitializer;
import com.netty.support.UserAsyncDataListener;
import com.netty.support.VerifyServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;


@SpringBootApplication(scanBasePackages = "com.netty")
public class NettyChatRestApi {
    public static void main(String[] args) {
        ConfigManager.initNetty = new InChatInitializer();
        ConfigManager.fromServerService = FromServerServiceImpl.TYPE2;
        ConfigManager.asyncListener = new UserAsyncDataListener();
        ConfigManager.inChatVerifyService = new VerifyServiceImpl();
        InChatServer.open();
//        SpringApplication.run(NettyChatRestApi.class, args);
    }
}
