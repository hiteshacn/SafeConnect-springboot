# SafeConnect V1 — Location Ping Service

Minimal backend. Captures GPS pings, stores in H2 in-memory DB.
No external dependencies — runs with a single Docker command.

---

## Run Locally

### Option A — Docker Compose (recommended)
```bash
docker-compose up --build
```

### Option B — Maven directly (needs Java 17)
```bash
./mvnw spring-boot:run
```

### Option C — Docker only
```bash
docker build -t safeconnect-v1 .
docker run -p 8080:8080 safeconnect-v1
```

---

## Test the API

### Send a ping
```bash
curl -X POST http://localhost:8080/api/location/ping \
  -H "Content-Type: application/json" \
  -d '{
    "userRef": "9876543210",
    "lat": 25.4358,
    "lng": 81.8463,
    "accuracyM": 12.5,
    "eventRef": "kumbh-2025",
    "source": "GPS"
  }'
```

### Get latest location for a user
```bash
curl http://localhost:8080/api/location/latest?userRef=9876543210
```

### Get location history (last 30 min)
```bash
curl "http://localhost:8080/api/location/history?userRef=9876543210&lastMinutes=30"
```

### Recent pings across all users
```bash
curl http://localhost:8080/api/location/recent
```

### Stats
```bash
curl http://localhost:8080/api/location/stats
```

### Health check
```bash
curl http://localhost:8080/actuator/health
```

---

## H2 Console (local debug)
Open in browser: http://localhost:8080/h2-console

```
JDBC URL:  jdbc:h2:mem:safeconnectdb
Username:  sa
Password:  (leave blank)
```

Run SQL directly:
```sql
SELECT * FROM LOCATION_PINGS ORDER BY PING_TIME DESC;
```

---

## What's Next (gradual additions)

| Step | What to add |
|------|-------------|
| V1.1 | Replace H2 with Cloud SQL PostgreSQL |
| V1.2 | Add OTP login via MSG91, session tokens |
| V1.3 | Add SOS endpoint |
| V1.4 | Add PostGIS, geo-fence checks |
| V1.5 | Wire Cloud Pub/Sub for high-throughput ingestion |
| V2.0 | Read replica, Redis cache, full partitioning |
