package com.trackertraced.trackerbee.application.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class TcpClient {
	private static final String TAG = "TcpClient";
	private String SERVER_IP = "27.147.151.61"; // server IP address
	private int SERVER_PORT = 10000;
	private static final int SOCKET_READ_TIMEOUT_MILLISECONDS = 1000;
	// sends message received notifications
	private OnMessageReceived mMessageListener = null;
	// while this is true, the server will continue running
	private boolean mRun = false;
	// used to send messages
	private PrintWriter mBufferOut;
	// used to read messages from the server
	private BufferedReader mBufferIn;

	// message to send to the server
	// private String mServerMessage;
	private char[] mBuffer = new char[4096];

	public TcpClient() {

	}

	/**
	 * Constructor of the class. OnMessagedReceived listens for the messages
	 * received from server
	 */
	public TcpClient(String server_ip, int server_port,
			OnMessageReceived listener) {
		mMessageListener = listener;
		SERVER_IP = server_ip;
		SERVER_PORT = server_port;
		// mBuffer = new char[4096];
	}

	/**
	 * Constructor of the class.
	 */
	public TcpClient(String server_ip, int server_port) {
		SERVER_IP = server_ip;
		SERVER_PORT = server_port;
		// mBuffer = new char[4096];
	}

	/**
	 * Sends the message entered by client to the server
	 * 
	 * @param packet
	 *            String data
	 * @throws IOException
	 */
	public void Sendlocation(String packet) throws IOException {
		if (mBufferOut != null && !mBufferOut.checkError()) {
			Log.i(TAG, "Sending: " + packet);
			mBufferOut.println(packet);
			mBufferOut.flush();
		}
	}

	public void SendLocation(Object ojb) throws IOException {
		if (mBufferOut != null && !mBufferOut.checkError()) {
			mBufferOut.print(ojb);
			mBufferOut.flush();
		}
	}

	/**
	 * Sends the message entered by client to the server
	 * 
	 * @param packet
	 *            char[] data
	 * @throws IOException
	 */
	public void Sendlocation(char[] packet) throws IOException {
		if (mBufferOut != null && !mBufferOut.checkError()) {
			mBufferOut.println(packet);
			mBufferOut.flush();
		}
	}

	/**
	 * Close the connection and release the members
	 */
	public void stopClient() throws IOException, Exception {

		// send mesage that we are closing the connection
		// sendMessage("");
		Log.i(TAG, "Stoping Client...");
		mRun = false;
		if (mBufferOut != null) {
			mBufferOut.flush();
			mBufferOut.close();
		}

		mMessageListener = null;
		mBufferOut = null;
		mBufferIn = null;
		// mServerMessage = null;
		mBuffer = null;
	}

	public Socket ConnectToServer(String server, int port) throws Exception {
		InetAddress serverAddr = null;
		Socket socket = null;

		Log.i(TAG, "Connecting to" + server + ":" + port);
		try {
			serverAddr = InetAddress.getByName(server);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create a socket to make the connection with the server
		try {
			socket = new Socket(serverAddr, port);
			socket.setSoTimeout(SOCKET_READ_TIMEOUT_MILLISECONDS * 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "Conected To server");
		return socket;
	}

	public void InitBuffers(Socket socket) {
		try {
			// sends the message to the server
			Log.i(TAG, "Buffer Init... socket:" + socket);
			mBufferOut = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
			// receives the message which the server sends back
			mBufferIn = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String WaitForServerResponce() throws Exception {
		int nByte;
		nByte = mBufferIn.read(mBuffer);
		String buff = null;
		if (nByte > 0 && mMessageListener != null) {
			buff = String.valueOf(mBuffer, 0, nByte);
			Log.i(TAG, "Received Message(" + nByte + "): [" + buff + "]");
		}
		return buff;
	}

	public void readBffer() throws Exception {
		int nByte;
		nByte = mBufferIn.read(mBuffer);
		if (nByte > 0 && mMessageListener != null) {
			Log.i(TAG,
					"Received Message(" + nByte + "): ["
							+ String.valueOf(mBuffer, 0, nByte) + "]");
			// // call the method messageReceived from MyActivity
			// class
			mMessageListener.messageReceived(mBuffer, nByte);
		}
	}

	public void run() {

		mRun = true;

		try {
			Socket socket = ConnectToServer(SERVER_IP, SERVER_PORT);
			try {
				InitBuffers(socket);
				Log.d(TAG, "In/OUT buffer ready");
				// in this while the client listens for the messages sent by the
				// server
				while (mRun) {
					Log.d(TAG,
							"Waiting for server responce (mMessageListener): "
									+ mMessageListener);
					// mServerMessage = mBufferIn.readLine();
					readBffer();
				}

			} catch (Exception e) {

				Log.w(TAG, "run() Error:", e);

			} finally {
				// the socket must be closed. It is not possible to reconnect to
				// this socket
				// after it is closed, which means a new socket instance has to
				// be created.
				socket.close();
				Log.d(TAG, "socket.close()");
			}

		} catch (Exception e) {

			Log.w(TAG, "Error:", e);

		}

	}

	// Declare the interface. The method messageReceived(String message) will
	// must be implemented in the MyActivity
	// class at on asynckTask doInBackground
	public interface OnMessageReceived {
		public void messageReceived(char[] ReceivedMessag, int lenght);
	}
}
