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
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));     //Liest Konsoleneingaben ein, Befehle sind nicht Groß- oder Kleinschreibungssensitiv
		    
		    String uInput;
			while((uInput = reader.readLine()) != null)
			{
				String[] test = uInput.split(" ");
					
				if(test.length == 3 && test[0].equalsIgnoreCase("CONNECT"))   //Bei korrekter Eingabe wird eine Pokenachricht an den angegebenen Client gesendet
				{
					
				    peer.poke(test[1],Integer.parseInt(test[2]));
				    
				}
				
				else if(test.length == 1 && test[0].equalsIgnoreCase("DISCONNECT"))    //Bei korrekter Eingabe wird die disconnect-Methode aufgerufen und die Liste der bekannten Clients geleert
				{
					
					peer.disconnect(peer.getName(),peer.getIp(),peer.getPort());
					peer.getKnownPeers().clear();
					
				}
				
				else if(test.length == 1 && test[0].equalsIgnoreCase("EXIT"))     //Bei korrekter Eingabe werden die disconnect- und die exit-Methode aufgerufen
				{
					
					peer.disconnect(peer.getName(),peer.getIp(),peer.getPort());
					peer.exit();
					
				}
				
				else if(test.length >= 3 && test[0].equalsIgnoreCase( "M" ))    //Bei korrekter Eingabe wird die Nachricht mit der Methode messageMulti an allen bekannten Clients mit dem angegebenen Namen gesendet
				{
					
					peer.messageMulti( test );
				}
				
				else if(test.length >= 4 && test[0].equalsIgnoreCase("MX"))   //Bei korrekter Eingabe wird die Nachricht an den angegebenen Client gesendet
				{
					
					String[] messageArray = new String[test.length - 3];
					
					
					for(int i = 3; i < test.length; i++){
						messageArray[i-3] = test[i];
					}
					
					String message = String.join(" ", messageArray);
					peer.messageSingle(test[1], Integer.parseInt(test[2]),message);
				}
				else if(test[0].equalsIgnoreCase("LIST"))    //Kontrollkommando, das alle bekannten Peers ausgibt
				{
					peer.printPeers();
				}
				else{
					
					System.out.println("Unbekannter Befehl oder falsches BEfehlsformat");
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
