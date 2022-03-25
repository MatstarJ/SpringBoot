package com.matstar.ex7.controller;

import com.matstar.ex7.dto.NoteDTO;
import com.matstar.ex7.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping(value="")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO){

        log.info("noteDTO: " + noteDTO);

        Long num = noteService.register(noteDTO);

        return new ResponseEntity<>(num, HttpStatus.OK);

    }

    @GetMapping(value="/{num}",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num) {

        log.info("Read num : " + num);

        return new ResponseEntity<>(noteService.get(num),HttpStatus.OK);

    }

    @GetMapping(value="/all", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDTO>> getList(String email) {
        log.info("all, email : " + email);
        return new ResponseEntity<>(noteService.getAllWithWriter(email),HttpStatus.OK);
    }

    @PutMapping(value="/{num}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO) {

        log.info("modify noteDTO : " + noteDTO);
        noteService.modify(noteDTO);

        return new ResponseEntity<>("modified",HttpStatus.OK);
    }
}
