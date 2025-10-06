package dao;
import model.User;
import java.sql.*;            
import java.util.*;

public class UserDAO{
	public User save(User user,Connection conn){
		String query = "INSERT INTO users (username,email,phone,loyalty) VALUES(?,?,?,?)";

		try(PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1,user.getUsername());
			stmt.setString(2,user.getEmail());
			stmt.setString(3,user.getPhone());
			stmt.setDouble(4,user.getLoyalty());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
             			try (ResultSet rs = stmt.getGeneratedKeys()) {
                    			if (rs.next()) {
                        			user.setId(rs.getLong(1)); 
                    			}
                		}
                		System.out.println("User created!");
                		return user;
            		}
		}
		catch(SQLException e){
			System.out.println("Error creating user: " + e.getMessage());
		}

		throw new RuntimeException("Error creating user...");
	}

	public List<User> findAll(Connection conn){
		String query = "SELECT * FROM users";
		List<User> users = new ArrayList<>();

		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query)){

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("id"));
		                user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setLoyalty(rs.getDouble("loyalty"));
				users.add(user);
            		}
	    	} catch (SQLException e) {
            		System.out.println("Error fetching users: " + e.getMessage());	
        	} catch(RuntimeException e){
			System.out.println("Error fetchinh users: " + e.getMessage());
		}

        	return users;
	}

	public User findById(long id,Connection conn){
		String query = "SELECT * FROM users WHERE id=?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setLong(1,id);

			ResultSet rs = stmt.executeQuery(); 

			if(rs.next()){
				return new User(
					rs.getLong("id"),
					rs.getString("username"),
					rs.getString("email"),
					rs.getString("phone"),
					rs.getDouble("loyalty")
						);
			}
			return null;
		}catch(SQLException e){
			System.out.println("Error fetching user : " + e.getMessage());
			throw new RuntimeException(e);
		}

	}

	public User findByEmail(String email,Connection conn){
		String query = "SELECT * FROM users WHERE email = ?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setString(1,email);

			ResultSet rs = stmt.executeQuery();

			if(rs.next()){
				return new User(
						rs.getLong("id"),
						rs.getString("username"),
						rs.getString("email"),
						rs.getString("phone"),
						rs.getDouble("loyalty")
						);
			}
			return null;
		}catch(SQLException e){
			System.out.println("Error finding user " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public User update(User user,Connection conn){
		String query = "UPDATE users SET username = ?, email = ?, phone = ?, loyalty = ? WHERE id=?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setString(1,user.getUsername());
			stmt.setString(2,user.getEmail());
			stmt.setString(3,user.getPhone());
			stmt.setDouble(4,user.getLoyalty());
			stmt.setLong(5,user.getId());

			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected>0){
				System.out.println("User updated...");
				return user;
			}
			throw new RuntimeException("Error updating user.");
		}catch(SQLException e){
			System.out.println("Error updating user : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public User updateLoyalty(Long id,Double loyalty,Connection conn){
		String query = "UPDATE users SET loyalty = ? where id = ?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setDouble(1,loyalty);
			stmt.setLong(2,id);

			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected>0){
				System.out.println("User loyalty updated.");
				return findById(id,conn);
			}

			throw new RuntimeException("Couldn't update user loyalty.");
		}catch(SQLException e){
			System.out.println("Error updating loyalty : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public Boolean delete(long id,Connection conn){
		String query = "DELETE FROM users WHERE id = ?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setLong(1,id);

			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected>0){
				System.out.println("User deleted...");
				return true;
			}
			else{
				System.out.println("Error deleting user");
				throw new RuntimeException("Error deleting user");
			}
		}
		catch(SQLException e){
			System.out.println("Error deleting user : " + e.getMessage());
			throw new RuntimeException(e);
		}

	}
}
