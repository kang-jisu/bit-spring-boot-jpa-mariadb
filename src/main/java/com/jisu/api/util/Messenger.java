package com.jisu.api.util;

import lombok.Builder;
import lombok.Getter;

/*
ResponeEntity(에러 핸들링) 응답용 객체이다.
 */
@Getter
@Builder
public class Messenger {
    private String message;
    private int status;
    private String code;
}
