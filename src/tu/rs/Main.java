package tu.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main {
	


	public static void main(String[] args) {
		
		
		try																														//Try-Block zur �berpr�fung eines syntaktisch korrekten 
		{																														//Argumentes an zweiter Stelle	
		
			if(args.length == 2 && ( Integer.parseInt(args[1]) < 1025 || Integer.parseInt(args[1]) > 65535))					//�berpr�fung ob der angegebene Port innerhalb der 
			{																													//gestatteten Grenze liegt
			
				System.out.println("Bitte w�hlen Sie eine Zahl zwischen 1025 und 65535 an.");
			
			
			}
			else if(args.length != 2)
			{
				System.out.println("Ung�ltige Argumente");
			}
			else
			{
				Peer init = new Peer(args[0],Integer.parseInt(args[1]));
			}	
		}
		catch(NumberFormatException e)
		{
			System.out.println("Bitte geben Sie eine g�ltige Zahl ein");
		}
	}

}
