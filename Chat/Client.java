import java.io.*;
import java.net.*;

public class Client {
   static int port = 8080;

   public static void main(String[] args) throws Exception {
        Socket socket = new Socket(args[0], port);
        System.out.println("SOCKET = " + socket);
        // illustration des capacites bidirectionnelles du flux
        BufferedReader sisr = new BufferedReader(
                               new InputStreamReader(socket.getInputStream()));

        PrintWriter sisw = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),true);
        String str = "bonjour ";
        for (int i = 0; i < 10; i++) {
           sisw.println(str+i);          // envoi d'un message
           str = sisr.readLine();      // lecture de la reponse
	   System.out.println(str);
        }
        System.out.println("END");     // message de fermeture
        //sisw.println("END") ;
        sisr.close();
        sisw.close();
        socket.close();
   }
}
