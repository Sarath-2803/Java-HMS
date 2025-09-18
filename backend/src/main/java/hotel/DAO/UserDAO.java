public class UserDAO{
	public User save(User user,Connection conn){
		String query = "INSERT INTO user (username,email,phone,loyalty) VALUES(?,?,?,?)";

		try(PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1,user.getUsername());
			stmt.setString(2,user.getEmail());
			stmt.setString(3,user.getPhone());
			stmt.setDouble(4,user.getLoyalty());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
             			try (ResultSet rs = stmt.getGeneratedKeys()) {
                    			if (rs.next()) {
                        			user.setId(rs.getInt(1)); // auto-generated ID
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
		String query = "SELECT * FROM user";
		List<User> users = new ArrayList<>();

		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query)){

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
		                user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setLoyalty(rs.getDouble("loyalty"));
				users.add(user);
            		}
	    	} catch (SQLException e) {
            		System.out.println("Error fetching users: " + e.getMessage());
        	}

        	return users;
		}
	}

	public User findById(long id,Connection conn){
		String query = "SELECT * FROM user WHERE id=?";

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
		}catch(SQLException e){
			System.out.println("Error fetching user : " + e.getMessage());
		}

		throw new RuntimeException("Error fetching user...");
	}

	public User update(User user,long id,Connection conn){
		String query = "UPDATE user SET username = ?, email = ?, phone = ?, loyalty = ? WHERE id=?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setString(1,user.getUsername());
			stmt.setString(2,user.getEmail());
			stmt.setString(3,user.getPhone());
			stmt.setLoyalty(4,user.getLoyalty());

			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected>0){
				System.out.println("User updated...");
				return user;
			}
		}catch(SQLException e){
			System.out.println("Error updating user : " + e.getMessage());
		}

		throw new RuntimeException("Error updating user...");
	}

	public void delete(long id,Connection conn){
		String query = "DELETE FROM user WHERE id = ?";

		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setLong(1,id);

			int rowsAffected = stmt.executeUpdate();

			if(rowsAffected>0){
				System.out.println("User deleted...");
				return;
			}
			else{
				System.out.println("Error deleting user");
				return;
			}
		}
		catch(SQLException e){
			System.out.println("Error deleting user : " + e.getMessage());
		}
		throw new RuntimeException("Error deleting user...");

	}
}
