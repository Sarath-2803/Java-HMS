package com.hotel.hotelmanagement.service;

import com.hotel.hotelmanagement.model.Room;
import com.hotel.hotelmanagement.repository.RoomRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // get all rooms
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    // get room by id
    public Room getById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    // create new room
    public Room saveRoom(Room room) {
        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new RuntimeException("Room with this number already exists");
        }
        return roomRepository.save(room);
    }

    // update availability
    @Transactional
    public Room updateAvailability(Long id, Boolean available
