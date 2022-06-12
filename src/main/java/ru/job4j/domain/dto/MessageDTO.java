package ru.job4j.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MessageDTO {
    @NotNull(message = "Id must be non null")
    private long id;
    @NotBlank(message = "Message must be not empty")
    private String text;
    @Min(value = 1, message = "authorId must be more than 0")
    private long authorId;
}
