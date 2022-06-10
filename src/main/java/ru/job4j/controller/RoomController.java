package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.domain.dto.MessageDTO;
import ru.job4j.domain.dto.RoomDTO;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;
    private final PersonService personService;

    public RoomController(final RoomService roomService, final PersonService personService) {
        this.roomService = roomService;
        this.personService = personService;
    }

    @GetMapping("/")
    public Iterable<Room> findAll() {
        return this.roomService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable long id) {
        var room = this.roomService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room with id: " + id + " not found")
        );
        return new ResponseEntity<Room>(room, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        checkNull(room);
        return new ResponseEntity<Room>(
                this.roomService.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        checkNull(room);
        this.roomService.save(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Room room = new Room();
        room.setId(id);
        this.roomService.delete(room);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Room> patch(@RequestBody RoomDTO roomDTO) {
        if (roomDTO.getId() < 1 || roomDTO.getName() == null || roomDTO.getCreatorId() < 1) {
            throw new NullPointerException("ID, name room and creatorId mustn't be empty");
        }
        Person creator = personService.findById(roomDTO.getCreatorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room creator name with id: " + roomDTO.getCreatorId() + " not found. Please check creatorId."));
        Room room = Room.of(roomDTO.getId(), roomDTO.getName(), creator);
        return new ResponseEntity<>(roomService.save(room), HttpStatus.OK);
    }

    private void checkNull(Room room) {
        if (room.getName() == null) {
            throw new NullPointerException("Name of room mustn't be empty");
        }
    }
}
