import java.io.*;
import java.net.*;

public class ServeurMC {
   static int port = 8080;
   static final int maxClients=50;
   static PrintWriter pw[];
   static int numClient=0;
   public static void main(String[] args) throws Exception {
        if (args.length!=0) port=Integer.parseInt(args[0]);
	pw=new PrintWriter[maxClients];
	ServerSocket s = new ServerSocket(port);
	System.out.println("SOCKET ECOUTE CREE => "+s);
        while (numClient<maxClients){
 	 Socket soc = s.accept();
         ConnexionClient cc=new ConnexionClient(numClient,soc);
	 System.out.println("NOUVELLE CONNEXION - SOCKET =>"+soc);
         numClient++;
         cc.start();
	}
   }
  
 }	

class ConnexionClient extends Thread{
 private int id;
 private String pseudo;
 private boolean arret=false;
 private Socket s;
 private BufferedReader sisr;
 private PrintWriter sisw;

 public ConnexionClient(int id, Socket s){
    this.id=id;
    this.s=s;
    // BufferedReader permet de lire par ligne
    try{
    sisr = new BufferedReader(new InputStreamReader(s.getInputStream()));
    sisw = new PrintWriter( new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream())),true);
    }catch(IOException e){e.printStackTrace();}
    ServeurMC.pw[id]=sisw;
    // le client envoie son pseudo en premier
    try{
     pseudo=sisr.readLine();
    }catch(IOException e){e.printStackTrace();}
    }
  public void run(){
     try{
     while (true) {
           String str = sisr.readLine();          		// lecture du message
           if (str.equals("END")) break;
           System.out.println("recu de "+id+","+pseudo+"=> " + str);   	// trace locale
           // on envoi a tous
           for(int i=0; i<ServeurMC.numClient; i++){
             if (ServeurMC.pw[i]!=null && i!=id)
              ServeurMC.pw[i].println(pseudo+"=>"+str); }	// envoi à tous
        }
        sisr.close();
        sisw.close();
        s.close();
    }catch(IOException e){e.printStackTrace();}
  }
 }


