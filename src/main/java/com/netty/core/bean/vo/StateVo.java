package com.netty.core.bean.vo;


public class StateVo {

    public String token;

    public Boolean state;

    public StateVo() {
    }

    public StateVo(String token, Boolean state) {
        this.token = token;
        this.state = state;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
