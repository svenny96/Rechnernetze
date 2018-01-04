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
				String[] test = uInput.split(",");
				
				peer.connect(test[0], Integer.parseInt(test[1]));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
