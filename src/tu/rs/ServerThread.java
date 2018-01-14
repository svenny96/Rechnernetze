package tu.rs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private Peer peer;
	private ServerSocket server;
	
	public ServerThread(ServerSocket server, Peer peer)
	{
		this.peer = peer;
		this.server = server; 
	}
	
	@Override
	public void run()
	{
		System.out.println("Horche an Port:"+server.getLocalPort());
		System.out.println("Zusätzliche Befehle:");
		System.out.println("list : Gibt die aktuelle Liste bekannter Peers aus.");
		try {
			
			while(true)
			{
				Socket client = server.accept();														//Server wartet auf eingehende Verbindungen und startet jeweils einen neuen ClientHandler
				//System.out.println(client.getInetAddress());
				ClientHandler cHandler = new ClientHandler(peer,client);
				cHandler.start();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
