package com.green.greengramver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements ErrorCode {
    REQUIRED_IMAGE(HttpStatus.BAD_REQUEST, "사진은 필수입니다."),
    FAIL_TO_REG(HttpStatus.BAD_REQUEST, "변경에 실패했습니다."),
    FAIL_TO_DEL(HttpStatus.BAD_REQUEST, "피드 삭제에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
