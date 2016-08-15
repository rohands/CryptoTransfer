package com.example.rohan.shopkeeper;

import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	private String phno;
	private String name;
	private Double amount;
	private String address;
	private String time;


	public Customer() {
	}

	public Customer(String phno, String name, Double amount, String address) {

		this.phno = phno;
		this.name = name;
		this.amount = amount;
		this.address = address;
	

	}

	public String toString() {
		return "Self [phoneno=" + phno + ", name=" + name + ", amount="
				+ amount + ",address=" + address + ",time="+time +"]";
	}

	public String getPhoneNumber() {
		return phno;
	}

	public String getName() {
		return name;
	}

	public Double getAmount() {
		return amount;
	}

	public String getAddress() {
		return address;
	}
	public String getTime()
	{
		return time;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneno(String phno) {
		this.phno = phno;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public void setTime(String time) {
		this.time = time;
	}

}
