package com.project.controllers;

import com.project.domain.PhotoForPlace;
import com.project.domain.dto.PhotoForPlaceDto;
import com.project.service.PhotoForPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequestMapping("/photos")
public class PhotoForPlaceController {

    private final PhotoForPlaceService photoForPlaceService;

    @GetMapping("/place/{id}")
    public List<PhotoForPlace> findAll(@PathVariable Long id) {
        return this.photoForPlaceService.findAll(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoForPlace> findById(@PathVariable Long id) {
        return this.photoForPlaceService.findById(id)
                .map(photoForPlace -> ResponseEntity.ok().body(photoForPlace))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/add/place/{id}")
    public ResponseEntity<PhotoForPlace> add(@PathVariable Long id, @RequestBody PhotoForPlaceDto photoForPlaceDto) {
        return this.photoForPlaceService.add(id, photoForPlaceDto)
                .map(photoForPlace -> ResponseEntity.ok().body(photoForPlace))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<PhotoForPlace> edit(@PathVariable Long id, @RequestBody PhotoForPlaceDto photoForPlaceDto) {
        return this.photoForPlaceService.edit(id, photoForPlaceDto)
                .map(photoForPlace -> ResponseEntity.ok().body(photoForPlace))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.photoForPlaceService.deleteById(id);
        if (this.photoForPlaceService.findById(id).isEmpty())
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
