package service;

import dao.PaymentDAO;
import java.sql.Connection;
import java.util.List;
import model.Payment;

public class PaymentService{
	public PaymentDAO paymentDAO;

	public PaymentService(PaymentDAO paymentDAO){
		this.paymentDAO=paymentDAO;
	}

	//create new user
	public Payment createPayment(Long userId,Long bookingId,Double amount,Connection conn){
	//	Double price = roomBooking.findById(bookingId).getPrice();
	//	starttime
	//	endtime
	//	difference
	//	amount=price*difference
		Payment newPayment = new Payment(userId,bookingId,amount,true);
		paymentDAO.save(newPayment,conn);
		return newPayment;
	}

	//Find all payments
	public List<Payment> getAllPayments(Connection conn){
		return paymentDAO.findAll(conn);
	}

	//Find by id
	public Payment getById(Long id,Connection conn){
		Payment payment= paymentDAO.findById(id,conn);
		
		if(payment == null){
			throw new RuntimeException("Payment not found");
		}
		return payment;
	}

	//Update payment
	public Payment updatePayment(Payment payment,Connection conn){
		getById(payment.getId(),conn);
		return paymentDAO.updatePayment(payment,conn);
	}

	//Update status
	public Payment updateStatus(Long id,Boolean status,Connection conn){
		getById(id,conn);
		return paymentDAO.updatePaymentStatus(id,status,conn);
	}

	//Delete payment
	public Boolean deletePayment(Long id,Connection conn){
		getById(id,conn);
		Boolean value = paymentDAO.deletePayment(id,conn);
		if(value==true){
			System.out.println("Payment deleted...");
			return value;
		}
		else{
			throw new RuntimeException("Error deleting payment");
		}
	}
}
