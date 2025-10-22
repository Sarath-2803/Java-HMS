CREATE TABLE IF NOT EXISTS rooms (
    id BIGSERIAL PRIMARY KEY,
    roomNumber BIGSERIAL NOT NULL UNIQUE, 
    type VARCHAR(100) NOT NULL,              
    price DECIMAL(10,2) NOT NULL,          
    capacity INTEGER NOT NULL,               
    available BOOLEAN DEFAULT TRUE         
);
