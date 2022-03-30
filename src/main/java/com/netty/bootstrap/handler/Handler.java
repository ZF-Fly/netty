package com.netty.bootstrap.handler;

import io.netty.channel.Channel;

public interface Handler {
    void close(Channel channel);
}
