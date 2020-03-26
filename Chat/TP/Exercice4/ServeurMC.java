import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public class ServeurMC {
	static int port = 8080;
	static final int maxClients = 50;
	static PrintWriter pw[];
	static int numClient = 0;
	static Hashtable<String, Canal> canaux = new Hashtable<>();

	// Pour utiliser un autre port pour le serveur, l'exécuter avec la commande : java ServeurMC 8081
	public static void main(String[] args) throws Exception {
		if (args.length != 0) {
			port = Integer.parseInt(args[0]);
		}
		pw = new PrintWriter[maxClients];
		Canal general = new Canal("Général");
		canaux.put("Général", general);
		
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
	private boolean arret = false;
	private Socket s;
	private ObjectInputStream sisr;
	private PrintWriter sisw;
	private Canal canal;

	public ConnexionClient(int id, Socket s) {
		this.id = id;
		this.s = s;
		canal = ServeurMC.canaux.get("Général");
		canal.subscribe(id);

		/* 5a - A partir du Socket connectant le serveur à un client, le serveur ouvre 2 flux :
		1) un flux entrant (ObjectInputStream) afin de recevoir ce que le client envoie
		2) un flux sortant (PrintWriter) afin d'envoyer des messages au client */ 
		// BufferedReader permet de lire par ligne
		try {
			sisr = new ObjectInputStream(s.getInputStream());
			sisw = new PrintWriter( new BufferedWriter(
					    new OutputStreamWriter(s.getOutputStream())), true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		ServeurMC.pw[id] = sisw;
	}

	public void run(){
		try {
			while (true) {
				/* 6 - Le serveur attend que le client envoie des messages avec le PrintWriter côté client
				que le serveur recevra grâce à son BufferedReader (la méthode sisr.readObject() est bloquante) */
				Message message = (Message)sisr.readObject();          		// lecture du message
				if (message.texte.equals("END")) break;
				System.out.println("recu de " + id + "," + message.pseudo + "=> " + message.texte);   	// trace locale
				// 8 - Le serveur traite le message
				if (message.commande == Commande.CreateChannel) {
					Canal nouveauCanal = new Canal(message.parametre);
					ServeurMC.canaux.put(message.parametre, nouveauCanal);
				} else if (message.commande == Commande.SetChannel) {
					if (ServeurMC.canaux.containsKey(message.parametre)) {
						canal = ServeurMC.canaux.get(message.parametre);
					} else {
						sisw.println("Canal " + message.parametre + " inexistant.");
					}
				} else if (message.commande == Commande.SubscribeChannel) {
					if (ServeurMC.canaux.containsKey(message.parametre)) {
						ServeurMC.canaux.get(message.parametre).subscribe(id);
					} else {
						sisw.println("Canal " + message.parametre + " inexistant.");
					}
				} else if (message.commande == Commande.UnsubscribeChannel) {
					if (ServeurMC.canaux.containsKey(message.parametre)) {
						ServeurMC.canaux.get(message.parametre).unsubscribe(id);
					} else {
						sisw.println("Canal " + message.parametre + " inexistant.");
					}
				} else if (message.commande == Commande.List) {
					for (String nom : ServeurMC.canaux.keySet()) {
						sisw.println(nom);
					}
				} else {
					canal.sendMessage(message.pseudo, id, message.texte);
				}

			}
			// 10a - Le serveur ferme ses flux
			sisr.close();
			sisw.close();
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

class Canal {
	private String nom;
	private List<Integer> utilisateurs;
	
	public Canal(String nom) {
		this.nom = nom;
		utilisateurs = new ArrayList<Integer>();
	}
	
	public void subscribe(int id) {
		utilisateurs.add(id);
	}
	
	public void unsubscribe(int id) {
		utilisateurs.remove((Object)id);
	}
	
	public void sendMessage(String pseudo, int auteur, String message) {
		for (int id : utilisateurs) {
			if (id != auteur) {
				ServeurMC.pw[id].println(nom + ":" + pseudo + "=>" + message);
			}
		}
	}
}


