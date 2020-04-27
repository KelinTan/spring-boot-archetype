// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Kelin Tan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String account;
    @NotBlank
    private String password;
}
