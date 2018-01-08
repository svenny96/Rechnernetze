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
		this.server = server; //testggggggggg
	}
	
	@Override
	public void run()
	{
		System.out.println("Horche an Port:"+server.getLocalPort());
		try {
			
			while(true)
			{
				Socket client = server.accept();
				System.out.println(client.getInetAddress());
				ClientHandler cHandler = new ClientHandler(peer,client);
				cHandler.start();
			}	
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
