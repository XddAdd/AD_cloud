package com.add.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * http响应json数据前后端一致约定的json模式
 * 状态码：200，进入ajax的success来使用
 *
 */
@Getter
@Setter
@ToString
public class JsonResponse {
    //业务操作是否成功
    private boolean success;
    //业务操作的消息码，一般来说，出现错误的错误码才有意义
    private String code;
    //业务操作的错误消息
    private String message;
    //业务数据：操作成功时候，给前端ajax的success函数返回的json数据
    private Object data;




}
