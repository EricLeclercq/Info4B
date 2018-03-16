import java.io.*;
import java.net.*;

public class Serveur {
   static final int port = 8080;
   public static void main(String[] args) throws Exception {
        ServerSocket s = new ServerSocket(port);
        Socket soc = s.accept();
        System.out.println("SOCKET "+s);
	System.out.println("SOCKET "+soc);
        // BufferedReader permet de lire par ligne
        BufferedReader sisr = new BufferedReader(
                               new InputStreamReader(soc.getInputStream())
                              );
        // Un PrintWriter possede toutes les operations print classiques.
        // En mode auto-flush, le tampon est vide (flush) a l'appel de println.
        PrintWriter sisw = new PrintWriter( new BufferedWriter(
                            new OutputStreamWriter(soc.getOutputStream())),true);
        while (true) {
           String str = sisr.readLine();          // lecture du message
           if (str.equals("END")) break;
           System.out.println("ECHO = " + str);   // trace locale
           sisw.println(str);                     // renvoi d'un echo
        }
        sisr.close();
        sisw.close();
        soc.close();
   }
}
