package com.farm.entity;

public class Question3Num {
	private int num1;
	private String signal1;
	private int num2;
	private String signal2;
	private int num3;
	private int result;
	
	
	
	public Question3Num(int num1, String signal1, int num2, String signal2, int num3, int result) {
		super();
		this.num1 = num1;
		this.signal1 = signal1;
		this.num2 = num2;
		this.signal2 = signal2;
		this.num3 = num3;
		this.result = result;
	}
	public int getNum1() {
		return num1;
	}
	public void setNum1(int num1) {
		this.num1 = num1;
	}
	public String getSignal1() {
		return signal1;
	}
	public void setSignal1(String signal1) {
		this.signal1 = signal1;
	}
	public int getNum2() {
		return num2;
	}
	public void setNum2(int num2) {
		this.num2 = num2;
	}
	public String getSignal2() {
		return signal2;
	}
	public void setSignal2(String signal2) {
		this.signal2 = signal2;
	}
	public int getNum3() {
		return num3;
	}
	public void setNum3(int num3) {
		this.num3 = num3;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Question3Num [num1=" + num1 + ", signal1=" + signal1 + ", num2=" + num2 + ", signal2=" + signal2
				+ ", num3=" + num3 + ", result=" + result + "]";
	}
	
	
}
