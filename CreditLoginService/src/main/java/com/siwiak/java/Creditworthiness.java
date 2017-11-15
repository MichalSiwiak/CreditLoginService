package com.siwiak.java;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class Creditworthiness implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final double INTEREST_RATE = 0.1 / 12;

	private double grossIncome;
	private double maintenanceCosts;
	private double installmenOfloans;
	private int periodOfloan;

	private double netIncome;
	private double maxCredit;
	private double pmt;

	public double getNetIncome() {
		netIncome = 0.8 * (grossIncome - maintenanceCosts - installmenOfloans);
		if (netIncome == 0)
			return 0;
		else
			return netIncome;
	}

	public double getMaxCredit() {

		double dividend = (Math.pow(1 + INTEREST_RATE, periodOfloan) - 1) * getNetIncome();
		double divisor = INTEREST_RATE * Math.pow(1 + INTEREST_RATE, periodOfloan);
		maxCredit = dividend / divisor;
		return Math.round(maxCredit);
	}

	public double getPmt() {

		double dividend = INTEREST_RATE * Math.pow(1 + INTEREST_RATE, periodOfloan) * getMaxCredit();
		double divisor = Math.pow(1 + INTEREST_RATE, periodOfloan) - 1;
		pmt = dividend / divisor;

		return pmt;
	}

	public double getGrossIncome() {
		return grossIncome;
	}

	public void setGrossIncome(double grossIncome) {
		this.grossIncome = grossIncome;
	}

	public double getMaintenanceCosts() {
		return maintenanceCosts;
	}

	public void setMaintenanceCosts(double maintenanceCosts) {
		this.maintenanceCosts = maintenanceCosts;
	}

	public double getInstallmenOfloans() {
		return installmenOfloans;
	}

	public void setInstallmenOfloans(double installmenOfloans) {
		this.installmenOfloans = installmenOfloans;
	}

	public int getPeriodOfloan() {
		return periodOfloan;
	}

	public void setPeriodOfloan(int periodOfloan) {
		this.periodOfloan = periodOfloan;
	}

}