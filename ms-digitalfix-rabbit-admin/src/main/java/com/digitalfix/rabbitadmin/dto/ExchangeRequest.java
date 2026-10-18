package com.digitalfix.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ExchangeRequest(
        @NotBlank @Pattern(regexp = "[a-zA-Z0-9._-]{1,120}") String name,
        @NotBlank @Pattern(regexp = "(?i)direct|topic|fanout") String type
) {
}
