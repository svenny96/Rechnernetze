package tu.rs;

import java.net.Socket;

public class PeerListEntry {
	private String name;
	private String ip;
	private int port;
	private long lastPoke;
	private boolean isActive;;
	private Socket client;
	
	



	public PeerListEntry(String name,String ip,int port)
	{
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.isActive = true;
		this.lastPoke = System.currentTimeMillis() / 1000L; //UnixTime
	
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
	public long getLastPoke() {
		return lastPoke;
	}
	public void setLastPoke(long lastPoke) {
		this.lastPoke = lastPoke;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Socket getClient() {
		return client;
	}



	public void setClient(Socket client) {
		this.client = client;
	}

	
}
