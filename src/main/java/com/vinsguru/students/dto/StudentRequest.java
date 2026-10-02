package com.vinsguru.students.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(
        @NotBlank(message = "name must not be blank") String name,
        @NotNull(message = "age must not be null")
        @Min(value = 1, message = "age must be at least 1")
        @Max(value = 120, message = "age must be at most 120") Integer age,
        @NotBlank(message = "className must not be blank") String className) {
}
