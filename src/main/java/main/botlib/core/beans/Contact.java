/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Fouad Almalki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package main.botlib.core.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object represents a phone contact.
 */
public class Contact
{
	/**
	 * Contact's phone number.
	 */
	@JsonProperty("phone_number")	
	private String phoneNumber;
	
	/**
	 * Contact's first name.
	 */
	@JsonProperty("first_name")
	private String firstName;
	
	/**
	 * Optional. Contact's last name.
	 */
	@JsonProperty("last_name")
	private String lastName;
	
	/**
	 * Optional. Contact's user identifier in Telegram.
	 */
	@JsonProperty("user_id")
	private Integer userId;
	
	public Contact(){}
	
	public Contact(String phoneNumber, String firstName, String lastName, Integer userId)
	{
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
	}
	
	public String getPhoneNumber(){return phoneNumber;}
	public String getFirstName(){return firstName;}
	public String getLastName(){return lastName;}
	public Integer getUserId(){return userId;}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		
		Contact contact = (Contact) o;
		
		if(phoneNumber != null ? !phoneNumber.equals(contact.phoneNumber) : contact.phoneNumber != null) return false;
		if(firstName != null ? !firstName.equals(contact.firstName) : contact.firstName != null) return false;
		if(lastName != null ? !lastName.equals(contact.lastName) : contact.lastName != null) return false;
		return userId != null ? userId.equals(contact.userId) : contact.userId == null;
		
	}
	
	@Override
	public int hashCode()
	{
		int result = phoneNumber != null ? phoneNumber.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString()
	{
		return "Contact{" +
				"phoneNumber='" + phoneNumber + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", userId=" + userId +
				'}';
	}
}