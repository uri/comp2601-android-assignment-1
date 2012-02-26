package edu.carleton.COMP2601;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListViewAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final ArrayList<String> values;

	public CustomListViewAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.list_row_layout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.list_row_layout, parent, false);
		
		// Get some elements
		TextView textView = (TextView) rowView.findViewById(R.id.textView);
		CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox1);
		textView.setText(values.get(position));

		
		
		// Change the icon for Windows and iPhone
		String curString = values.get(position);
		checkBox.setChecked(curString.substring(curString.length() - 3).equals("txt"));
		

		return rowView;
	}
	


}
