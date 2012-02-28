package edu.carleton.COMP2601;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewActivity extends ListActivity implements Runnable {

	private ArrayList<String> fileList;

	private int position;
	private String content;
	private ClientState state;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();

		// Get the input streams
		if (extras != null) {
			fileList = (ArrayList<String>) extras.getSerializable("fileList");

			state = (ClientState) extras.getSerializable("state");
		}

		CustomListViewAdapter adapter = new CustomListViewAdapter(this,
				fileList);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		

		this.position = position;
		
		if (fileList.get(position).substring(fileList.get(position).length() - 3).equals("txt"))
			new Thread(this).start();

	}

	@Override
	public void run() {

		// Send the request for a file
		Message request = new Message(Message.REQ_FILE);
		request.getBody().put(Message.KEY_FILE, fileList.get(position));
		
		Assignment1Activity.getInstance().getNetworkService().sendMessage(request);

		// Listen for the response
		Message reply = Assignment1Activity.getInstance().getNetworkService().receiveMessage();
		
		// Check if the reply is good
		if (reply != null) {
			if (reply.getHeader().getType().equals(Message.REPLY_FILE)) {
//				Toast.makeText(this, "Content Reply", Toast.LENGTH_LONG).show();
				content = (String) reply.getBody().get(Message.KEY_CONTENT);

				// Start a new activity at this point
				if (content != null) {
					Intent intent = new Intent(getApplicationContext(),
							FileViewActivity.class);
					intent.putExtra("content", content);
					startActivity(intent);
				}

			}
			
		} else {

		}
		

		// Listen for a response
	}

}
