package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.dto.MessageDTO;
import ru.job4j.handler.Operation;
import ru.job4j.service.MessageService;
import ru.job4j.service.PersonService;

import javax.validation.Valid;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final PersonService personService;

    public MessageController(final MessageService messageService, final PersonService personService) {
        this.messageService = messageService;
        this.personService = personService;
    }

    @GetMapping("/")
    public ResponseEntity<Iterable<Message>> findAll() {
        return new ResponseEntity<>(messageService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable long id) {
        var message = this.messageService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message with id: " + id + " not found")
        );
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@Valid @RequestBody Message message) {
        checkNull(message);
        return new ResponseEntity<Message>(
                this.messageService.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        checkNull(message);
        this.messageService.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Message message = new Message();
        message.setId(id);
        this.messageService.delete(message);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Message> patch(@Valid @RequestBody MessageDTO messageDTO) {
        if (messageDTO.getId() < 1 || messageDTO.getText() == null || messageDTO.getAuthorId() < 1) {
            throw new NullPointerException("ID, text and authorId mustn't be empty");
        }
        Person author = personService.findById(messageDTO.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Author with id: " + messageDTO.getAuthorId() + " not found. Please check authorId."));
        Message message = Message.of(messageDTO.getId(), messageDTO.getText(), author);
        return new ResponseEntity<>(messageService.save(message), HttpStatus.OK);
    }

    private void checkNull(Message message) {
        if (message.getText() == null) {
            throw new NullPointerException("Message mustn't be empty");
        }
    }
}