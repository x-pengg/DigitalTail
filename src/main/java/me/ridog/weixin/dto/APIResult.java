package me.ridog.weixin.dto;

import com.google.gson.Gson;
import me.ridog.weixin.config.ErrorCode;

/**
 * Created by chan on 2016/11/4.
 */
public class APIResult {

    private Integer status;

    private Integer errCode;

    private String errMsg;

    private Object data;

    public APIResult(){}

    public static APIResult newRs(){
        return new APIResult();
    }

    public APIResult success(){
        this.status = ErrorCode.EXECUTE_SUCCESS;
        return this;
    }

    public APIResult fail(){
        this.status = ErrorCode.EXECUTE_FAIL;
        return this;
    }

    public APIResult errCode(){
        this.errCode = ErrorCode.PARAM_ERROR;
        return this;
    }

    public APIResult errCode(Integer errCode){
        this.errCode = errCode;
        return this;
    }

    public APIResult errMsg(String errMsg){
        this.errMsg = errMsg;
        return this;
    }

    public APIResult data(Object data){
        this.data = data;
        return this;
    }

    public String build(){
        return new Gson().toJson(this);
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public Object getData() {
        return data;
    }

}
