package com.example.rohan.securelibrary;

import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	private String phno;
	private String name;
	private String password;

	public Customer() {
	}

	public Customer(String name, String phno, String password) {

		this.phno = phno;
		this.name = name;
		this.password = password;

	}

	public String toString() {
		return "Self [phoneno=" + phno + ", name=" + name + ", password="
				+ password + "]";
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

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneno(String phno) {
		this.phno = phno;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
