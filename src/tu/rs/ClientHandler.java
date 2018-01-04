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

public class ClientHandler extends Thread  {

	private Socket client;
	private ArrayList<PeerListEntry> knownPeers;
	private InetAddress addr;
	
	
	public ClientHandler( Socket socket, ArrayList<PeerListEntry> arr) {
		
		this.client = socket;
		this.knownPeers = arr;
	}

	@Override
	public void run()
	{
		addr = client.getInetAddress();
		System.out.println("Eingehender Verbindung von:"+ addr.getHostAddress()+":"+client.getPort());
		try {
			
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			BufferedReader buff =  new BufferedReader(new InputStreamReader(client.getInputStream()));
			String strInput;
			
			while((strInput = buff.readLine()) != null)
			{
				
				String[] test =	strInput.split(" ");
				
				if(test[0].equalsIgnoreCase("POKE"))
				{
					
				}
				else if(test[0].equals("DISCONNECT"))
				{
					
				}
				else if(test[0].equals("MESSAGE"))
				{
					
				}
				
				
			}	
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public synchronized void addToPeers(String name,String ip,int port)
	{
		PeerListEntry temp = new PeerListEntry(name,ip,port);
		knownPeers.add(temp);
	}
	
}
