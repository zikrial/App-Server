package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.rmi.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ChatServer extends WebSocketServer{
	
	public ChatServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}
	
	public ChatServer(InetSocketAddress address) {
		super(address);
	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		broadcast(conn + " has left the room !");
		System.out.println(conn + " has left the room !");
	}
	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		// TODO Auto-generated method stub
		ex.printStackTrace();
		if (conn != null) {
			System.out.println("Web Socket failed to connect");
		}
	}
	
	@Override
	public void onMessage(WebSocket conn, String message) {
		// TODO Auto-generated method stub
		broadcast(message);
		System.out.println(conn + " : " + message);
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// TODO Auto-generated method stub
		//Method untuk mengirimkan pesan ke client yang terhubung
		conn.send("Welcome to Server!");
		broadcast("new connection : " + handshake.getResourceDescriptor());
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room");
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("Server Started!");
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		WebSocketImpl.DEBUG = true;
		int port = 8887;
		ChatServer s;
		
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			s = new ChatServer(port);
			s.start();
			System.out.println("ChatServer started on port : " + s.getPort());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String in;
			try {
				in = sysin.readLine();
				s = new ChatServer(port);
				s.broadcast(in);
				if(in.equals("exit")) {
					s.stop();
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
