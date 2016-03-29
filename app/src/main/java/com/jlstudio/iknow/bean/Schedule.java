package com.jlstudio.iknow.bean;

public class Schedule {
	private String one;
	private String three;
	private String five;
	private String seven;
	private String nine;
	public Schedule(String one, String three, String five, String seven,
			String nine) {
		super();
		this.one = one;
		this.three = three;
		this.five = five;
		this.seven = seven;
		this.nine = nine;
	}
	public String getOne() {
		return one;
	}
	public void setOne(String one) {
		this.one = one;
	}
	public String getThree() {
		return three;
	}
	public void setThree(String three) {
		this.three = three;
	}
	public String getFive() {
		return five;
	}
	public void setFive(String five) {
		this.five = five;
	}
	public String getSeven() {
		return seven;
	}
	public void setSeven(String seven) {
		this.seven = seven;
	}
	public String getNine() {
		return nine;
	}
	public void setNine(String nine) {
		this.nine = nine;
	}
	public String toString(){
		return one+" "+three+" "+five+" "+seven+" "+nine;
	}
	
}
