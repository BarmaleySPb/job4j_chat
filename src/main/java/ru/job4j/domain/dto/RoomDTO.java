package ru.job4j.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoomDTO {
    @NotNull(message = "Id must be non null")
    private long id;
    @NotBlank(message = "Name of room mustn't be empty")
    private String name;
    @Min(value = 1, message = "creatorId must be more than 0")
    private long creatorId;
}
