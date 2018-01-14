package tu.rs;

import java.util.concurrent.CopyOnWriteArrayList;

public class PeerTimer implements Runnable{
	private Peer peer;
	
	
	public PeerTimer(Peer peer)
	{
		this.peer = peer;
	}
	
	
	
	
	@Override
	public void run()
	{
		long currentTime  = System.currentTimeMillis() / 1000L;
		peer.pokeAll(new PeerListEntry(peer.getName(),peer.getIp(),peer.getPort()));				//Bekannte Peers werden �ber eiegene Existenz informiert
		for(PeerListEntry entry : peer.getKnownPeers())
		{
			if(Math.abs(currentTime - entry.getLastPoke()) >= 60  )									//Alle bekannten Peers, dessen Poke Zeitpunkt l�nger als eine Minute
			{																						//zur�ckliegt, werden aus der Liste entfernt
				peer.getKnownPeers().remove(entry);
			}
		}
		
	}
	

}
