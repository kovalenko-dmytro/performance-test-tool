package com.gmail.apachdima.asfosis.common.constant.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonConstant {

    MESSAGE_SOURCE_PATH("classpath:messages/messages"),
    AUTH_HEADER("Authorization"),
    BASIC_AUTH_HEADER_PREFIX("Basic "),
    BEARER_AUTH_HEADER_PREFIX("Bearer "),
    COLON(":"),
    DOT("."),
    EQUAL("="),
    COMMA(","),
    SPACE(" "),
    EMPTY("");

    private final String value;
}
