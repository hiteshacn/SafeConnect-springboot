package com.safeconnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "location_pings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationPing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Simple user identifier — phone number or device ID for now
    // (no auth yet — we add that later)
    @Column(name = "user_ref", nullable = false)
    private String userRef;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    // GPS accuracy in metres — null if not available
    @Column(name = "accuracy_m")
    private Float accuracyM;

    // Optional: which event/session this ping belongs to
    @Column(name = "event_ref")
    private String eventRef;

    // Source of location: GPS | NETWORK | PASSIVE
    @Column(nullable = false)
    @Builder.Default
    private String source = "GPS";

    @Column(name = "ping_time", nullable = false)
    private Instant pingTime;

    @PrePersist
    protected void onCreate() {
        if (pingTime == null) pingTime = Instant.now();
    }
}
