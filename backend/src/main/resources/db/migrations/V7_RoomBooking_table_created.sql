CREATE TABLE IF NOT EXISTS roombookings ( 
    id BIGSERIAL PRIMARY KEY, 
    userId BIGINT NOT NULL, 
    roomId BIGINT NOT NULL, 
    checkIn TIMESTAMP NOT NULL, 
    checkOut TIMESTAMP NOT NULL, 
    totalPrice DOUBLE PRECISION NOT NULL, 
    FOREIGN KEY (userId) REFERENCES users(id), 
    FOREIGN KEY (roomId) REFERENCES rooms(id) );
