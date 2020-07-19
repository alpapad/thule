package uk.co.serin.thule.people.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.people.service.PeopleService;
import uk.co.serin.thule.people.service.PersonNotFoundException;
import uk.co.serin.thule.utils.trace.TracePublicMethods;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@RestController
@RequestMapping("/people/{id}")
@TracePublicMethods
public class PhotographController {
    @NonNull
    private final PeopleService peopleService;

    @GetMapping("/photographs")
    public ResponseEntity<byte[]> getPhotograph(@PathVariable("id") long id) {
        var photograph = peopleService.photograph(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photograph);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public void notFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }
}
