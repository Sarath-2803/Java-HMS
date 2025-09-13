SET search_path TO public;


CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL CHECk (type in ('GUEST','NON-GUEST')),
    loyalty INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rooms (
    id BIGSERIAL PRIMARY KEY,
    room_number VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    price VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS amenity (
    id BIGSERIAL PRIMARY KEY,
    type INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS room_booking (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    room_id INTEGER NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,

    CONSTRAINT booking_room_user_fk FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT booking_room_room_fk FOREIGN KEY (room_id)
        REFERENCES rooms(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS amenity_booking(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amenity_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,

    CONSTRAINT booking_amenity_user_fk FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT booking_amenity_amenity_fk FOREIGN KEY (amenity_id)
        REFERENCES amenity(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payment (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT payment_user_fk FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
