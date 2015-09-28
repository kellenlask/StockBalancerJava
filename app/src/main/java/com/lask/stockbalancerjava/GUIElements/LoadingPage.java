package com.lask.stockbalancerjava.GUIElements;

import android.app.Fragment;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lask.stockbalancerjava.FinancialObjects.Portfolio;
import com.lask.stockbalancerjava.FinancialObjects.Stock;
import com.lask.stockbalancerjava.R;
import com.lask.stockbalancerjava.Tasks.RetrieveStockInfoTaskAsync;

import java.util.ArrayList;
import java.util.Map;


public class LoadingPage extends Fragment {
	//--------------------------------------------------------
	//
	//		Fields
	//
	//--------------------------------------------------------

	//Constants
	public static final String GOOGLE_FINANCE_URL = "http://finance.google.com/finance/info?client=ig&q=AAPL,GOOG,ABB,CYBR,GFN,ACAD";

	public static final String[] TICKERS = new String[] {"AAPL", "GOOG", "CYBR", "ABB", "GFN", "ACAD"};
	public static final int[] INITIAL_SHARE_COUNTS = new int[] {50, 200, 150, 900, 0, 0};
	public static final double[] PROPORTIONS = new double[] {.22, .38, 0.0, 0.0, .25, .15};

	//Other fields
	private MainActivity parent;

	//--------------------------------------------------------
	//
	//		Constructors / Initializers
	//
	//--------------------------------------------------------
	public LoadingPage() {	/* Empty Constructor */	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (MainActivity) getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_loading_page, container, false);

	} //End public View onCreateView(LayoutInflater, ViewGroup, Bundle)

	@Override
	public void onStart() {
		super.onStart();

		//Check the internet connection
		if(isNetworked()) {
			//Start the AsyncTask
			RetrieveStockInfoTaskAsync task = new RetrieveStockInfoTaskAsync(this);
			task.execute(GOOGLE_FINANCE_URL);

		} else {
			//Inform the user that their network is not connected.
			TextView uiText = (TextView) parent.findViewById(R.id.uiText);
			uiText.setText("No Network Connection");

		}
	} //End public void onStart()

	//--------------------------------------------------------
	//
	//		Helper Methods
	//
	//--------------------------------------------------------

	//Provides a method for AsyncTask to call upon completion of the HTTP request and JSON stuff
	public void newStockValuesLoaded(Map<String, Double> newStockValues) {
		if(newStockValues != null) {
			//Create ArrayList<Stock>
			ArrayList<Stock> stocks = getStocks(newStockValues);

			//Create & balance a portfolio
			Portfolio portfolio = new Portfolio(stocks);
			String[][] output = portfolio.balance();

			//Send output to next fragment & remove the loading page from the stack
			parent.switchToResultsPage(output);

		} else {
			TextView uiText = (TextView) parent.findViewById(R.id.uiText);
			uiText.setText("An error occurred. Please try again later.");
			uiText.setTextColor(Color.RED);

		} //End else-if block
	} //End public void newStockValuesLoaded(HashMap<String, Double>)

	//Newer versions of API will require asking the user for the permission and handling it in-code
	public boolean isNetworked() {
		ConnectivityManager cm = (ConnectivityManager) parent.getSystemService(parent.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	} //End public boolean isNetworked()

	public static ArrayList<Stock> getStocks(Map<String, Double> newStockValues) {
		ArrayList<Stock> stocks = new ArrayList<>();

		//Populate stocks arraylist
		for(int i = 0; i < TICKERS.length; i++) {
			String ticker = TICKERS[i];
			int quantity = INITIAL_SHARE_COUNTS[i];
			double proportion = PROPORTIONS[i];
			double value = newStockValues.get(TICKERS[i]);

			Stock stock = new Stock(ticker, quantity, proportion, value);
			stocks.add(stock);
		} //End for loop

		return stocks;

	} //End public static ArrayList<Stock> getStocks(Map<String, Double>

} //End class
