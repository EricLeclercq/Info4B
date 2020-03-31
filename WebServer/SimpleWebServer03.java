import java.net.* ;
import java.io.* ;
import java.util.* ;
// donner deux paramètres au démarrage :
// 1 - le port d'écoute
// 2 - le fichier à délivrer

class ServeurMultiClient{
  private String nomDuFichier,donneesDuFichier;
  private String typeDesDonnees;
  private int longueurDonnees;
  private boolean arret;
  private int port;

  public ServeurMultiClient(int port, String nomDuFichier){
    this.port = port;
    this.nomDuFichier = nomDuFichier;
    arret = false;
    this.chargeCache();

    try{
      ServerSocket listener = new ServerSocket (port) ;
      while (!arret) {
        Socket connexionClient = listener.accept () ;
        System.out.println ("Connection from " + connexionClient.getInetAddress().getHostName()+"\n"+connexionClient);
        ExpediteurFichier envoyerLeFichier = new ExpediteurFichier(connexionClient,donneesDuFichier,longueurDonnees,typeDesDonnees);
        envoyerLeFichier.start();
      }
    }catch(IOException e){e.printStackTrace();}
  }

  public void chargeCache(){
    FileReader fichierCourant;
    try{
      fichierCourant = new FileReader(nomDuFichier);
      BufferedReader br = new BufferedReader(fichierCourant);
      // on fixe le type des données pour le décodage par le client
      if (nomDuFichier.endsWith(".html") || nomDuFichier.endsWith(".htm")){
       typeDesDonnees = "text/html";
      }
      else{
       typeDesDonnees = "text/plain";
      }
      String ligneCourante;
      donneesDuFichier ="";
      while ((ligneCourante = br.readLine()) != null){
         donneesDuFichier += ligneCourante + "\n";
       }
      longueurDonnees=donneesDuFichier.length();
     }
     catch(IOException e){e.printStackTrace(); System.exit(1);}
    }

}

public class SimpleWebServer03 {
   public static void main (String[] args) throws IOException{
     int port = Integer.parseInt (args[0]) ;
     ServeurMultiClient smc = new ServeurMultiClient(port, args[1]);
	 }
 }

class ExpediteurFichier extends Thread{
   private Socket connexionClient;
   private String donneesDuFichier, typeDesDonnees;
   private int longueurDonnees;
   public ExpediteurFichier(Socket connexionClient, String donneesDuFichier, int longueurDonnees, String typeDesDonnees){
     this.connexionClient = connexionClient;
     this.donneesDuFichier = donneesDuFichier;
     this.longueurDonnees = longueurDonnees;
     this.typeDesDonnees = typeDesDonnees;
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
        os.print("HTTP/1.1 200 OK\r\n");
        Date today = new Date();
        os.print("Date: "+today+"\r\n");
        // envoi de la signature du serveur
        os.print("Serveur: SimpleWebServer EL 1.0\r\n");
        os.print("Content-length:"+longueurDonnees+"\r\n");
        os.print("Content-type:"+typeDesDonnees+"\r\n\r\n");
      }
      System.out.println(donneesDuFichier);
      os.println(donneesDuFichier);
      connexionClient.close();
    }
    catch(IOException e){e.printStackTrace(); System.exit(1);}
   }
 }
