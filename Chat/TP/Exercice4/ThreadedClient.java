import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public class ThreadedClient {
	static int port = 8080;
	static boolean arreter = false;
	static String pseudo = "pseudo";
	
	// Le client attend comme argument l'adresse du serveur et le pseudo (ex. : java Client 127.0.0.1 pseudo pour l'exécuter)
	public static void main(String[] args) throws Exception {
		pseudo = args[1];
		// 4 - le client ouvre une connexion avec le serveur
		Socket socket = new Socket(args[0], port);
		System.out.println("SOCKET = " + socket);
		/* 5b - A partir du Socket connectant le serveur au client, le client ouvre 2 flux :
		1) un flux entrant (BufferedReader) afin de recevoir ce que le serveur envoie
		2) un flux sortant (ObjectOutputStream) afin d'envoyer des messages au serveur */ 
		BufferedReader sisr = new BufferedReader(
		                       new InputStreamReader(socket.getInputStream()));

		ObjectOutputStream sisw = new ObjectOutputStream(socket.getOutputStream());
		
		// Gestion des messages écrits via le terminal
		GererSaisie saisie=new GererSaisie(sisw);
		saisie.start();
		while (!arreter) {
			/* 7 - Le client attend les messages du serveur. La méthode sisr.ready() permet de vérifier
			si un message est dans le flux, ce qui permet de rendre l'action non bloquante */
			try {
				if (sisr.ready()) {
					String str = sisr.readLine();
					System.out.println(str);
				} 
			} catch (Exception e) {
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e1) {}
			}
		}
		System.out.println("END");     // message de fermeture
		// 9 - Le client envoie un message pour mettre fin à la connexion, qui fera sortir le serveur de son while
		Message message = new Message(ThreadedClient.pseudo, Commande.Message, "", "END"); 
		sisw.writeObject(message);

		// 10b - Le client ferme ses flux
		sisr.close();
		sisw.close();
		socket.close();
	}
}

/**
 * Utiliser un thread pour gérer les entrées clavier du client permet de dissocier les actions
 * d'envoi et de réception de messages. C'est indispenseble dans un contexte de chat multi clients,
 * puisqu'un client ne sait pas s'il sera le prochain à envoyer un message ou non, il doit
 * être capable de gérer les 2 cas.
 */
class GererSaisie extends Thread {
	private BufferedReader entreeClavier;
	private ObjectOutputStream pw;

	public GererSaisie(ObjectOutputStream pw) {
		entreeClavier = new BufferedReader(new InputStreamReader(System.in));
		this.pw = pw;
	}

	public void run() {
		String str;
		try {
			while(!(str = entreeClavier.readLine()).equals("END")) {
				Message message = new Message(ThreadedClient.pseudo, Commande.Message, "", str); 
				if (str.startsWith(Commande.CreateChannel.texte)) {
					message.commande = Commande.CreateChannel;
					message.parametre = str.split(" ")[2];
				} else if (str.startsWith(Commande.SetChannel.texte)) {
					message.commande = Commande.SetChannel;
					message.parametre = str.split(" ")[2];
				} else if (str.startsWith(Commande.SubscribeChannel.texte)) {
					message.commande = Commande.SubscribeChannel;
					message.parametre = str.split(" ")[2];
				} else if (str.startsWith(Commande.UnsubscribeChannel.texte)) {
					message.commande = Commande.UnsubscribeChannel;
					message.parametre = str.split(" ")[2];
				} else if (str.startsWith(Commande.List.texte)) {
					message.commande = Commande.List;
				}
				
				// 8bis - Le client envoie un message au serveur grâce à son PrintWriter
				pw.writeObject(message);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}   
		ThreadedClient.arreter = true;     
	}
}
