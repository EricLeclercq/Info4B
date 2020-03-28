import java.net.* ;
import java.io.* ;
import java.util.* ;
// donner deux param au démarrage :
// 1 - le port d'écoute
// 2 - le fichier à délivrer
public class SimpleWebServer02 {
   public static String donneesDuFichier;
   public static String typeDesDonnees;
   public static int longueurDonnees;

   public static void main (String[] args) throws IOException
   {
     FileReader fichierCourant;
     int port = Integer.parseInt (args[0]) ;

     // on charge le fichier (sorte de cache)
     try{
       fichierCourant = new FileReader(args[1]);
       BufferedReader br = new BufferedReader(fichierCourant);
       // on fixe le type des données pour le décodage par le client
       if (args[0].endsWith(".html") || args[0].endsWith(".htm"))
        typeDesDonnees = "text/html";
      else
        typeDesDonnees = "text/plain";
        String ligneCourante;
        while ((ligneCourante = br.readLine()) != null){
          donneesDuFichier += ligneCourante + "\n";
        }
      }
      catch(IOException e){e.printStackTrace(); System.exit(1);}

     ServerSocket listener = new ServerSocket (port) ;

     while (true) {
	     Socket connexionClient = listener.accept () ;
       System.out.println ("Connection from " + connexionClient.getInetAddress().getHostName()+"\n"+connexionClient);
       EnvoieFichier envoyerLeFichier = new EnvoieFichier(connexionClient);
       envoyerLeFichier.start();
      }
	 }
 }

 class EnvoieFichier extends Thread{
   Socket connexionClient;
   public EnvoieFichier(Socket connexionClient){
     this.connexionClient=connexionClient;
   }

   public void run(){
     // def un flux output
     try{
      PrintStream os = new PrintStream(connexionClient.getOutputStream());
      BufferedReader is = new BufferedReader(new InputStreamReader(connexionClient.getInputStream()));
      String requete = is.readLine();
      System.out.println(requete);
      // on regarde si le client envoie une requete HTTP
      if (requete.indexOf("HTTP/")!=-1){
        // on passe le reste
        while (true){
          String ligne = is.readLine();
          System.out.println(ligne);
          if (ligne.trim().equals("")) break;
        }
      // on envoie le fichier depuis le cache
      os.print("HTTP/1.0 200 OK \r\n");
      Date today = new Date();
      os.print("Date: "+today+"\r\n");
      // envoi de la signature du serveur
      os.print("Serveur: SimpleWebServer EL 1.0\r\n");
      os.print("Content-length:"+SimpleWebServer02.longueurDonnees+"\r\n");
      }
    }
    catch(IOException e){e.printStackTrace(); System.exit(1);}
   }
 }
