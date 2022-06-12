package ru.job4j.domain;

import lombok.Getter;
import lombok.Setter;
import ru.job4j.handler.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private long id;
    @NotBlank(message = "Text of message must be not empty")
    private String text;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    public Message() {

    }

    public static Message of(long id, String text, Person author) {
        Message message = new Message();
        message.setId(id);
        message.setText(text);
        message.setAuthor(author);
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{"
                + "id=" + id
                + ", text='" + text + '\''
                + ", author=" + author
                + '}';
    }
}
