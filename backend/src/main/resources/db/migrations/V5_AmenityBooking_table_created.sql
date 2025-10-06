CREATE TABLE IF NOT EXISTS amenity_bookings (
    id SERIAL PRIMARY KEY,
    amenity_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    FOREIGN KEY (amenity_id) REFERENCES amenities(id)
);