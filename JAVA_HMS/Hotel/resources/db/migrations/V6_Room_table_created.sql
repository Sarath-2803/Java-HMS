CREATE TABLE IF NOT EXISTS rooms (
    id BIGSERIAL PRIMARY KEY,
    roomNumber BIGSERIAL NOT NULL UNIQUE,  -- From 1st
    type VARCHAR(100) NOT NULL,              -- From 1st  
    price DECIMAL(10,2) NOT NULL,           -- From 1st (better for money)
    capacity INTEGER NOT NULL,               -- From 1st
    available BOOLEAN DEFAULT TRUE          -- From 2nd
);