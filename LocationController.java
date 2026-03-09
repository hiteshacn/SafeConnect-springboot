package com.safeconnect.controller;

import com.safeconnect.entity.LocationPing;
import com.safeconnect.service.LocationPingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationPingService pingService;

    /**
     * POST /api/location/ping
     *
     * Accepts a GPS ping from the PWA.
     * userRef is a phone number or device ID — no auth yet.
     *
     * Example body:
     * {
     *   "userRef": "9876543210",
     *   "lat": 25.4358,
     *   "lng": 81.8463,
     *   "accuracyM": 12.5,
     *   "eventRef": "kumbh-2025",
     *   "source": "GPS"
     * }
     */
    @PostMapping("/ping")
    public ResponseEntity<LocationPing> receivePing(@Valid @RequestBody PingRequest req) {
        LocationPing saved = pingService.savePing(
                req.getUserRef(), req.getLat(), req.getLng(),
                req.getAccuracyM(), req.getEventRef(), req.getSource()
        );
        return ResponseEntity.ok(saved);
    }

    /**
     * GET /api/location/latest?userRef=9876543210
     * Returns the most recent ping for a user.
     */
    @GetMapping("/latest")
    public ResponseEntity<?> getLatest(@RequestParam String userRef) {
        return pingService.getLatest(userRef)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/location/history?userRef=9876543210&lastMinutes=60
     * Returns pings for a user over the last N minutes.
     */
    @GetMapping("/history")
    public List<LocationPing> getHistory(
            @RequestParam String userRef,
            @RequestParam(defaultValue = "30") int lastMinutes) {
        return pingService.getHistory(userRef, lastMinutes);
    }

    /**
     * GET /api/location/recent
     * Returns last 100 pings across all users — debug/admin use.
     */
    @GetMapping("/recent")
    public List<LocationPing> getRecent() {
        return pingService.getRecent();
    }

    /**
     * GET /api/location/stats
     * Basic stats — total pings captured so far.
     */
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
                "totalPings", pingService.getTotalCount(),
                "status", "running"
        );
    }

    // ── Request DTO ───────────────────────────────────────────────
    @Data
    public static class PingRequest {
        @NotBlank(message = "userRef is required (phone or device ID)")
        private String userRef;

        @NotNull @DecimalMin("-90") @DecimalMax("90")
        private Double lat;

        @NotNull @DecimalMin("-180") @DecimalMax("180")
        private Double lng;

        private Float accuracyM;
        private String eventRef;
        private String source;
    }
}
