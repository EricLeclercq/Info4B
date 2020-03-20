import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public class Client {
	static int port = 8080;
	
	// Le client attend comme argument l'adresse du serveur et le pseudo (ex. : java Client 127.0.0.1 pseudo pour l'exécuter)
	public static void main(String[] args) throws Exception {
		String pseudo = args[1];
		// 4 - le client ouvre une connexion avec le serveur
		Socket socket = new Socket(args[0], port);
		System.out.println("SOCKET = " + socket);
		/* 5b - A partir du Socket connectant le serveur au client, le client ouvre 2 flux :
		1) un flux entrant (BufferedReader) afin de recevoir ce que le serveur envoie
		2) un flux sortant (PrintWriter) afin d'envoyer des messages au serveur */ 
		BufferedReader sisr = new BufferedReader(
		                       new InputStreamReader(socket.getInputStream()));

		PrintWriter sisw = new PrintWriter(new BufferedWriter(
		                        new OutputStreamWriter(socket.getOutputStream())),true);
		
		// 7 - Le client envoie son pseudo au serveur
		sisw.println(pseudo);
		
		// Gestion des messages écrits via le terminal
		Scanner in = new Scanner(System.in);
		String message = in.nextLine();
		while (!"END".equals(message)) {
			// 9 - Le client envoie un message au serveur grâce à son PrintWriter
			sisw.println(message);          // envoi d'un message
			// 10 - Le client attend que le serveur lui réponde (la méthode sisr.readLine() est bloquante)
			String str = sisr.readLine();      // lecture de la reponse
			System.out.println(str);
			message = in.nextLine();
		}
		System.out.println("END");     // message de fermeture
		// 12 - Le client envoie un message pour mettre fin à la connexion, qui fera sortir le serveur de son while
		sisw.println("END");

		// 13b - Le client ferme ses flux
		sisr.close();
		sisw.close();
		socket.close();
	}
}
