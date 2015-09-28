package com.lask.stockbalancerjava.GUIElements;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lask.stockbalancerjava.R;

public class MainActivity extends AppCompatActivity {
	//--------------------------------------------------------
	//
	//		Fields
	//
	//--------------------------------------------------------
	protected int fragmentFrame = R.id.fragmentFrame;
	protected FragmentManager manager;

	//--------------------------------------------------------
	//
	//		Entry Point
	//
	//--------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get the activity's fragment manager
		manager = getFragmentManager();

		//Push through the first transaction to the container
		FragmentTransaction transact = manager.beginTransaction();
		transact.add(fragmentFrame, new LoadingPage(), Shard.LOADING_PAGE.toString());
		transact.commit();

	} //End protected void onCreate(Bundle)

	//--------------------------------------------------------
	//
	//		Fragment Management
	//
	//--------------------------------------------------------
	public void switchToResultsPage(String[][] output) {
		//Create a new ResultsPage
		ResultsPage fragInstance = new ResultsPage();

		//Replace the existing fragment with the new one
		FragmentTransaction transact = manager.beginTransaction();
		transact.replace(fragmentFrame, fragInstance, Shard.RESULTS_PAGE.toString());
		transact.commit();

		//Give the results page some results
		fragInstance.setContent(output, this);

	} //End public void switchToResultsPage(String[][])

	public void switchToLoadingPage() {
		//Replace the existing fragment with the new one
		FragmentTransaction transact = manager.beginTransaction();
		transact.replace(fragmentFrame, new LoadingPage(), Shard.LOADING_PAGE.toString());
		transact.commit();

	} //End public void switchToLoadingPage()
} //End Class
