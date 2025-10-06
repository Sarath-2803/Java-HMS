package controller;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import service.UserService;
import model.User;
import util.Utils;
import dto.ResponseDTO;
import util.ResponseUtils;
import util.HelperUtils;


public class UserController{
	private final UserService userService;
//	private final Connection conn;

	public UserController(UserService userService){
		this.userService = userService;
//		this.conn = conn;
	}

	/*==================================================*/

	//Register routes
	public void registerRoutes(HttpServer server){
		server.createContext("/user",this::handleUserPath);
		server.createContext("/user/",this::handleUserIdPath);
	}

	/*====================================================*/

	//Handle /user
	public void handleUserPath(HttpExchange exchange) throws IOException{
		String method = exchange.getRequestMethod();

		switch(method.toUpperCase()){
			case "GET" : displayAllUsers(exchange);
				     break;
			case "POST" : createUser(exchange);
				      break;
			case "PUT" : updateUser(exchange);
				     break;

		}
	}

	//Handle /user/
	public void handleUserIdPath(HttpExchange exchange) throws IOException{
		String method = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath(); 
								  
    		String[] parts = path.split("/");
    		Long id = Long.parseLong(parts[2]);

		if(parts.length == 3 && method.toUpperCase().equals("GET")){
			displayUserById(exchange,id);
		}else if(parts.length == 3 && method.toUpperCase().equals("DELETE")){
			deleteUserById(exchange,parts);
		}
		else if(parts.length == 4 && method.toUpperCase().equals("PUT")){
			handleUserLoyalty(exchange,parts);
		}else if(parts.length == 2 && method.toUpperCase().equals("GET")){
			displayAllUsers(exchange);
		}
	}

	public void handleUserLoyalty(HttpExchange exchange,String[] parts) throws IOException{
		Long id = Long.parseLong(parts[2]);

    		String query = exchange.getRequestURI().getQuery();  
    		Map<String, String> params = HelperUtils.parseQuery(query);
    		if (!params.containsKey("loyalty")) {
        		ResponseDTO response = new ResponseDTO(400, "Missing loyalty parameter");
        		ResponseUtils.sendResponse(exchange, response);
        		return;
    		}

		Double loyalty = Double.parseDouble(params.get("loyalty"));

		try(Connection conn = Utils.getConnection()){
			User user = userService.updateLoyalty(id,loyalty,conn);
			ResponseDTO response = new ResponseDTO(200,user);
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("User loyalty updated");
		} catch(SQLException e){
    			System.out.println("error :" + e.getMessage());
    			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
		} catch(Exception e){
    			System.out.println("error :" + e.getMessage());
    			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
		}
	}

	public void deleteUserById(HttpExchange exchange,String[] parts) throws IOException{
		Long id = Long.parseLong(parts[2]);

		try(Connection conn = Utils.getConnection()){
			userService.deleteUser(id,conn);
			ResponseDTO response = new ResponseDTO(204,-1);
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("User deleted successfully...");
		} catch(SQLException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error deleting user : " + e.getMessage());
		} catch(RuntimeException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error deleting user : " + e.getMessage());
		}
	}


	public void displayUserById(HttpExchange exchange,Long id) throws IOException{
			System.out.println("Received request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());

			try(Connection conn = Utils.getConnection()){
				User user = userService.getById(id,conn);
				ResponseDTO response = new ResponseDTO(200,user);
				ResponseUtils.sendResponse(exchange,response);
			} catch(SQLException e){
				System.out.println("error :" + e.getMessage());
	    			ResponseDTO response = new ResponseDTO(500,e.getMessage());
				ResponseUtils.sendResponse(exchange,response);
			} catch(RuntimeException e){
				System.out.println("error :" + e.getMessage());
    				ResponseDTO response = new ResponseDTO(500,e.getMessage());
				ResponseUtils.sendResponse(exchange,response);
			}
	}




	//Handle /user
	public void displayAllUsers(HttpExchange exchange) throws IOException{
		System.out.println("Received request: " + exchange.getRequestMethod() + "" + exchange.getRequestURI());

		try(Connection conn = Utils.getConnection()){
			List<User> users = userService.getAllUsers(conn);
			ResponseDTO response = new ResponseDTO(200,users);
			System.out.println("Response : " + response);
			ResponseUtils.sendResponse(exchange,response);
		} catch(SQLException e){
    			System.out.println("error :" + e.getMessage());
    			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
		} catch(Exception e){
    			System.out.println("error :" + e.getMessage());
    			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
		}
	}


	public void createUser(HttpExchange exchange) throws IOException{
		System.out.println("Received request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());

    		InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
   	 	BufferedReader br = new BufferedReader(isr);
		StringBuilder buf = new StringBuilder();
    		String line;
    		while ((line = br.readLine()) != null) {
        		buf.append(line);
    		}
    		br.close();
    		String requestBody = buf.toString();
    		System.out.println("Request body: " + requestBody);

		String username = HelperUtils.extractJsonValue(requestBody, "username");
		String email = HelperUtils.extractJsonValue(requestBody, "email");
		String phone = HelperUtils.extractJsonValue(requestBody, "phone");

		if(username == null || email == null || phone == null){
			ResponseDTO response = new ResponseDTO(500,"Missing paramters");
    			ResponseUtils.sendResponse(exchange,response);
			return;
		}


		try(Connection conn = Utils.getConnection()){
			User user = userService.createUser(username,email,phone,conn);
			ResponseDTO response = new ResponseDTO(201,user);
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("User created successfully: " + user.getUsername());
		} catch(SQLException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error creating user: " + e.getMessage());
		} catch(RuntimeException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error creating user: " + e.getMessage());
		}
	}

	
	public void updateUser(HttpExchange exchange) throws IOException{
		System.out.println("Received request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());
		
		InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(),"utf-8");
		BufferedReader br = new BufferedReader(isr);
		StringBuilder buf = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			buf.append(line);
		}
		br.close();
		String requestBody = buf.toString();
		System.out.println("Request body: " + requestBody);

		String idstr = HelperUtils.extractJsonValue(requestBody,"id");
		String username = HelperUtils.extractJsonValue(requestBody,"username");
		String email = HelperUtils.extractJsonValue(requestBody,"email");
		String phone = HelperUtils.extractJsonValue(requestBody,"phone");
		String loyaltystr = HelperUtils.extractJsonValue(requestBody,"loyalty");	

		Long id = Long.parseLong(idstr);
		Double loyalty = Double.parseDouble(loyaltystr);

		if(id == null || username == null || email == null || phone == null || loyalty == null){
			ResponseDTO response = new ResponseDTO(500,"Missing parameters");
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("params : " + idstr + " " + username + " " + email + " " + phone + " " + loyaltystr);
			System.out.println("Error updating user : Missing parameter...");
			return;
		}
	

		User user = new User(id,username,email,phone,loyalty);

		try(Connection conn = Utils.getConnection()){
			User updatedUser = userService.updateUser(user,conn);
			ResponseDTO response = new ResponseDTO(200,updatedUser);
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("User updated successfully...");
		} catch(SQLException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error updating user : " + e.getMessage());
		} catch(RuntimeException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error updating user : " + e.getMessage());
		}
	}

}
