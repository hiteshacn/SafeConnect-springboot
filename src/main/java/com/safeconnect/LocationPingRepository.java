package com.safeconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationPingRepository extends JpaRepository<LocationPing, Long> {
    List<LocationPing> findByUserId(String userId);
}
