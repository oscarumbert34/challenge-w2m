package com.w2m.superheroe.models.entities.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ErrorApi {
    private int code;
    private String message;
}
