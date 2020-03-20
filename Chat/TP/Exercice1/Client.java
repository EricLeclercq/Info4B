import java.io.*;
import java.net.*;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public class Client {
	static int port = 8080;
	
	// Le client attend comme argument l'adresse du serveur (ex. : java Client 127.0.0.1 pour l'exécuter)
	public static void main(String[] args) throws Exception {
		// 3 - le client ouvre une connexion avec le serveur
		Socket socket = new Socket(args[0], port);
		System.out.println("SOCKET = " + socket);
		/* 4b - A partir du Socket connectant le serveur au client, le client ouvre 2 flux :
		1) un flux entrant (BufferedReader) afin de recevoir ce que le serveur envoie
		2) un flux sortant (PrintWriter) afin d'envoyer des messages au serveur */ 
		BufferedReader sisr = new BufferedReader(
		                       new InputStreamReader(socket.getInputStream()));

		PrintWriter sisw = new PrintWriter(new BufferedWriter(
		                        new OutputStreamWriter(socket.getOutputStream())),true);
		String str = "bonjour ";
		for (int i = 0; i < 10; i++) {
			// 6 - Le client envoie un message au serveur grâce à son PrintWriter
			sisw.println(str+i);          // envoi d'un message
			// 7 - Le client attend que le serveur lui réponde (la méthode sisr.readLine() est bloquante)
			str = sisr.readLine();      // lecture de la reponse
			System.out.println(str);
		}
		System.out.println("END");     // message de fermeture
		// 9 - Le client envoie un message pour mettre fin à la connexion, qui fera sortir le serveur de son while
		sisw.println("END");

		// 10b - Le client ferme ses flux
		sisr.close();
		sisw.close();
		socket.close();
	}
}
