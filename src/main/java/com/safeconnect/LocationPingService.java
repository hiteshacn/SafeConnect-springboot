package com.safeconnect;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationPingService {

    private final LocationPingRepository repository;

    public LocationPingService(LocationPingRepository repository) {
        this.repository = repository;
    }

    public LocationPing save(LocationPing ping) {
        return repository.save(ping);
    }

    public List<LocationPing> findAll() {
        return repository.findAll();
    }

    public List<LocationPing> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }
}
