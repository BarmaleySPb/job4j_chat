package ru.job4j.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private long id;
    private String text;
    private long authorId;
}
