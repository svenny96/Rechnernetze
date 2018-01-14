package tu.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientHandler extends Thread  {

	private Socket client;
	private InetAddress addr;
	private Peer peer;
	
	
	public ClientHandler(Peer peer, Socket socket) {
		
		this.peer = peer;
		this.client = socket;
		
	}

	@Override
	public void run()
	{
		addr = client.getInetAddress();
		
		try {
			
			
			BufferedReader buff =  new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			String strInput;
			
			while(buff.ready() && (strInput = buff.readLine()) != null)				//"Abhören" des StreamsS
			{
				//System.out.println(strInput);
				String[] test =	strInput.split(" ");
				PeerListEntry commPeer = null;
				//System.out.println(test[1]+" "+test[2]+" "+test[3]);
				
				
					if(test[0].equalsIgnoreCase("POKE"))
					{
						
						if( test.length >= 3 && (!test[2].equals(peer.getIp()) || !(Integer.parseInt(test[3])  == peer.getPort())))  //Test ob Poke eigene Daten enthält
						{
						commPeer = new PeerListEntry(test[1],test[2],Integer.parseInt(test[3]));
						
						
						if(exists(commPeer))
						{
							//System.out.println("exists");
							commPeer = getListElement(commPeer);   						 //commPeer verweist nun auf das tatsächliche Element in der Liste knownPeers
							commPeer.setLastPoke(System.currentTimeMillis() / 1000L);
						}
						else
						{
							//System.out.println("not exists");
							
							
							
							peer.addPeer(commPeer);
							peer.poke(commPeer.getIp(),commPeer.getPort());				//Poke mit eigenen Daten als Antwort
							peer.pokeAll(commPeer);										//Alle bekannten Peers werden über neuen Teilnehmer benachrichtigt
							       	
						}
						
					}
				}
				else if(test[0].equals("DISCONNECT"))
				{
					
					commPeer = new PeerListEntry(test[1],test[2],Integer.parseInt(test[3]));
					
					if(exists(commPeer))
					{
						
						commPeer = getListElement(commPeer);							//
						peer.getKnownPeers().remove(commPeer);							//Client wird aus Liste entfernt
						peer.disconnect(test[1],test[2],Integer.parseInt(test[3]));		//und schließlich wird die DISCONNECT Nachricht an alle bekannten Peers weitergeleitet
					}
					
				}
				
				else if(test[0].equals("MESSAGE"))
				{
					System.out.print("Nachricht von "+ test[1] + ":");					//Inhalt der Nachricht wird ausgegeben
					for(int i = 4; i < test.length; i++){
						System.out.print(" "+test[i]);
					}
					System.out.println();
				}
				
				
			}	
			
			
		} catch (IOException e) {
			System.out.println("End of Stream");
			
		}
		
		
	}
	
	
	public boolean exists(PeerListEntry entry)																					//Existiert ein Peer mit diesen Daten schon
	{
		for(PeerListEntry compare : peer.getKnownPeers())
		{
			
			//System.out.println("Vergleiche:"+entry.getIp()+"/"+compare.getIp()+","+entry.getPort()+"/"+compare.getPort());
		   
			if( compare.getIp().equals(entry.getIp()) && compare.getPort() == entry.getPort())
			{
				return true;
			}
		}
		return false;
	}
	
	public synchronized PeerListEntry getListElement(PeerListEntry entry)														//Verweist auf das tatsächliche Element in der Peer Liste
	{
		for(int i=0; i<peer.getKnownPeers().size();i++)
		{
		   PeerListEntry index = peer.getKnownPeers().get(i);
			if(index.getIp().equals(entry.getIp())   && index.getPort() == entry.getPort())
			{
				return index;
			}
		}
		return null;
		
	}
	
	
	
}
