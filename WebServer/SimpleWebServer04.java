import java.net.* ;
import java.io.* ;
import java.util.* ;
// donner deux paramètres au démarrage :
// 1 - le port d'écoute
// 2 - le fichier à délivrer

class ServeurMultiClient{
  private String nomDuFichier,donneesDuFichier;
  private File documentRoot;
  private String typeDesDonnees;
  private int longueurDonnees;
  private boolean arret;
  private int port;
  public static final String fichierIndex = "index.html";

  public ServeurMultiClient(int port, File racine){
    this.port = port;
    this.nomDuFichier = nomDuFichier;
    arret = false;
    //this.chargeCache();

    try{
      ServerSocket listener = new ServerSocket (port) ;
      while (!arret) {
        Socket connexionClient = listener.accept () ;
        System.out.println ("Connection from " + connexionClient.getInetAddress().getHostName()+"\n"+connexionClient);
        ExpediteurFichier envoyerLeFichier = new ExpediteurFichier(connexionClient,donneesDuFichier,longueurDonnees,typeDesDonnees);
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

      StringTokenizer st = new StringTokenizer(requete);
      commande = st.nextToken();
      if (commande.equals("GET")){
        parametreCommande = st.nextToken();
        if (parametreCommande.endsWith("/"))
         parametreCommande += ServeurMultiClient.fichierIndex;
        //typeMime = decodeType(parametreCommande);
        if (st.hasMoreTokens())
         versionProtocole = st.nextToken();
      }
      System.out.println("decode commande :"+commande + " " + parametreCommande + " " + versionProtocole);

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
