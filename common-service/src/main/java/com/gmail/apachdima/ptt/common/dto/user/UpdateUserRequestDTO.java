package com.gmail.apachdima.ptt.common.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserRequestDTO (
    String firstName,
    String lastName
) {

}
