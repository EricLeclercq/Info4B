import java.io.*;
import java.net.*;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public class Serveur {
   static final int port = 8080;
   public static void main(String[] args) throws Exception {
	// 1 - Ouverture du ServerSocket par le serveur
        ServerSocket s = new ServerSocket(port);
	/* 2 - Attente d'une connexion client (la méthode s.accept() est bloquante 
	tant qu'un client ne se connecte pas) */
        Socket soc = s.accept();
        System.out.println("SOCKET "+s);
	System.out.println("SOCKET "+soc);
	/* 4a - A partir du Socket connectant le serveur au client, le serveur ouvre 2 flux :
	1) un flux entrant (BufferedReader) afin de recevoir ce que le client envoie
	2) un flux sortant (PrintWriter) afin d'envoyer des messages au client */ 

        // BufferedReader permet de lire par ligne
        BufferedReader sisr = new BufferedReader(
                               new InputStreamReader(soc.getInputStream())
                              );
        // Un PrintWriter possede toutes les operations print classiques.
        // En mode auto-flush, le tampon est vide (flush) a l'appel de println.
        PrintWriter sisw = new PrintWriter( new BufferedWriter(
                            new OutputStreamWriter(soc.getOutputStream())),true);
        while (true) {
		/* 5 - Le serveur attend que le client envoie des messages avec le PrintWriter côté client
		que le serveur recevra grâce à son BufferedReader (la méthode sisr.readLine() est bloquante) */
		String str = sisr.readLine();          // lecture du message
		if (str.equals("END")) break;
		System.out.println("ECHO = " + str);   // trace locale
		// 8 - Le serveur envoie la réponse que le client attend
		sisw.println(str);                     // renvoi d'un echo
        }
	// 10a - Le serveur ferme ses flux
        sisr.close();
        sisw.close();
        soc.close();
   }
}
