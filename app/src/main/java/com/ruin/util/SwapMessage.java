package com.ruin.util;


import java.io.Serializable;

/**
 * 数据包实体类
 * @author LHH
 *
 */


public class SwapMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
  
	private String temp = "";
	private String humi = "";
	private String light = "";	
	private String rain="";
	private String date ="";
	private String sign="";
	private int ID=0;
	



	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getHumi() {
		return humi;
	}

	public void setHumi(String humi) {
		this.humi = humi;
	}

	public String getLight() {
		return light;
	}

	public void setLight(String light) {
		this.light = light;
	}

	public String getRain() {
		return rain;
	}

	public void setRain(String rain) {
		this.rain = rain;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
