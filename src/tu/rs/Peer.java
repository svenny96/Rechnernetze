package tu.rs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Peer {


private String name;
private String ip;
private int port;
private ServerSocket sSocket;
private Socket socket = new Socket();
private ArrayList<PeerListEntry> knownPeers = new ArrayList<PeerListEntry>();


	public Peer(String name,int port)
	{
		this.name = name;
		
		this.port = port;
		
		
		
		try {
			sSocket = new ServerSocket(port);
			ServerThread sThread = new ServerThread(sSocket,this);
			sThread.start();
			UserInputThread uiThread = new UserInputThread(this);
			uiThread.start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void connect(String ip,int port)
	{
		try {
			socket.connect(new InetSocketAddress(ip,port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void disconnect()
	{
		
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public synchronized ArrayList<PeerListEntry> getKnownPeers() {
		return knownPeers;
	}


	public void setKnownPeers(ArrayList<PeerListEntry> knownPeers) {
		this.knownPeers = knownPeers;
	}
	
	
	

}
