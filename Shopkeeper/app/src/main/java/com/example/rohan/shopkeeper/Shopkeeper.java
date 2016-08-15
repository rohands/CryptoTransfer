package com.example.rohan.shopkeeper;

import java.io.Serializable;

public class Shopkeeper implements Serializable {
	private static final long serialVersionUID = 1L;
	private String phno;
	private String name;
	private String password;
	private String address;

	public Shopkeeper() {
	}

	public Shopkeeper(String name, String phno, String password, String address) {

		this.phno = phno;
		this.name = name;
		this.password = password;
		this.address = address;

	}

	public String toString() {
		return "Self [phoneno=" + phno + ", name=" + name + ", password="
				+ password +",address="+address+ "]";
	}

	public String getPhoneNumber() {
		return phno;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
	public String getAddress()
	{
		return address;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneno(String phno) {
		this.phno = phno;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}

}
