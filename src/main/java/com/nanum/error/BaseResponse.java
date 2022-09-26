package com.nanum.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "error", "result"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    @JsonInclude(JsonInclude.Include.NON_NULL)  // null인 데이터는 안나오도록 함(요청 실패한 경우)
    private T result;

    // 요청 성공한 경우
    public BaseResponse(int code, T result) {
        this.isSuccess = true;
        this.code = code;
        this.message = "요청에 성공하였습니다.";
        this.result = result;
    }

    // 요청 실패한 경우
    public BaseResponse(int code, String message, String error) {
        this.isSuccess = false;
        this.code = code;
        this.message = message;
        this.error = error;
    }
}
