package model;

public class User{
	private Long id;
	private String username;
	private String email;
	private String phone;
	private Double loyalty=0.0;


	//Constructor
	public User(){}

	//for existing users
	public User(Long id,String username,String email,String phone,Double loyalty){
		this.id=id;
		this.username=username;
		this.email=email;
		this.phone=phone;
		this.loyalty=loyalty;
	}
	
	//for new users
	public User(String username,String email,String phone){
		this.username=username;
		this.email=email;
		this.phone=phone;
	}


	//Getter
	public Long getId(){	return id;}

	public String getUsername(){	return username;}

	public String getEmail(){	return email;}

	public String getPhone(){	return phone;}

	public Double getLoyalty(){	return loyalty;}


	//Setter
	public void setId(Long id){	this.id=id;}

	public void setUsername(String username){	this.username=username;}

	public void setEmail(String email){	this.email=email;}

	public void setPhone(String phone){	this.phone=phone;}

	public void setLoyalty(Double loyalty){	this.loyalty=loyalty;}
}
