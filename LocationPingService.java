package com.safeconnect.service;

import com.safeconnect.entity.LocationPing;
import com.safeconnect.repository.LocationPingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationPingService {

    private final LocationPingRepository repository;

    @Transactional
    public LocationPing savePing(String userRef, Double lat, Double lng,
                                  Float accuracyM, String eventRef, String source) {
        LocationPing ping = LocationPing.builder()
                .userRef(userRef)
                .lat(lat)
                .lng(lng)
                .accuracyM(accuracyM)
                .eventRef(eventRef)
                .source(source != null ? source : "GPS")
                .pingTime(Instant.now())
                .build();

        LocationPing saved = repository.save(ping);
        log.info("Ping saved — user={} lat={} lng={} accuracy={}m", userRef, lat, lng, accuracyM);
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<LocationPing> getLatest(String userRef) {
        return repository.findTopByUserRefOrderByPingTimeDesc(userRef);
    }

    @Transactional(readOnly = true)
    public List<LocationPing> getHistory(String userRef, int lastMinutes) {
        Instant from = Instant.now().minusSeconds(lastMinutes * 60L);
        return repository.findByUserRefAndPingTimeBetweenOrderByPingTimeDesc(
                userRef, from, Instant.now());
    }

    @Transactional(readOnly = true)
    public List<LocationPing> getRecent() {
        return repository.findTop100ByOrderByPingTimeDesc();
    }

    @Transactional(readOnly = true)
    public long getTotalCount() {
        return repository.count();
    }
}
