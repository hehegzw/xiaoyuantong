package com.jlstudio.iknow.bean;

import java.io.Serializable;

public class ScoreItem implements Serializable {
	private String name;
	private String cridit;
	private String point;
	private String score;
	public ScoreItem(String name, String cridit, String point, String score) {
		super();
		this.name = name;
		this.cridit = cridit;
		this.point = point;
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCridit() {
		return cridit;
	}
	public void setCridit(String cridit) {
		this.cridit = cridit;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String toString(){
		return name+" "+cridit+" "+point+" "+score;
	}
	
}
