package dao;

import model.Payment;
import java.sql.*;
import java.util.*;

public class PaymentDAO{
	public Payment save(Payment payment,Connection conn){
		String query = "INSERT INTO payment (userId, bookingId, amount, status) VALUES (?, ?, ?, ?)";

		try(PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)){
			stmt.setLong(1,payment.getUserId());
			stmt.setLong(2,payment.getBookingId());
			stmt.setDouble(3,payment.getAmount());
			stmt.setBoolean(4,payment.getStatus());

			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected > 0){
				try(ResultSet rs = stmt.getGeneratedKeys()){
					if(rs.next()){
						payment.setId(rs.getLong(1));
					}
				}
				System.out.println("Payment completed successfully...");
				return payment;
			}
		} catch(SQLException e){
			System.out.println("Error completing payment : " + e.getMessage());
		}

		throw new RuntimeException("Error completing payment...");
	}

	public List<Payment> findAll(Connection conn){
		String query = "SELECT * FROM payment";
		List<Payment> payments = new ArrayList<>();

		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query)){
				while(rs.next()){
					Payment payment = new Payment();
					payment.setId(rs.getLong("id"));
					payment.setUserId(rs.getLong("userId"));
					payment.setBookingId(rs.getLong("bookingId"));
					payment.setAmount(rs.getDouble("amount"));
					payment.setStatus(rs.getBoolean("status"));

					payments.add(payment);
				}
		} catch(SQLException e){
			System.out.println("Error fetching payments : " + e.getMessage());
			throw new RuntimeException(e);
		} 

		return payments;
	}

	public Payment findById(Long id,Connection conn){
		String query = "SELECT * FROM payment WHERE id = ?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setLong(1,id);

			ResultSet rs = stmt.executeQuery();

			if(rs.next()){
				return new Payment(rs.getLong("id"),
						rs.getLong("userId"),
						rs.getLong("bookingId"),
						rs.getDouble("amount"),
						rs.getBoolean("status")
						);
			}
			return null;
		} catch(SQLException e){
			System.out.println("Error fetching payment : " + e.getMessage());
			throw new RuntimeException(e);
		} 
	}

	public Payment updatePayment(Payment payment,Connection conn){
		String query = "UPDATE payment SET userId = ?, bookingId = ?,amount = ?,status = ? WHERE id =?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setLong(1,payment.getUserId());
			stmt.setLong(2,payment.getUserId());
			stmt.setDouble(3,payment.getAmount());
			stmt.setBoolean(4,payment.getStatus());
			stmt.setLong(5,payment.getId());

			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected>0){
				System.out.println("Payment updated");
				return payment;
			}
			throw new RuntimeException("Error updating payment...");
		} catch(SQLException e){
			System.out.println("Error updating payment : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public Payment updatePaymentStatus(Long id,Boolean status,Connection conn){
		String query = "UPDATE payment SET status = ? WHERE id = ?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setBoolean(1,status);
			stmt.setLong(2,id);
			
			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected > 0){
				System.out.println("Payment status updated");
				return findById(id,conn);
			}
			throw new RuntimeException("Error updating payment status...");
		} catch(SQLException e){
			System.out.println("Error updating payment status : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public boolean deletePayment(Long id,Connection conn){
		String query = "DELETE FROM payment WHERE id = ?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setLong(1,id);

			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected > 0){
				System.out.println("Payment deleted successfully...");
				return true;
			}
			throw new RuntimeException("Error deleting payment...");
		} catch(SQLException e){
			System.out.println("Error deleting payment: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
