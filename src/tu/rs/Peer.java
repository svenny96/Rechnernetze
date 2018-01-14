package tu.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
			ServerThread sThread = new ServerThread(sSocket,this);														//Server Thread gestartet um auf eingehende Verbindungen zu warten
			sThread.start();																							
			UserInputThread uiThread = new UserInputThread(this);														//UserInputThread zur Konsoleneingabe gestartet
			uiThread.start();
			PeerTimer peerTimer = new PeerTimer(this);
			final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();				//30 Sekunden nach Programmstart wird alle 30 Sekunden die run-Methode 
			executorService.scheduleAtFixedRate(peerTimer, 30, 30, TimeUnit.SECONDS);									//der PeerTimer Klasse ausgeführt.
			
			
		} catch (IOException e) {
			System.out.println("Fehler bei Initialisierung");
		}
		
		
	}
	
	
	
	public synchronized void disconnect(String name,String ip,int port){				//Für den angegebenen Client wird eine disconnect Nachricht verschickt.
		Socket disconnectSocket = null;												
		PrintWriter out  = null;
		
		for(PeerListEntry peer : knownPeers){
			try {
				disconnectSocket = new Socket(peer.getIp(),peer.getPort());
				out = new PrintWriter(disconnectSocket.getOutputStream(),true);
				out.println("DISCONNECT "+name+" "+ip+" "+port);
				disconnectSocket.close();
			} catch (UnknownHostException e) {
				System.out.println("Host Adresse konnte nicht aufgelöst werden");
			} catch (IOException e) {
				System.out.println("Client "+peer.getName()+" "+"nicht erreichbar");
			}
		}
		
		
		
	}
	
	
	
	public synchronized void exit(){
		try {
			sSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		boolean valid = false;
		for(PeerListEntry compare : knownPeers){
			valid = valid | ( compare.getIp().equals(ip) & compare.getPort() == port );
		}
		if(valid){
			
		    try {
			    Socket messageSocket = new Socket(ip,port);
			 
		 
		 	    PrintWriter out = new PrintWriter(messageSocket.getOutputStream(),true);
		 	
			    out.println("MESSAGE "+name+" "+ip+" "+port+" "+text);
			
			    messageSocket.close();
			
		    } catch (IOException e) {
			    System.out.println("Senden fehlgeschlagen");
			    e.printStackTrace();
		    }
		    
		} else{
			System.out.println("Unbekannter Empfänger, Nachricht nicht versendet");
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
			
			pokeSocket.close();
			
		} catch (IOException e) {
			System.out.println("Verbindung zur angegebenen Adresse konnte nicht hergestellt werden");
			
		}
		
	}
	 public synchronized void  pokeAll(PeerListEntry unknown) 						//Weiterleitung der Poke Nachricht bei bisher unbekanntem Sender
	 {
		 try{
			 for(PeerListEntry entry  : knownPeers)
			 {
				Socket socket = new Socket(); 														
				socket.connect(new InetSocketAddress(entry.getIp(),entry.getPort()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
				out.println("POKE "+unknown.getName()+" "+unknown.getIp()+" "+unknown.getPort());
				
				socket.close();
			 }
		 }
		 catch (IOException e) {
				System.out.println("Übertragungsfehler");
				
			}
	 }
	 public void printPeers()																		//Ausgeben der Liste bisher bekannter Peers
	 {
		 for(PeerListEntry entry : knownPeers)
		 {
			 System.out.println("Liste:"+entry.getName()+" "+entry.getIp()+" "+entry.getPort()+" "+entry.getLastPoke());
		 }
	 }
	
	

}
