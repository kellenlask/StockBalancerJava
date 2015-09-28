package com.lask.stockbalancerjava.GUIElements;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.lask.stockbalancerjava.R;

import java.util.ArrayList;


public class ResultsPage extends Fragment {

	//--------------------------------------------------------
	//
	//		Fields
	//
	//--------------------------------------------------------

	//Constants
	public static final String[] TABLE_TITLES = {"TICKER", "ACTION", "NEW PERCENTAGE"};

	//Other Fields
	private MainActivity parent;
	private GridView dataTable;
	private Button refreshButton;
	private ArrayList<String> content;

	//--------------------------------------------------------
	//
	//		Constructors / Initializers
	//
	//--------------------------------------------------------
	public ResultsPage() {	/* Empty Constructor */	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_results_page, container, false);

	}

	@Override
	public void onStart() {
		super.onStart();

		//Set the UI elements as fields
		dataTable = (GridView) parent.findViewById(R.id.dataTable);
		refreshButton = (Button) parent.findViewById(R.id.refreshButton);

		//Set refreshButton's action handler
		setRefreshButton();

		//Create Array Adapter from data
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent.getBaseContext(), android.R.layout.simple_expandable_list_item_1, content);

		//Add data to table
		dataTable.setAdapter(adapter);

	}

	//Parse a 2-d String array into ArrayList for GridLayout
	public void setContent(String[][] output, MainActivity activity) {
		parent = activity;

		//Initialize ArrayList for ArrayAdapter
		content = new ArrayList<>();

		//Add the summary details
		for(int i = 0; i < output.length; i++) {
			content.add(output[i][0]);
			content.add(output[i][1]);
			content.add(output[i][2]);

		} //End for

	} //End public void setContent(String[][])

	//--------------------------------------------------------
	//
	//		Action Handlers
	//
	//--------------------------------------------------------

	public void setRefreshButton() {
		refreshButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.switchToLoadingPage();
			}
		});

	}
}
