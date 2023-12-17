package com.gmail.apachdima.asfosis.common.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserRequestDTO (
    String firstName,
    String lastName
) {

}
