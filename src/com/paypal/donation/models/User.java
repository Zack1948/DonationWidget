package com.paypal.donation.models;

// Generated Dec 9, 2011 10:53:44 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String payerId;
	private String emailId;
	private Date createdDt;
	private Date updatedDt;
	private String firstName;
	private String lastName;
	private String correlationId;
	private String version;
	private String build;
	private char grantedPermission;

	public User() {
	}

	public User(String payerid, String emailid, Date createdDt, Date updatedDt, String correlationId, String version, String build, char grantPerm) {
		this.payerId = payerid;
		this.emailId = emailid;
		this.createdDt = createdDt;
		this.updatedDt = updatedDt;
		this.correlationId = correlationId;
		this.build = build;
		this.version = version;
		this.grantedPermission = grantPerm;
	}

	public User(String payerid, String emailid, Date createdDt, Date updatedDt,
			String firstname, String lastname, String correlationId, String version, String build, char grantPerm) {
		this.payerId = payerid;
		this.emailId = emailid;
		this.createdDt = createdDt;
		this.updatedDt = updatedDt;
		this.firstName = firstname;
		this.lastName = lastname;
		this.correlationId = correlationId;
		this.build = build;
		this.version = version;
		this.grantedPermission = grantPerm;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayerId() {
		return this.payerId;
	}

	public void setPayerId(String payerid) {
		this.payerId = payerid;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailid) {
		this.emailId = emailid;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstname) {
		this.firstName = firstname;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastname) {
		this.lastName = lastname;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public char getGrantedPermission() {
		return grantedPermission;
	}

	public void setGrantedPermission(char grantedPermission) {
		this.grantedPermission = grantedPermission;
	}

}
