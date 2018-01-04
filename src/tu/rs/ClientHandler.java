package tu.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread  {

	private Socket client;
	
	
	public ClientHandler( Socket socket) {
		
		this.client = socket;
	}

	@Override
	public void run()
	{
		System.out.println("ClientHandler");
		try {
			
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			BufferedReader buff =  new BufferedReader(new InputStreamReader(client.getInputStream()));
			String strInput;
			
			while((strInput = buff.readLine()) != null)
			{
				
				String[] test =	strInput.split(",");
				
			}	
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
