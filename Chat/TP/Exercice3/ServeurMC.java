import java.io.*;
import java.net.*;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public class ServeurMC {
	static int port = 8080;
	static final int maxClients = 50;
	static PrintWriter pw[];
	static int numClient = 0;

	// Pour utiliser un autre port pour le serveur, l'exécuter avec la commande : java ServeurMC 8081
	public static void main(String[] args) throws Exception {
		if (args.length != 0) {
			port = Integer.parseInt(args[0]);
		}
		pw = new PrintWriter[maxClients];
		// 1 - Ouverture du ServerSocket par le serveur
		ServerSocket s = new ServerSocket(port);
		System.out.println("SOCKET ECOUTE CREE => " + s);
		while (numClient < maxClients){
			/* 2 - Attente d'une connexion client (la méthode s.accept() est bloquante 
			tant qu'un client ne se connecte pas) */
			Socket soc = s.accept();
			/* 3 - Pour gérer plusieurs clients simultanément, le serveur attend que les clients se connectent,
			et dédie un thread à chacun d'entre eux afin de le gérer indépendamment des autres clients */
			ConnexionClient cc = new ConnexionClient(numClient, soc);
			System.out.println("NOUVELLE CONNEXION - SOCKET => " + soc);
			numClient++;
			cc.start();
		}
	}
  
 }	

class ConnexionClient extends Thread {
	private int id;
	private String pseudo;
	private boolean arret = false;
	private Socket s;
	private BufferedReader sisr;
	private PrintWriter sisw;

	public ConnexionClient(int id, Socket s) {
		this.id = id;
		this.s = s;

		/* 5a - A partir du Socket connectant le serveur à un client, le serveur ouvre 2 flux :
		1) un flux entrant (BufferedReader) afin de recevoir ce que le client envoie
		2) un flux sortant (PrintWriter) afin d'envoyer des messages au client */ 
		// BufferedReader permet de lire par ligne
		try {
			sisr = new BufferedReader(new InputStreamReader(s.getInputStream()));
			sisw = new PrintWriter( new BufferedWriter(
					    new OutputStreamWriter(s.getOutputStream())), true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		ServeurMC.pw[id] = sisw;
		
		// 6 - Le serveur attend que le client envoie son pseudo
		try {
			pseudo = sisr.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void run(){
		try {
			while (true) {
				/* 8 - Le serveur attend que le client envoie des messages avec le PrintWriter côté client
				que le serveur recevra grâce à son BufferedReader (la méthode sisr.readLine() est bloquante) */
				String str = sisr.readLine();          		// lecture du message
				if (str.equals("END")) break;
				System.out.println("recu de " + id + "," + pseudo + "=> " + str);   	// trace locale
				// 10 - Le serveur envoie le message à tous les clients
				for(int i=0; i<ServeurMC.numClient; i++){
					if (ServeurMC.pw[i] != null && i != id) {
						ServeurMC.pw[i].println(pseudo + "=>" + str);
					}
				}	
			}
			// 12a - Le serveur ferme ses flux
			sisr.close();
			sisw.close();
			s.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}


