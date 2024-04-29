package dev.codescreen.controller;
import dev.codescreen.Entity.Ping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/ping")
public class PingController {

    @GetMapping
    public ResponseEntity<Ping> ping() {
        // Get current server time
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ISO_DATE_TIME);

        // Create and return the response
        Ping pingResponse = new Ping(formattedTime,"Ping successful");
        return new ResponseEntity<>(pingResponse, HttpStatus.OK);
    }
}