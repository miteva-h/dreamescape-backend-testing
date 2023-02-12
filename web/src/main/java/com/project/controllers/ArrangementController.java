package com.project.controllers;

import com.project.domain.Arrangement;
import com.project.domain.dto.ArrangementDto;
import com.project.domain.relations.ArrangementInShoppingCart;
import com.project.service.ArrangementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequestMapping("/arrangements")
public class ArrangementController {

    private final ArrangementService arrangementService;

    @GetMapping("/accommodation/{id}")
    public List<LocalDate> findAllDatesForAccommodation(@PathVariable Long id) {
        return this.arrangementService.getAllDatesForAccommodation(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Arrangement> add(@RequestBody ArrangementDto arrangementDto) {
        return this.arrangementService.add(arrangementDto)
                .map(arrangement -> ResponseEntity.ok().body(arrangement))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/user/{username}")
    public List<ArrangementInShoppingCart> findAllArrangementsForUser(@PathVariable String username){
        return this.arrangementService.getAllArrangementsForUser(username);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.arrangementService.deleteById(id);
        if (this.arrangementService.findById(id).isEmpty())
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

}
