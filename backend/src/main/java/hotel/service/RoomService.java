package service;

import java.sql.Connection;

import model.Room;
import java.util.List;
import dao.RoomDAO;

public class RoomService {
    private final RoomDAO roomDao;

    public RoomService(RoomDAO roomDao) {
        this.roomDao = roomDao;
    }

    public Room createRoom(Room room,Connection conn) {
        return roomDao.create(room,conn);
    }

    public List<Room> getAllRooms(Connection conn) {
        return roomDao.findAll(conn);
    }

    public Room getRoom(Long id,Connection conn) {
        Room room = roomDao.findById(id,conn);
        if (room == null) throw new RuntimeException("Room not found");
        return room;
    }

    public Room updateRoom(Room room,Connection conn) {
        return roomDao.update(room,conn);
    }

    public void deleteRoom(Long id,Connection conn) {
        roomDao.delete(id,conn);
    }
}
