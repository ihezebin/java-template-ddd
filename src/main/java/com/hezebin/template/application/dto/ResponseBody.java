package com.hezebin.template.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody<T> {
    private int code;
    private T data;
    private String message;

    public static <T> ResponseBody<T> success(T data) {
        return new ResponseBody<>(ResponseBodyCode.OK.getCode(), data, ResponseBodyCode.OK.getMessage());
    }

    public static ResponseBody<?> error(ResponseBodyCode code) {
        return new ResponseBody<>(code.getCode(), null, code.getMessage());
    }

    public static ResponseBody<?> error(ResponseBodyCode code, String message) {
        return new ResponseBody<>(code.getCode(), null, message);
    }
}