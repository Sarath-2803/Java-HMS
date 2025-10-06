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

import service.PaymentService;
import model.Payment;
import util.Utils;
import dto.ResponseDTO;
import util.ResponseUtils;
import util.HelperUtils;

public class PaymentController{
	public PaymentService paymentService;

	public PaymentController(PaymentService paymentService){
		this.paymentService = paymentService;
	}

	//Register routes
	public void registerRoutes(HttpServer server){
		server.createContext("/payment",this::handlePayment);
		server.createContext("/payment/",this::handlePaymentIdPath);
	}

	//Handle /payment
	public void handlePayment(HttpExchange exchange) throws IOException{
		String method = exchange.getRequestMethod();

		switch(method.toUpperCase()){
			case "GET" : displayPayments(exchange);
				     break;
			case "POST" : createPayment(exchange);
				      break;
			case "PUT" : updatePayment(exchange);
				     break;
		}
	}

	//Handle /payment/
	public void handlePaymentIdPath(HttpExchange exchange)throws IOException{
		String method = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath(); 
								  
    		String[] parts = path.split("/");
    		Long id = Long.parseLong(parts[2]);

		if(parts.length == 3 && method.toUpperCase().equals("GET")){
			displayPaymentById(exchange,id);
			return;
		} else if(parts.length == 3 && method.toUpperCase().equals("DELETE")){
			deletePaymentById(exchange,id);
			return;
		} else if(parts.length == 4 && method.toUpperCase().equals("PUT")){
			updatePaymentStatus(parts,exchange);
			return;
		}

	}

	// /payment-GET
	public void displayPayments(HttpExchange exchange) throws IOException{
		System.out.println("Received request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());

		try(Connection conn = Utils.getConnection()){
			List<Payment> payments = paymentService.getAllPayments(conn);
			ResponseDTO response = new ResponseDTO(200,payments);
			System.out.println("Response : " + response);
			ResponseUtils.sendResponse(exchange,response);
			} catch(SQLException e){
				System.out.println("error : " + e.getMessage());
				ResponseDTO response = new ResponseDTO(500,e.getMessage());
				ResponseUtils.sendResponse(exchange,response);
			} catch(RuntimeException e){
				System.out.println("error : " + e.getMessage());
				ResponseDTO response = new ResponseDTO(500,e.getMessage());
				ResponseUtils.sendResponse(exchange,response);
			}
	}

	// /payment-POST
	public void createPayment(HttpExchange exchange) throws IOException{
		System.out.println("Received request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());

		InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(),"utf-8");
		BufferedReader br = new BufferedReader(isr);
		StringBuilder buf = new StringBuilder();
    		String line;
    		while ((line = br.readLine()) != null) {
        		buf.append(line);
    		}
    		br.close();
		String requestBody = buf.toString();
		System.out.println("Request body : " + requestBody);

		String userIdStr = HelperUtils.extractJsonValue(requestBody,"userId");
		String bookingIdStr = HelperUtils.extractJsonValue(requestBody,"bookingId");
		String amountStr = HelperUtils.extractJsonValue(requestBody,"amount");   //disable later

		Long userId = Long.parseLong(userIdStr);
		Long bookingId = Long.parseLong(bookingIdStr);
		Double amount = Double.parseDouble(amountStr);

		if(userId == null || bookingId == null){
			System.out.println("Error creating payment : Missing paramters...");
			ResponseDTO response = new ResponseDTO(500,"Missing paramters");
			ResponseUtils.sendResponse(exchange,response);
			return;
		}

		try(Connection conn = Utils.getConnection()){
			Payment payment = paymentService.createPayment(userId,bookingId,amount,conn);
			ResponseDTO response = new ResponseDTO(201,payment);
			System.out.println("Payment completed successfully...");
			ResponseUtils.sendResponse(exchange,response);
		} catch(SQLException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error completing payment : " + e.getMessage());
		} catch(RuntimeException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error completing payment : " + e.getMessage());
		}
	}

	// /payment-PUT
	public void updatePayment(HttpExchange exchange) throws IOException{
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
		String userIdStr = HelperUtils.extractJsonValue(requestBody,"userId");
		String bookingIdStr = HelperUtils.extractJsonValue(requestBody,"bookingId");
		String amountStr = HelperUtils.extractJsonValue(requestBody,"amount");	

		Long id = Long.parseLong(idstr);
		Long userId = Long.parseLong(userIdStr);
		Long bookingId = Long.parseLong(bookingIdStr);
		Double amount = Double.parseDouble(amountStr);
		Boolean status = true;

		if(id == null || userId == null || bookingId == null || amount == null){
			ResponseDTO response = new ResponseDTO(500,"Missing parameters");
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error updating payment : Missing parameter...");
			return;
		}
	

		Payment payment = new Payment(id,userId,bookingId,amount,status);

		try(Connection conn = Utils.getConnection()){
			Payment updatedPayment = paymentService.updatePayment(payment,conn);
			ResponseDTO response = new ResponseDTO(200,updatedPayment);
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Payment updated successfully...");
		} catch(SQLException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error updating payment : " + e.getMessage());
		} catch(RuntimeException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error updating payment : " + e.getMessage());
		}
	}

	// /payment/-GET
	public void displayPaymentById(HttpExchange exchange,Long id) throws IOException{
			System.out.println("Received request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());

			try(Connection conn = Utils.getConnection()){
				Payment payment = paymentService.getById(id,conn);
				ResponseDTO response = new ResponseDTO(200,payment);
				ResponseUtils.sendResponse(exchange,response);
				System.out.println("response : " + response);
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

	// /payment/-DELETE
	public void deletePaymentById(HttpExchange exchange,Long id) throws IOException{
		System.out.println("Received request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());
	
		try(Connection conn = Utils.getConnection()){
			paymentService.deletePayment(id,conn);
			ResponseDTO response = new ResponseDTO(204,-1);
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Payment deleted successfully...");
		} catch(SQLException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error deleting payment : " + e.getMessage());
		} catch(RuntimeException e){
			ResponseDTO response = new ResponseDTO(500,e.getMessage());
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Error deleting payment : " + e.getMessage());
		}
	}

	// /payment/status -PUT
	public void updatePaymentStatus(String[] parts,HttpExchange exchange) throws IOException{
		Long id = Long.parseLong(parts[2]);

    		String query = exchange.getRequestURI().getQuery();  
    		Map<String, String> params = HelperUtils.parseQuery(query);
    		if (!params.containsKey("status")) {
        		ResponseDTO response = new ResponseDTO(400, "Missing status parameter");
        		ResponseUtils.sendResponse(exchange, response);
        		return;
    		}

		Boolean status = Boolean.parseBoolean(params.get("status"));

		try(Connection conn = Utils.getConnection()){
			Payment payment = paymentService.updateStatus(id,status,conn);
			ResponseDTO response = new ResponseDTO(200,payment);
			ResponseUtils.sendResponse(exchange,response);
			System.out.println("Payment status updated");
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

}
