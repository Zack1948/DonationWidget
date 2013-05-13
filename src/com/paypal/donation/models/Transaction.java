package com.paypal.donation.models;

// Generated Dec 9, 2011 10:53:44 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Transaction generated by hbm2java
 */
public class Transaction implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String widgetexternalid;
	private String payerid;
	private String emailid;
	private String paypaltransid;
	private String transresponse;
	private String status;
	private Double amount;
	private String transactiontype;
	private Date createdDt;
	private Date updatedDt;

	public Transaction() {
	}

	public Transaction(String widgetid, Date createdDt, Date updatedDt) {
		this.widgetexternalid = widgetid;
		this.createdDt = createdDt;
		this.updatedDt = updatedDt;
	}

	public Transaction(String widgetid, String payerid, String emailid,
			String paypaltransid, String transresponse, String status,
			Double amount, Date createdDt, Date updatedDt) {
		this.widgetexternalid = widgetid;
		this.payerid = payerid;
		this.emailid = emailid;
		this.paypaltransid = paypaltransid;
		this.transresponse = transresponse;
		this.status = status;
		this.amount = amount;
		this.createdDt = createdDt;
		this.updatedDt = updatedDt;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWidgetexternalid() {
		return widgetexternalid;
	}

	public void setWidgetexternalid(String widgetexternalid) {
		this.widgetexternalid = widgetexternalid;
	}

	public String getPayerid() {
		return this.payerid;
	}

	public void setPayerid(String payerid) {
		this.payerid = payerid;
	}

	public String getEmailid() {
		return this.emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPaypaltransid() {
		return this.paypaltransid;
	}

	public void setPaypaltransid(String paypaltransid) {
		this.paypaltransid = paypaltransid;
	}

	public String getTransresponse() {
		return this.transresponse;
	}

	public void setTransresponse(String transresponse) {
		this.transresponse = transresponse;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public String getTransactiontype() {
		return transactiontype;
	}

	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}

}
