package com.nanum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    // 400 에러
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            RuntimeException.class
    })
    public ResponseEntity<Object> BadRequestException(final RuntimeException ex) {
        log.warn("400 error", ex);

        BaseResponse<String> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "잘못된 입력 값입니다.", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    // 500 에러
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("500 error", ex);

        BaseResponse<String> response = new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러입니다.", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
