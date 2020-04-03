import java.net.* ;
import java.io.* ;
import java.util.* ;
// donner deux paramètres au démarrage :
// 1 - le port d'écoute
// 2 - le fichier à délivrer

class ServeurMultiClient{
  private String nomDuFichier,donneesDuFichier;
  static  File documentRoot;
  private String typeDesDonnees;
  private int longueurDonnees;
  private boolean arret;
  private int port;
  public static final String fichierIndex = "index.html";

  public ServeurMultiClient(int port, File racine){
    this.port = port;
    this.nomDuFichier = nomDuFichier;
    documentRoot = racine;
    arret = false;
    //this.chargeCache();

    try{
      ServerSocket listener = new ServerSocket (port) ;
      while (!arret) {
        Socket connexionClient = listener.accept () ;
        System.out.println ("Connection from " + connexionClient.getInetAddress().getHostName()+"\n"+connexionClient);
        ExpediteurFichier envoyerLeFichier = new ExpediteurFichier(connexionClient, donneesDuFichier,longueurDonnees,typeDesDonnees);
        envoyerLeFichier.start();
      }
    }
    catch(IOException e){e.printStackTrace();}
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

public class SimpleWebServer04 {
   public static void main (String[] args) throws IOException{
     int port;
     File documentRoot;
     // arg[1] contient maintenant la racine
     try{
       documentRoot = new File(args[1]);
     }
     catch (Exception e) {
       documentRoot = new File(".");
       e.printStackTrace();
     }
     // on vérifie aussi le port
     try{
       port = Integer.parseInt (args[0]);
       if (port <1024 || port > 65535 ) port = 8080;
     }
     catch (Exception e){ port = 8080; }
     ServeurMultiClient smc = new ServeurMultiClient(port, documentRoot);
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

   private String decodeType(String nom){
     String type;
     if (nom.endsWith(".html") || nom.endsWith(".htm"))
      type = "text/html";
     else if (nom.endsWith(".txt") || nom.endsWith(".java"))
      type = "text/plain";
     else if (nom.endsWith(".gif"))
      type = "image/gif";
     else if (nom.endsWith(".jpg") || nom.endsWith(".jpeg"))
      type = "image/jpeg";
     else type = "text/plain";
     return type;
   }

   public void run(){
     String commande;
     String parametreCommande = "";
     String typeMime;
     String versionProtocole ="";
     // def un flux output
     try{
      PrintStream os = new PrintStream(connexionClient.getOutputStream());
      BufferedReader is = new BufferedReader(new InputStreamReader(connexionClient.getInputStream()));
      String requete = is.readLine();

      System.out.println(requete);

      StringTokenizer st = new StringTokenizer(requete);
      commande = st.nextToken();
      if (commande.equals("GET")){
        parametreCommande = st.nextToken();
        if (parametreCommande.endsWith("/"))
         parametreCommande += ServeurMultiClient.fichierIndex;
        typeDesDonnees = decodeType(parametreCommande);
        if (st.hasMoreTokens())
         versionProtocole = st.nextToken();
         System.out.println("decode commande :"+commande + " " + parametreCommande + " " + versionProtocole
         + " "+ typeDesDonnees);
         // on passe les éléments inutiles
         while ((commande = is.readLine()) != null){
          //System.out.println(ligne);
          if (commande.trim().equals("")) break;
         }
        executeGet(parametreCommande, typeDesDonnees, os);
      }
      else
      {
        os.print("HTTP/1.1 501 Not Implemented\r\n");
        Date today = new Date();
        os.print("Date: "+today+"\r\n");
        // envoi de la signature du serveur
        os.print("Serveur: SimpleWebServer EL 1.0\r\n");
        os.print("Content-type:"+typeDesDonnees+"\r\n\r\n");
      }
    }
    catch(IOException e){e.printStackTrace(); System.exit(1);}
   }

 void executeGet(String fichier, String typeDesDonnees, PrintStream os){
    try{
     System.out.println(ServeurMultiClient.documentRoot + "fichier=" + fichier.substring(1,fichier.length()));
     File fichierCourant = new File(ServeurMultiClient.documentRoot,fichier.substring(1,fichier.length()));
     FileInputStream fis = new FileInputStream(fichierCourant);
     byte[] donneesDuFichier = new byte[(int)fichierCourant.length()];
     fis.read(donneesDuFichier);
     fis.close();

      os.print("HTTP/1.1 200 OK\r\n");
      Date today = new Date();
      os.print("Date: " + today + "\r\n");
      // envoi de la signature du serveur
      os.print("Serveur: SimpleWebServer EL 1.0\r\n");
      os.print("Content-length:" + donneesDuFichier.length + "\r\n");
      os.print("Content-type:" + typeDesDonnees + "\r\n\r\n");

      //System.out.println(donneesDuFichier);
      os.write(donneesDuFichier, 0, donneesDuFichier.length);
      connexionClient.close();
    }
    catch(IOException e){e.printStackTrace(); System.exit(1);}
   }
 }
