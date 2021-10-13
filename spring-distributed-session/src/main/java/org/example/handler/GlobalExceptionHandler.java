package org.example.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.dto.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger();

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult serviceException(HttpServletRequest request, ServiceException e) {
        logger.error("ServiceException {}\n", request.getRequestURI(), e);
        return new CommonResult(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult serviceException(HttpServletRequest request, Exception e) {
        return new CommonResult(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage());
    }
}