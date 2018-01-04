package tu.rs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

	private ServerSocket server;
	
	public ServerThread(ServerSocket server)
	{
		this.server = server; //testggggg
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
				ClientHandler cHandler = new ClientHandler(client);
				cHandler.start();
			}	
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
