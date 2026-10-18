package com.digitalfix.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BindingRequest(
        @NotBlank @Pattern(regexp = "[a-zA-Z0-9._-]{1,120}") String queue,
        @NotBlank @Pattern(regexp = "[a-zA-Z0-9._-]{1,120}") String exchange,
        @NotBlank String routingKey
) {
}
