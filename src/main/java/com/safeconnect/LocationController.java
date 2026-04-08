package com.safeconnect;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationPingService service;

    public LocationController(LocationPingService service) {
        this.service = service;
    }

    @PostMapping("/ping")
    public ResponseEntity<LocationPing> savePing(@RequestBody LocationPing ping) {
        return ResponseEntity.ok(service.save(ping));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LocationPing>> getAllPings() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LocationPing>> getPingsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }
}
