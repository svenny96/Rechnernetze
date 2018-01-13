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
					
				if(test.length == 3 && test[0].equalsIgnoreCase("CONNECT")){
					
				    peer.poke(test[1],Integer.parseInt(test[2]));
				    
				}
				
				else if( test[0].equalsIgnoreCase("DISCONNECT")){
					
					peer.disconnect(peer.getName(),peer.getIp(),peer.getPort());
					peer.getKnownPeers().clear();
					
				}
				
				else if( test[0].equalsIgnoreCase("EXIT")){
					
					peer.disconnect(peer.getName(),peer.getIp(),peer.getPort());
					peer.exit();
					
				}
				
				else if(test.length >= 3 && test[0].equalsIgnoreCase( "M" )){
					
					peer.messageMulti( test );
				}
				
				else if(test.length >= 4 && test[0].equalsIgnoreCase("MX")){
					
					String[] messageArray = new String[test.length - 3];
					
					
					for(int i = 3; i < test.length; i++){
						messageArray[i-3] = test[i];
					}
					
					String message = String.join(" ", messageArray);
					peer.messageSingle(test[1], Integer.parseInt(test[2]),message);
				}
				
				else{
					peer.printPeers();
					System.out.println("Falsche Eingabe");
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
