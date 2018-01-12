package tu.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputThread extends Thread {
	
	private Peer peer;

	public UserInputThread(Peer peer)
	{
		this.peer = peer;
	}


	@Override
	public void run()
	{
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    
		    String uInput;
			while((uInput = reader.readLine()) != null)
			{
				String[] test = uInput.split(" ");
					
				if( test[0].equalsIgnoreCase("CONNECT")){
					
				    peer.connect(test[1], Integer.parseInt(test[2]));
				    
				}
				
				else if( test[0].equalsIgnoreCase("DISCONNECT")){
					
					peer.disconnect();
					
				}
				
				else if( test[0].equalsIgnoreCase("EXIT")){
					
					peer.disconnect();
					peer.exit();
					
				}
				
				else if( test[0].equalsIgnoreCase( "M" )){
					
					
				}
				
				else if( test[0].equalsIgnoreCase("MX")){
					
				}
				
				else{
					System.out.println("Falsche Eingabe");
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
