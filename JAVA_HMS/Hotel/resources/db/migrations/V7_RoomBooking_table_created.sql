-- Drop and recreate (if you can lose existing data)
DROP TABLE IF EXISTS roombookings;

CREATE TABLE IF NOT EXISTS roombookings (
    id BIGSERIAL PRIMARY KEY,
    userId BIGINT NOT NULL,
    roomId BIGINT NOT NULL,
    checkIn TIMESTAMP NOT NULL,
    checkOut TIMESTAMP NOT NULL,
    totalPrice DOUBLE PRECISION NOT NULL,
    status BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (roomId) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_roombookings_roomId ON roombookings(roomId);
CREATE INDEX IF NOT EXISTS idx_roombookings_dates ON roombookings(checkIn, checkOut);
CREATE INDEX IF NOT EXISTS idx_roombookings_userId ON roombookings(userId);