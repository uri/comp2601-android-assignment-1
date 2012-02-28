package edu.carleton.COMP2601;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Assignment1Activity extends Activity implements Runnable {
	/** Called when the activity is first created. */

	Message m;

	private InetSocketAddress address;
	private Assignment1Activity parent;
	private ClientState state;

	private int portNum = 7001;

	private Handler handler = new Handler();
	private Toast toast;

	private Button b;

	private EditText ipField;
	private EditText portField;

	private static Assignment1Activity instance;

	// Network service
	private NetworkService netService;

	// Service connections

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.main);

		ServiceConnection serviceConnection = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				netService = null;
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				netService = ((NetworkService.MyBinder) service).getService();
			}
		};

		// Bind the service
		getApplicationContext().bindService(new Intent(this, NetworkService.class), serviceConnection, BIND_AUTO_CREATE);

		state = ClientState.NOT_CONNECTED;

		ipField = (EditText) findViewById(R.id.ipAddressField);
		portField = (EditText) findViewById(R.id.portField);

		ipField.setText("" + Common.IP_ADDRESS);
		portField.setText("" + portNum);

		// Start the activity
		b = (Button) findViewById(R.id.buttonStart);
		parent = this;

		// Create a listener
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Thread(parent).start();
			}
		});
	}

	public void run() {

		// Check if the socket is connected

		if (state == ClientState.NOT_CONNECTED) {
			netService.establishConnection(Common.IP_ADDRESS, portNum);
			if (netService.isConnected()) {
				state = ClientState.CONNECTED;
				handler.post(new Runnable() {

					public void run() {
						b.setText("Login");
					}
				});

			}

		} else if (state == ClientState.CONNECTED) {

			// Send a message
			netService.sendMessage(new Message (Message.REQ_LOGIN));

			// Now we read a message
			getReply(Message.REPLY_LOGIN, ClientState.LOGGED_IN);
			handler.post(new Runnable() {
				public void run() {
					b.setText("Grab File List");
				}
			});

		} else if (state == ClientState.LOGGED_IN) {
			// We are logged in, we would like to get the list of files
			// Send the request files message
			netService.sendMessage(new Message(Message.REQ_LIST_FILES));
			// dos.writeObject(new Message(Message.REQ_LIST_FILES, null));
			toastMessage("Sent" + Message.REQ_LIST_FILES);

			// Get the reply
			Message reply = getReply(Message.REPLY_LIST_FILES, ClientState.VIEWING_FILES);
			if (reply != null) {
				Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
				intent.putExtra("fileList", (ArrayList<String>) reply.getBody().get(Message.KEY_FILE_LIST));
				intent.putExtra("state", state);
				startActivity(intent);
			}

		} else {
			// toastMessage("The date is " + dis.readUTF());
			toastMessage("Could not connect");
		}

	}

	private Message getReply(String expectedReply, ClientState newState) {

		Message reply = netService.receiveMessage();

		if (reply != null) {
			if (reply.getHeader().getType().equals(expectedReply)) {
				state = newState;
				return reply;
			}
		}

		return null;
	}

	public void toastMessage(final CharSequence message) {
		handler.post(new Runnable() {
			public void run() {
				Context context = getApplicationContext();
				CharSequence text = message;
				int duration = Toast.LENGTH_SHORT;

				toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});
	}

	public static Assignment1Activity getInstance() {
		return instance;
	}

	public NetworkService getNetworkService() {
		return netService;
	}
}