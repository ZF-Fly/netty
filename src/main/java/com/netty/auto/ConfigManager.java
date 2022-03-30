package com.netty.auto;


import com.netty.bootstrap.channel.http.FromServerService;
import com.netty.bootstrap.channel.protocol.InChatVerifyService;
import com.netty.core.bean.InitNetty;
import com.netty.scheduling.AsyncListener;
import com.netty.support.FromServerServiceImpl;

/**
 * 默认配置工厂
 * Created by MySelf on 2018/12/21.
 */
public class ConfigManager {

    /** Redis的ip地址 */
    public static String RedisIP;

    /** 用户校验伪接口 */
    public static InChatVerifyService inChatVerifyService;

    /** 系统信息枚举服务接口 */
    public static FromServerServiceImpl fromServerService;

    /** InChat项目配置 */
    public static InitNetty initNetty;

    /** 用户监听异步数据伪接口 */
    public static AsyncListener asyncListener;

}
