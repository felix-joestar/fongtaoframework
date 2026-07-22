package com.fongtaoframework.starter.web.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.result.CommonErrorCode;
import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.core.trace.TraceIdContext;
import com.fongtaoframework.starter.logging.support.SensitiveDataSanitizer;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<R<Void>> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(R.failed(ex.code(), ex.getMessage()));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            HandlerMethodValidationException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<R<Void>> handleValidationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.failed(CommonErrorCode.PARAM_ERROR.code(), firstMessage(ex)));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<R<Void>> handleErrorResponseException(ErrorResponseException ex) {
        int status = ex.getStatusCode().value();
        String message = ex.getBody().getDetail();
        if (StrUtil.isBlank(message)) {
            message = ex.getMessage();
        }
        if (StrUtil.isBlank(message)) {
            message = CommonErrorCode.INTERNAL_ERROR.message();
        }
        return ResponseEntity.status(ex.getStatusCode()).body(R.failed(status, message));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<R<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(R.failed(HttpStatus.NOT_FOUND.value(), "请求路径不存在"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<Void>> handleException(Exception ex) {
        log.error(
                "unexpected_exception traceId={} exception={} message={}",
                TraceIdContext.currentOrCreate(),
                ex.getClass().getName(),
                SensitiveDataSanitizer.maskLine(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.failed(CommonErrorCode.INTERNAL_ERROR));
    }

    private String firstMessage(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException
                && methodArgumentNotValidException.hasErrors()) {
            return methodArgumentNotValidException.getAllErrors().getFirst().getDefaultMessage();
        }
        if (ex instanceof BindException bindException && bindException.hasErrors()) {
            return bindException.getAllErrors().getFirst().getDefaultMessage();
        }
        if (ex instanceof HandlerMethodValidationException handlerMethodValidationException
                && CollUtil.isNotEmpty(handlerMethodValidationException.getAllErrors())) {
            return handlerMethodValidationException.getAllErrors().getFirst().getDefaultMessage();
        }
        return ex.getMessage();
    }
}
