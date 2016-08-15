package com.example.rohan.securelibrary;

import java.io.Serializable;

public class Shopkeeper implements Serializable {
	private static final long serialVersionUID = 1L;
	private String phno;
	private String name;
	private Double amount;
	private String address;
	private String timestamp;

	public Shopkeeper() {
	}

	public Shopkeeper(String name, String phno, Double amount, String address,
			String timestamp) {

		this.phno = phno;
		this.name = name;
		this.amount = amount;
		this.address = address;
		this.timestamp = timestamp;
	}

	public String toString() {
		return "Self [phoneno=" + phno + ", name=" + name + ", amount="
				+ amount + ",address=" + address + ",timestamp= " + timestamp
				+ "]";
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

	public String getTimestamp() {
		return timestamp;
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

	public void setTimestamp(String time) {
		this.timestamp = time;
	}

}
