package edu.carleton.COMP2601;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class NetworkService extends Service {

	private final IBinder mBinder = new MyBinder();
	private ObjectInputStream dis;
	private ObjectOutputStream dos;
	private Socket client;

	public void sendMessage(String string) {
		try {
			dos.writeObject(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public Message receiveMessage() {
		String strReply = null;
		try {
			strReply = (String) dis.readObject();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (strReply != null) {
			Message reply = new Message(strReply);
			return reply;
		}
		return null;
	}

	public void establishConnection(String ipAddress, int portNum) {
		try {
			client = new Socket(ipAddress, portNum);
			
			if (isConnected()) {
				dos = new ObjectOutputStream(client.getOutputStream());
				dis = new ObjectInputStream(client.getInputStream());
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public boolean isConnected() {
		return client.isConnected();
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class MyBinder extends Binder {
		NetworkService getService() {
			return NetworkService.this;
		}
	}

}
