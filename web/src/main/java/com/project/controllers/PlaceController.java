package com.project.controllers;

import com.project.domain.Place;
import com.project.domain.dto.PlaceDto;
import com.project.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public List<Place> findAll() {
        return this.placeService.findAll();
    }

    @GetMapping("/filter/{word}")
    public List<Place> findAllByNameContaining(@PathVariable String word) {
        return this.placeService.findAllByNameContaining(word);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> findById(@PathVariable Long id) {
        return this.placeService.findById(id)
                .map(place -> ResponseEntity.ok().body(place))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Place> add(@RequestBody PlaceDto placeDto) {
        return this.placeService.add(placeDto)
                .map(place -> ResponseEntity.ok().body(place))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Place> edit(@PathVariable Long id, @RequestBody PlaceDto placeDto) {
        return this.placeService.edit(id, placeDto)
                .map(place -> ResponseEntity.ok().body(place))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.placeService.deleteById(id);
        if (this.placeService.findById(id).isEmpty())
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
