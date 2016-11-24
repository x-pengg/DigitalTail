package me.ridog.weixin.exception;

/**
 * Created by chan on 2016/11/4.
 */
public final class APIException extends RuntimeException {

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public APIException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
