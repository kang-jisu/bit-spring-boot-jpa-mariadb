package com.jisu.api.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class SecurityRuntimeException extends RuntimeException{
    private static final long SerialVersionUID = 1L;

    private final String msg;
    private final HttpStatus httpStatus;
}