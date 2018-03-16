import java.io.*;
import java.net.*;

public class ServeurMC {
   static final int port = 8080;
   static final int maxClients=50;
   static PrintWriter pw[];
   
   public static void main(String[] args) throws Exception {
        int numClient=0;
	pw=new PrintWriter[maxClients];
	ServerSocket s = new ServerSocket(port);
	System.out.println("SOCKET ECOUTE => "+s);
        while (numClient<maxClients){
 	 Socket soc = s.accept();
         ConnexionClient cc=new ConnexionClient(numClient,soc);
	 System.out.println("NOUVELLE CONNEXION - SOCKET =>"+soc);
         numClient++;
	}
   }
  
 }	

class ConnexionClient extends Thread{
 private int id;
 private boolean arret=false;
 private Socket s;
 private BufferedReader sisr;
 private PrintWriter sisw;

 public ConnexionClient(int id, Socket s){
    this.id=id;
    this.s=s;
    // BufferedReader permet de lire par ligne
    try{
    sisr = new BufferedReader(
                               new InputStreamReader(s.getInputStream())
                              );
    // Un PrintWriter possede toutes les operations print classiques.
    // En mode auto-flush, le tampon est vide (flush) a l'appel de println.
    sisw = new PrintWriter( new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream())),true);
    }catch(IOException e){e.printStackTrace();}
    ServeurMC.pw[id]=sisw;
   
    }
  public void run(){
     try{
     while (true) {
           String str = sisr.readLine();          // lecture du message
           if (str.equals("END")) break;
           System.out.println("ECHO = " + str);   // trace locale
           ServeurMC.pw[id].println(str);         // renvoi d'un echo
        }
        sisr.close();
        sisw.close();
        s.close();
    }catch(IOException e){e.printStackTrace();}
  }
 }


