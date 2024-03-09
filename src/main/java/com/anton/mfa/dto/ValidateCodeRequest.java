package com.anton.mfa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author by nadeeshan_fdz
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidateCodeRequest {
    String username;
    int code;
}
