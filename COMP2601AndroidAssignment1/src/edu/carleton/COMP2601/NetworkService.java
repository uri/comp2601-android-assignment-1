package edu.carleton.COMP2601;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class NetworkService extends Service {

	private final IBinder mBinder = new MyBinder();
	private JsonReader dis;
	private JsonWriter dos;
	private Socket client;
	private Gson gson;

	public void sendMessages() {
		// TODO Complete me
	}
	
	public void sendMessage(Message m) {
		try {
			
			dos = new JsonWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
			
			dos.beginArray();
			gson.toJson(m, Message.class, dos);
			dos.endArray();
			
			// Flush to send
			dos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receiveMessages() {
		// TODO Complete me
	}
	
	public Message receiveMessage() {
		
		
		Message reply = null;
		
		try {
			dis = new JsonReader(new BufferedReader(new InputStreamReader(client.getInputStream())));	
			
			dis.beginArray();
			reply = gson.fromJson(dis, Message.class);
			dis.endArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return reply;
	}

	public void establishConnection(String ipAddress, int portNum) {
		try {
			client = new Socket(ipAddress, portNum);
			
			if (isConnected()) {
				
				
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
		gson = new GsonBuilder().create();
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
