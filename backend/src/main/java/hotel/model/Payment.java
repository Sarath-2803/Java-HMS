package model;

public class Payment{
	private Long id;
	private Long userId;
	private Long bookingId;
	private Double amount;
	private Boolean status=false;

	//Constructor
	public Payment(){};

	public Payment(Long userId,Long bookingId,Double amount,Boolean status){
		this.userId=userId;
		this.bookingId=bookingId;
		this.amount=amount;
		this.status=status;
	}

	public Payment(Long id,Long userId,Long bookingId,Double amount,Boolean status){
		this.id=id;
		this.userId=userId;
		this.bookingId=bookingId;
		this.amount=amount;
		this.status=status;
	}

	//getter
	public Long getId(){
		return id;
	}

	public Long getUserId(){
		return userId;
	}

	public Long getBookingId(){
		return bookingId;
	}

	public Double getAmount(){
		return amount;
	}

	public Boolean getStatus(){
		return status;
	}

	//setter
	public void setId(Long id){
		this.id=id;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public void setBookingId(Long bookingId){
		this.bookingId=bookingId;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public void setStatus(Boolean status){
		this.status=status;
	}
}
