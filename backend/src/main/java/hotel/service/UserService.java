package service;
import dao.UserDAO;
import java.sql.Connection;
import java.util.List;
import model.User;

public class UserService{
	public UserDAO userDAO;

	public UserService(UserDAO userDAO){
		this.userDAO=userDAO;
	}

	//create new user
	public User createUser(String username,String email,String phone,Connection conn){
		User existingUser = userDAO.findByEmail(email,conn);

		if(existingUser != null){
			throw new RuntimeException("User exists...");
		}else{
			User newUser = new User(username,email,phone);
			return userDAO.save(newUser,conn);
		}
	}

	//Find all user
	public List<User> getAllUsers(Connection conn){
		return userDAO.findAll(conn);
	}

	//Find by id
	public User getById(Long id,Connection conn){
//		UserDAO userDAO = new UserDAO();
		User user = userDAO.findById(id,conn);

		if(user == null){
			throw new RuntimeException("User not found!");
		}

		return user;
	}

	//Update user
	public User updateUser(User user,Connection conn){
		getById(user.getId(),conn);
		return userDAO.update(user,conn);		
	}

	//Update user loyalty
	public User updateLoyalty(Long id,Double loyalty,Connection conn){
		getById(id,conn);
		return userDAO.updateLoyalty(id,loyalty,conn);
	}

	//Delete user
	public Boolean deleteUser(Long id,Connection conn){
		getById(id,conn);
		Boolean value = userDAO.delete(id,conn);
		if(value==true){
			System.out.println("User deleted...");
			return value;
		}
		else
			throw new RuntimeException("Error deleting user.");
	}
	
}
