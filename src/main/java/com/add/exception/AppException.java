package com.add.exception;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AppException extends RuntimeException{

    //自定义错误码
    private String code;

    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AppException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


}
