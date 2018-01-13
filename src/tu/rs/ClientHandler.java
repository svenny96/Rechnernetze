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
			
			while(buff.ready() && (strInput = buff.readLine()) != null)
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
							commPeer = getListElement(commPeer);    //commPeer verweist nun auf das tatsächliche Element in der Liste knownPeers
							commPeer.setLastPoke(System.currentTimeMillis() / 1000L);
						}
						else
						{
							//System.out.println("not exists");
							
							
							
							peer.addPeer(commPeer);
							peer.poke(commPeer.getIp(),commPeer.getPort());
							peer.pokeAll(commPeer);		//Alle bekannten Peers werden über neuen Teilnehmer benachrichtigt
							       	//Poke mit eigenen Daten als Antwort
						}
						
					}
				}
				else if(test[0].equals("DISCONNECT"))
				{
					System.out.println("disonnect"); //kontrollausgabe
					commPeer = new PeerListEntry(test[1],test[2],Integer.parseInt(test[3]));
					
					if(exists(commPeer))
					{
						
						commPeer = getListElement(commPeer);
						peer.getKnownPeers().remove(commPeer);
						peer.disconnect(test[1],test[2],Integer.parseInt(test[3]));
					}
					
				}
				
				else if(test[0].equals("MESSAGE"))
				{
					System.out.print("Nachricht von "+ test[1] + ":");
					for(int i = 4; i < test.length; i++){
						System.out.print(" "+test[i]);
					}
					System.out.println();
				}
				
				
			}	
			
			
		} catch (IOException e) {
			System.out.println("End of Stream");
			e.printStackTrace();
		}
		
		
	}
	
	
	public boolean exists(PeerListEntry entry)
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
	
	public synchronized PeerListEntry getListElement(PeerListEntry entry)
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
