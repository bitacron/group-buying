package com.example.groupbuying.common.exception;

import com.example.groupbuying.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(null);
    }
    /**
     * 自定义异常处理方法
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException exception){
        return Result.build(null, exception.getCode(), exception.getMessage());
    }
}