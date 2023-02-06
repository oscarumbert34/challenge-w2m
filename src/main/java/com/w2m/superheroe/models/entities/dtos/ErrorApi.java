package com.w2m.superheroe.models.entities.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorApi {
    private int code;
    private String message;
}
