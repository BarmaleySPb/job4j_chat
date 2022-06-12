package ru.job4j.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
public class PersonDTO {
    @NotNull(message = "Id must be non null")
    private int id;
    @NotBlank(message = "Name must be not empty")
    private String name;
    @Email(message = "Email isn't valid")
    @Pattern(regexp = ".+@.+\\..+", message = "Email isn't valid")
    private String email;
    @NotBlank(message = "Password must be not empty")
    private String password;
    @Min(value = 1, message = "roleId must be more than 0")
    private int roleId;
}
