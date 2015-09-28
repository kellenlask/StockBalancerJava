package com.lask.stockbalancerjava.FinancialObjects;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Kellen on 9/27/2015.
 */
public class Portfolio {
	//--------------------------------------------------------
	//
	//		Fields
	//
	//--------------------------------------------------------
	protected ArrayList<Stock> stocks;
	protected double totalValue;

	//--------------------------------------------------------
	//
	//		Constructors
	//
	//--------------------------------------------------------
	public Portfolio(ArrayList<Stock> stocks) {
		this.stocks = stocks;
		calcTotalValue();

	} //End public Portfolio(ArrayList<Stock>)

	//--------------------------------------------------------
	//
	//		Accessors
	//
	//--------------------------------------------------------
	public ArrayList<Stock> getStocks() {
		return stocks;
	} //End public ArrayList<Stock> getStocks()

	public double getTotalValue() {
		return totalValue;
	} //End public double getTotalValue()

	//--------------------------------------------------------
	//
	//		Mutators
	//
	//--------------------------------------------------------
	public double calcTotalValue() {
		totalValue = 0;

		for(Stock stock : stocks) {
			totalValue += stock.getTotalValue();

		} //End for

		return totalValue;

	}//End public double calcTotalValue()

	public void updateStockValues(Map<String, Double> currentValues) {
		for(Stock stock : stocks) {
			//Update the stock's value (and the portfolio's total value)
			totalValue += stock.setValue(currentValues.get(stock.getTicker()));

		} //End for
	} //End public void updateStockValues(Map<String, Double>)

	public String[][] balance() {
		//Initialize the output array
		String[][] summary = new String[stocks.size()][3];

		//For each stock, adjust the value & quantity
		for(int i = 0; i < stocks.size(); i++) {
			//Grab the stock at i & throw its ticker into the output array
			Stock stock = stocks.get(i);
			summary[i][0] = stock.getTicker();

			//Update the stocks' quantities to match their proportions
			int oldQuantity = stock.getShares();
			int newQuantity = stock.updateShares(totalValue);

			//Get the desired action and put it in the output array
			summary[i][1] = getAction(oldQuantity, newQuantity);

		} //End for loop

		//Update the portfolio's total value
		calcTotalValue();

		for(int i = 0; i < stocks.size(); i++) {
			//Grab the stock at i
			Stock stock = stocks.get(i);

			//Find the new percentage and add it to the array
			summary[i][2] = Math.round(stock.getActualProportion(totalValue) * 100) + "%";

		}

		return summary;

	} //End public String[][] balance()

	public String[][] balance(Map<String, Double> currentValues) {
		updateStockValues(currentValues);

		return balance();

	} //End public String[][] balance(Map<String, Double>)


	public void setStocks(ArrayList<Stock> stocks) {
		this.stocks = stocks;
		calcTotalValue();

	} //End public void setStocks(ArrayList<Stock>)

	//--------------------------------------------------------
	//
	//		Static Methods
	//
	//--------------------------------------------------------
	public static String getAction(int oldQuantity, int newQuantity) {
		int difference = newQuantity - oldQuantity;

		if(difference > 0) {
			return "Buy " + difference + " shares";

		} else if(difference < 0) {
			return "Sell " + Math.abs(difference) + " shares";

		}

		return "Do nothing";

	} //End public static String getAction(int, int)
} //End class
