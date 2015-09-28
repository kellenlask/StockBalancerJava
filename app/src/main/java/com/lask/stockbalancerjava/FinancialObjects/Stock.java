package com.lask.stockbalancerjava.FinancialObjects;

/**
 * Created by Kellen on 9/27/2015.
 */
public class Stock {
	//--------------------------------------------------------
	//
	//		Fields
	//
	//--------------------------------------------------------
	protected String ticker;
	protected int shares;
	protected double value;
	protected double proportion;

	//--------------------------------------------------------
	//
	//		Constructors
	//
	//--------------------------------------------------------
	public Stock(String ticker, int quantity, double proportion, double value) {
		setTicker(ticker);
		setProportion(proportion);
		setShares(quantity);
		setValue(value);

	}

	//--------------------------------------------------------
	//
	//		Accessors
	//
	//--------------------------------------------------------

	public String getTicker() {
		return ticker;
	}

	public int getShares() {
		return shares;
	}

	public double getValue() {
		return value;
	}

	public double getProportion() {
		return proportion;
	}

	public double getTotalValue() {
		return value * shares;
	}

	public double getActualProportion(double totalPortfolioValue) {
		return getTotalValue() / totalPortfolioValue;

	}

	//--------------------------------------------------------
	//
	//		Mutators
	//
	//--------------------------------------------------------

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	protected void setShares(int shares) {
		this.shares = shares;
	}

	//Returns the change in total value
	protected double setValue(double value) {
		double oldValue = getTotalValue();

		this.value = value;

		return getTotalValue() - oldValue;

	}

	public void setProportion(double proportion) {
		if(proportion > 1) {
			proportion /= 100;
		}

		if(proportion > 1) {
			throw new IllegalArgumentException("Invalid stock percentage of portfolio: " + proportion * 100);
		} else {
			this.proportion = proportion;
		}
	}

	protected int updateShares(double totalPortfolioValue) {
		setShares((int) Math.round((totalPortfolioValue * proportion) / value));

		return getShares();

	}
}
