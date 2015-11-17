package com.robo.store.dao;

public class AlipayParams {
	
	private String service;

	private String partner;

	private String input_charset;

	private String sign_type;

	private String sign;

	private String notify_url;

	private String out_trade_no;

	private String subject;

	private String payment_type;

	private String seller_id;

	private String total_fee;

	private String body;

	public void setService(String service) {
		this.service = service;
	}

	public String getService() {
		return this.service;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getPartner() {
		return this.partner;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getInput_charset() {
		return this.input_charset;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign_type() {
		return this.sign_type;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign() {
		return this.sign;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getNotify_url() {
		return this.notify_url;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getOut_trade_no() {
		return this.out_trade_no;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getPayment_type() {
		return this.payment_type;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_id() {
		return this.seller_id;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getTotal_fee() {
		return this.total_fee;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return this.body;
	}

}