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
				System.out.println(strInput);
				String[] test =	strInput.split(" ");
				PeerListEntry commPeer = null;
				System.out.println(test[1]+" "+test[2]+" "+test[3]);
				
				if(test[1] != peer.getName() || test[2] != peer.getIp() || test[3] !=  ""+peer.getPort())  //Test ob Poke eigene Daten enthält
				{
					if(test[0].equalsIgnoreCase("POKE"))
					{
						
						
						commPeer = new PeerListEntry(test[1],test[2],Integer.parseInt(test[3]));
						
						
						if(exists(commPeer))
						{
							System.out.println("exists");
							commPeer = getListElement(commPeer);    //commPeer verweist nun auf das tatsächliche Element in der Liste knownPeers
							commPeer.setLastPoke(System.currentTimeMillis() / 1000L);
						}
						else
						{
							System.out.println("not exists");
							
							
							
							peer.addPeer(commPeer);
							peer.printPeers();
							peer.pokeAll(commPeer);		//Alle bekannten Peers werden über neuen Teilnehmer benachrichtigt
							peer.poke(commPeer.getIp(),commPeer.getPort());       	//Poke mit eigenen Daten als Antwort
						}
					}
				}
				else if(test[0].equals("DISCONNECT"))
				{
					
					if(exists(commPeer))
					{
						commPeer = getListElement(commPeer);
						peer.removePeer(commPeer);
						peer.disconnect();
					}
				}
				
				else if(test[0].equals("MESSAGE"))
				{
					String message="Nachricht von"+client.getInetAddress().getHostAddress()+":";
					for(int i=1 ; i< test.length; i++)
					{
						message.concat(test[i]);
					}
					System.out.println(message);
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
			
			System.out.println("Vergleiche:"+entry.getIp()+"/"+compare.getIp()+","+entry.getPort()+"/"+compare.getPort());
		   
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
			if(index.getIp().equals(peer.getIp())   && index.getPort() == peer.getPort())
			{
				return index;
			}
		}
		return null;
		
	}
	
	
	
}
