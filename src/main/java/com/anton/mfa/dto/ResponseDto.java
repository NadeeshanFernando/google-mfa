package com.anton.mfa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @auther nadeeshan_fdz
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private int status;
    private Object data;
    private String message;
}
