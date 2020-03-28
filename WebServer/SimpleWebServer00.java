import java.net.* ;
import java.io.* ;
import java.util.* ;
// donner deux param au démarrage :
// 1 - le port d'écoute
// 2 - le fichier à délivrer
public class SimpleWebServer00 {
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
       System.out.println ("Connection from " + connexionClient.getInetAddress().getHostName()+"\n"+connexionClient) ;
      }
	 }
 }
