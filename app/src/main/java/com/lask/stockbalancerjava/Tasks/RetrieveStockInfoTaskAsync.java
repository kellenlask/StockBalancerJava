package com.lask.stockbalancerjava.Tasks;

import android.os.AsyncTask;

import com.lask.stockbalancerjava.GUIElements.LoadingPage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Kellen on 9/27/2015.
 */
public class RetrieveStockInfoTaskAsync extends AsyncTask<String, Void, HashMap<String, Double>> {
	//--------------------------------------------------------
	//
	//		Fields
	//
	//--------------------------------------------------------
	//The fragment that created an instance of this class
	LoadingPage caller;

	//--------------------------------------------------------
	//
	//		Constructors
	//
	//--------------------------------------------------------
	public RetrieveStockInfoTaskAsync(LoadingPage caller) {
		this.caller = caller;
	}

	//--------------------------------------------------------
	//
	//		Async Methods
	//
	//--------------------------------------------------------

	@Override
	protected void onPreExecute() {
		//Nothing to do here.

	} //End protected void onPreExecute()

	@Override
	protected HashMap<String, Double> doInBackground(String... args) {
		//Get the URL from the input params
		if(args.length > 0) {
			try {
				//Create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				//Make GET request to the given URL
				HttpResponse httpResponse = httpclient.execute(new HttpGet(args[0]));

				//Receive response as inputStream
				InputStream inputStream = httpResponse.getEntity().getContent();

				if(inputStream != null) {
					return parseJsonToMap(convertInputStreamToString(inputStream));

				}

			} catch (IOException e) {	}
		}

		return null;

	} //End protected HashMap<String, Double> doInBackground(String... args)

	@Override
	protected void onPostExecute(HashMap<String, Double> newStockValues) {
		//Send the results back to the UI
		caller.newStockValuesLoaded(newStockValues);

	} //End protected void onPostExecute(HashMap<String, Double>)

	//--------------------------------------------------------
	//
	//		Static / Helper methods
	//
	//--------------------------------------------------------

	//Read each stock's tickers and current values into a HashMap
	private static HashMap<String, Double> parseJsonToMap(String pageContent) {
		HashMap<String, Double> currentValues = new HashMap<>();

		//Cut off the "//" at the beginning of the page
		pageContent = pageContent.substring(2);

		try {
			//Read the page content from the HTTP request into JSON
			JSONArray json = new JSONArray(pageContent);

			//For each JSONObject in JSONArray
			for(int i = 0; i < json.length(); i++) {
				//Grab the JSONObject at the index
				JSONObject obj = json.getJSONObject(i);

				//Grab the JSON Object's "t" value and "l_cur" value
				String ticker = obj.getString("t");
				double currentValue = obj.getDouble("l_cur");

				//Add them to the dictionary
				currentValues.put(ticker, currentValue);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return currentValues;

	} //End private static HashMap<String, Double> parseJsonToMap(String)

	//Converts the HTTP request into a String
	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		//Initialize the output variables
		String line = "";
		StringBuilder result = new StringBuilder("");

		//Read the input stream into a BufferedReader
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

		//Read each line into the output string
		while((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}

		//Close the input stream
		inputStream.close();

		return result.toString();

	} //End private static String convertInputStreamToString(InputStream) throws IOException

} //End public class RetrieveStockInfoTaskAsync extends AsyncTask<String, Void, HashMap<String, Double>>
