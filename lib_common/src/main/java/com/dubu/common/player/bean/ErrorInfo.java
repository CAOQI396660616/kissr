package com.dubu.common.player.bean;

public class ErrorInfo {
    private ErrorCode mCode;
    private String mMsg;
    private String mExtra;

    public ErrorInfo() {
    }

    public ErrorCode getCode() {
        return this.mCode;
    }

    public void setCode(ErrorCode var1) {
        this.mCode = var1;
    }

    public String getMsg() {
        return this.mMsg;
    }

    public void setMsg(String var1) {
        this.mMsg = var1;
    }

    public String getExtra() {
        return this.mExtra;
    }

    public void setExtra(String var1) {
        this.mExtra = var1;
    }
}
