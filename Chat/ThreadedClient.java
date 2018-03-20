import java.io.*;
import java.net.*;

public class ThreadedClient {
   static int port = 8080;
   static String ip="127.0.0.1";
   static String pseudo="toto"+(int)(Math.random()*1000);
   static boolean arreter=false;

   public static void main(String[] args) throws Exception {
        if (args.length!=0){ 
		ip=args[0];
		port=Integer.parseInt(args[1]);
                pseudo=args[3];
	}	
        Socket socket = new Socket(ip,port);
        System.out.println("SOCKET = " + socket);

        
        BufferedReader sisr = new BufferedReader(
                               new InputStreamReader(socket.getInputStream()));

        PrintWriter sisw = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),true);
        

	sisw.println(pseudo);
	
        GererSaisie saisie=new GererSaisie(sisw);
	saisie.start();
        
	String str;
        while(arreter!=true) {
           str = sisr.readLine();      
	   System.out.println(str);
        }

        System.out.println("END");     
        //sisw.println("END") ;
        sisr.close();
        sisw.close();
        socket.close();
   }
}

class GererSaisie extends Thread{
   private BufferedReader entreeClavier;
   private PrintWriter pw;

   public GererSaisie(PrintWriter pw){
     entreeClavier = new BufferedReader(new InputStreamReader(System.in));
     this.pw=pw;
   }

   public void run(){
     String str;
     try{
     while(!(str=entreeClavier.readLine()).equals("END")){
      pw.println(str);
     }
     }catch(IOException e){e.printStackTrace();}   
     ThreadedClient.arreter=true;      
   }
}
