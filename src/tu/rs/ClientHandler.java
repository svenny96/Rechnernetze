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
		System.out.println("Eingehende Verbindung von:"+ addr.getHostAddress()+":"+client.getPort());
		try {
			
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			BufferedReader buff =  new BufferedReader(new InputStreamReader(client.getInputStream()));
			peer.setIp(client.getRemoteSocketAddress().toString().split(":")[0]);
			String strInput;
			
			while((strInput = buff.readLine()) != null)
			{
				
				String[] test =	strInput.split(" ");
				PeerListEntry commPeer = new PeerListEntry(test[1],test[2],Integer.parseInt(test[3]));
				
				if(test[0].equalsIgnoreCase("POKE"))
				{
					
					if(peer.exists(commPeer))
					{
						commPeer = peer.getListElement(commPeer);    //commPeer verweist nun auf das tatsächliche Element in der Liste knownPeers
						commPeer.setLastPoke(System.currentTimeMillis() / 1000L);
					}
					else
					{
						commPeer.setLastPoke(System.currentTimeMillis() / 1000L);
						commPeer.setClient(this.client);
						peer.addPeer(commPeer);
					}
				}
				
				else if(test[0].equals("DISCONNECT"))
				{
					
					if(peer.exists(commPeer))
					{
						commPeer = peer.getListElement(commPeer);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
}
