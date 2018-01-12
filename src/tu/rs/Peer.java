package tu.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Peer {


private String name;
private String ip;
private int port;
private ServerSocket sSocket;

private CopyOnWriteArrayList<PeerListEntry> knownPeers = new CopyOnWriteArrayList<PeerListEntry>();


	public Peer(String name,int port)
	{
		this.name = name;
		
		this.port = port;
		
		
		
		try {
			sSocket = new ServerSocket(port);
			this.ip = sSocket.getInetAddress().getLocalHost().getHostAddress();
			System.out.println(this.name+"/"+this.ip+"/"+this.port);
			ServerThread sThread = new ServerThread(sSocket,this);
			sThread.start();
			UserInputThread uiThread = new UserInputThread(this);
			uiThread.start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public synchronized void disconnect(){
		for(PeerListEntry peer : knownPeers){
			messageSingle( peer.getIp(), peer.getPort(), "DISCONNECTED: " + this.name + " " + this.ip + " " + this.port);
		}
		knownPeers.clear();
	}
	
	public synchronized void disconnectClient(PeerListEntry entry)
	{
		removePeer(entry);
		
		
			for(PeerListEntry peer : knownPeers)
			{
				
			}
	
		
	}
	
	public synchronized void exit(){
		System.exit(0);
	}
	
	public synchronized void messageMulti(String[] args){
		
		String[] messageArray = new String[args.length - 2];
		
		
		for(int i = 2; i < args.length; i++){
			messageArray[i-2] = args[i];
		}
		
		String message = String.join(" ", messageArray);
		
		for(PeerListEntry peer : knownPeers){
			
			if(peer.getName().equals(args[1])){
				messageSingle( peer.getIp(), peer.getPort(), message);
			}
		}
	}
	
	public synchronized void messageSingle(String ip, int port, String text){
		
		try {
			Socket messageSocket = new Socket(ip,port);
			
		
			PrintWriter out = new PrintWriter(messageSocket.getOutputStream(),true);
			
			out.println("MESSAGE "+name+" "+ip+" "+port+" "+text);
			
			
			
		} catch (IOException e) {
			System.out.println("Senden fehlgeschlagen");
			e.printStackTrace();
		}
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


	public synchronized CopyOnWriteArrayList<PeerListEntry> getKnownPeers() {
		return knownPeers;
	}


	public void setKnownPeers(CopyOnWriteArrayList<PeerListEntry> knownPeers) {
		this.knownPeers = knownPeers;
	}
	

	public synchronized void addPeer(PeerListEntry peer)
	{
		
		knownPeers.add(peer);
	}
	

	
	
	public synchronized void removePeer(PeerListEntry peer)
	{
		for(int i=0; i<knownPeers.size();i++)
		{
		   PeerListEntry index = knownPeers.get(i);
			if(index.getName() == peer.getName() && index.getIp() == peer.getIp() && index.getPort() == peer.getPort())
			{
				knownPeers.remove(i);
			}
		}
		
	}
	
	public void poke(String ip,int port)							//Poke Nachricht mit eigenen Daten an einzelnen Client schicken
	{
		try {
			Socket pokeSocket = new Socket(ip,port);
			
		
			PrintWriter out = new PrintWriter(pokeSocket.getOutputStream(),true);
			
			out.println("POKE "+this.getName()+" "+this.getIp()+" "+this.getPort());
			
			
			
		} catch (IOException e) {
			System.out.println("Senden fehlgeschlagen");
			e.printStackTrace();
		}
		
	}
	 public synchronized void  pokeAll(PeerListEntry unknown) 						//Weiterleitung der Poke Nachricht bei bisher unbekanntem Sender
	 {
		 try{
			 for(PeerListEntry entry  : knownPeers)
			 {
				Socket socket = new Socket(); 															//Socket muss neu initialisiert werden da ein close Aufruf die weiter Nutzung verhindert
				socket.connect(new InetSocketAddress(entry.getIp(),entry.getPort()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
				out.println("POKE "+unknown.getName()+" "+unknown.getIp()+" "+unknown.getPort());
				
				socket.close();
			 }
		 }
		 catch (IOException e) {
				System.out.println("Übertragungsfehler");
				e.printStackTrace();
			}
	 }
	 public void printPeers()
	 {
		 for(PeerListEntry entry : knownPeers)
		 {
			 System.out.println("Liste:"+entry.getName()+" "+entry.getIp()+" "+entry.getPort());
		 }
	 }
	
	

}
