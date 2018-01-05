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
	private ArrayList<PeerListEntry> knownPeers;
	private InetAddress addr;
	private Peer peer;
	
	public ClientHandler(Peer peer, Socket socket, ArrayList<PeerListEntry> arr) {
		
		this.peer = peer;
		this.client = socket;
		this.knownPeers = arr;
	}

	@Override
	public void run()
	{
		addr = client.getInetAddress();
		System.out.println("Eingehende Verbindung von:"+ addr.getHostAddress()+":"+client.getPort());
		try {
			
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			BufferedReader buff =  new BufferedReader(new InputStreamReader(client.getInputStream()));
			String strInput;
			
			while((strInput = buff.readLine()) != null)
			{
				
				String[] test =	strInput.split(" ");
				PeerListEntry commPeer = new PeerListEntry(test[1],test[2],Integer.parseInt(test[3]));
				
				if(test[0].equalsIgnoreCase("POKE"))
				{
					
					if(exists(commPeer))
					{
						commPeer = getListElement(commPeer);    //commPeer verweist nun auf das tatsächliche Element in der Liste knownPeers
						commPeer.setLastPoke(System.currentTimeMillis() / 1000L);
					}
					commPeer.setLastPoke(System.currentTimeMillis() / 1000L);
					commPeer.setClient(this.client);
					knownPeers.add(commPeer);
				}
				
				else if(test[0].equals("DISCONNECT"))
				{
					if(exists(commPeer))
					{
						commPeer = getListElement(commPeer);
						knownPeers.remove(commPeer);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public synchronized void addToPeers(PeerListEntry peer)
	{
		
		knownPeers.add(peer);
	}
	
	public synchronized boolean exists(PeerListEntry peer)
	{
		for(int i=0; i<knownPeers.size();i++)
		{
		   PeerListEntry index = knownPeers.get(i);
			if(index.getName() == peer.getName() && index.getIp() == peer.getIp() && index.getPort() == peer.getPort())
			{
				return true;
			}
		}
		return false;
	}
	
	public synchronized PeerListEntry getListElement(PeerListEntry peer)
	{
		for(int i=0; i<knownPeers.size();i++)
		{
		   PeerListEntry index = knownPeers.get(i);
			if(index.getName() == peer.getName() && index.getIp() == peer.getIp() && index.getPort() == peer.getPort())
			{
				return index;
			}
		}
		return null;
		
	}
	
}
