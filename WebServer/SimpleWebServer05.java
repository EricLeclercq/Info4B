import java.net.* ;
import java.io.* ;
import java.util.* ;
import java.util.concurrent.*;

/** donner deux paramètres au démarrage :
  * 1 - le port d'écoute
  * 2 - la racine du répertoire à publier
  */
public class SimpleWebServer05 {
  public static void main (String[] args) throws IOException{
    ServeurMultiClient smc = ServeurMultiClient.getInstance();
    // arg[1] contient maintenant la racine
    try{
      File documentRoot = new File(args[1]);
      smc.setDocumentRoot(documentRoot);
    }
    catch (Exception e) {
      System.out.println("Document root défini par défaut à \".\".");
    }
    // on vérifie aussi le port
    try{
      int port = Integer.parseInt(args[0]);
      if (port >= 1024 && port <= 65535) {
        smc.setPort(port);
      } else {
        System.out.println("Le port doit être compris entre 1024 et 65 535.");
        System.out.println("Port défini par défaut à 8080.");
      }
    }
    catch (Exception e){
      System.out.println("Port défini par défaut à 8080.");
    }
    smc.demarre();
  }
}

/**
  * La classe ServeurMultiClient est un singleton
  * pour le réaliser on doit garantir que l'instanciation ne génère qu'un seul objet de cette classe
  * il s'agit d'un pattern / patron de classe très utilisé :
  * 1.  on encapsule l'instance en dans un attribut private satic
  * 2.  on fait un constructeur privé
  * 3.  une méthode qui appelle le constructeur et délivre une instance
 */
class ServeurMultiClient{

  private static ServeurMultiClient monInstance = null;

  static  File documentRoot;
  private boolean arret;
  private int port;
  public static final String fichierIndex = "index.html";
  private Routeur routeur;

  /**
   * Constructeur privé de la classe. Cela permet de contrôler son appel depuis l'intérieur de la classe seulement,
   * et de ne créer une nouvelle instance que lorsque c'est souhaité.
   */
  private ServeurMultiClient(){
    System.out.println("Appel du constructuer de la classe ServeurMultiClient.");
    this.port = 8080;
    documentRoot = new File(".");
    arret = true;
    routeur = new Routeur(5);
  }

  public static ServeurMultiClient getInstance(){
    if (monInstance == null) {
      monInstance = new ServeurMultiClient();
    }
    return monInstance;
  }

  /**
   * Modifie le port du serveur. La modification n'est possible que si le serveur n'est pas démarré.
   * Si la modification a pu se faire, la méthode retourne true, sinon elle retourne false.
   */
  public synchronized boolean setPort(int port) {
    if (arret) {
      this.port = port;
      return true;
    }
    return false;
  }

  /**
   * Modifie le document root du serveur. La modification n'est possible que si le serveur n'est pas démarré.
   * Si la modification a pu se faire, la méthode retourne true, sinon elle retourne false.
   */
  public synchronized boolean setDocumentRoot(File documentRoot) {
    if (arret) {
      this.documentRoot = documentRoot;
      return true;
    }
    return false;
  }

  /**
   * Démarre le serveur (il attend les connexions des clients pour leur envoyer le fichier demandé)
   */
  public synchronized void demarre() {
    if (arret) {
      arret = false;

      try{
        ServerSocket listener = new ServerSocket(port) ;
        while (!arret) {
          Socket connexionClient = listener.accept() ;
          System.out.println ("Connection from " + connexionClient.getInetAddress().getHostName()+"\n"+connexionClient);
          routeur.addConnexion(connexionClient);
        }
      }
      catch(IOException e){e.printStackTrace();}
    }
  }

  /**
   * Méthode alternative pour démarrer le serveur, en précisant le port et le document root.
   */
  public synchronized void demarre(int port, File documentRoot){
    this.setPort(port);
    this.setDocumentRoot(documentRoot);
    this.demarre();
  }
}

/**
 * Classe permettant de gérer un ensemble de threads, et de répartir les connexions des clients entre eux.
 * Une Queue permet de stocker les connexions des clients, en attendant qu'un thread soit disponible
 * pour la traiter.
 */
class Routeur {
	private int capacite;
	private ExpediteurFichier[] workers;
	public ConcurrentLinkedQueue<Socket> queue;
	
	public Routeur(int capacite) {
		this.capacite = capacite;
		this.workers = new ExpediteurFichier[capacite];
		this.queue = new ConcurrentLinkedQueue<>();
		for (int i = 0; i < capacite; i++) {
			this.workers[i] = new ExpediteurFichier(i, this);
			this.workers[i].start();
		}
	}
	
	public void addConnexion(Socket connexionClient) {
		synchronized(queue) {
			queue.add(connexionClient);
			queue.notify();
		}
	}
	
	public Socket getConnexion() {
		synchronized(queue) {
			while (queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return queue.poll();
		}
	}
}

class ExpediteurFichier extends Thread{
	private Routeur routeur;
	private boolean arret;
	private int id;
	
  public ExpediteurFichier(int id, Routeur routeur) {
  	this.id = id;
  	this.routeur = routeur;
  	this.arret = false;
  }
	
	public void run() {
		while (!arret) {
			Socket connexionClient = routeur.getConnexion();
			nouvelleConnexion(connexionClient);
		}
	}
	
  private void nouvelleConnexion(Socket connexionClient) {
  	System.out.println("Thread " + id + " prend en charge la connexion.");
    String commande;
    String parametreCommande = "";
    String versionProtocole ="";
    // def un flux output
    try{
      PrintStream os = new PrintStream(connexionClient.getOutputStream());
      BufferedReader is = new BufferedReader(new InputStreamReader(connexionClient.getInputStream()));
      String requete = is.readLine();

      System.out.println("reçu:" + requete);

      StringTokenizer st = new StringTokenizer(requete);
      commande = st.nextToken();
      if (commande.equals("GET")){
          parametreCommande = st.nextToken();
        if (parametreCommande.endsWith("/"))
          parametreCommande += ServeurMultiClient.fichierIndex;
        if (st.hasMoreTokens())
          versionProtocole = st.nextToken();
        System.out.println("decode commande :"+commande + " " + parametreCommande + " " + versionProtocole);
        // on passe les éléments inutiles
        while ((commande = is.readLine()) != null){
          //System.out.println(ligne);
          if (commande.trim().equals("")) break;
        }
        executeGet(parametreCommande, os);
      }
      else
      {
        os.print("HTTP/1.1 501 Not Implemented\r\n");
        Date today = new Date();
        os.print("Date: "+today+"\r\n");
        // envoi de la signature du serveur
        os.print("Serveur: SimpleWebServer EL 1.0\r\n");
        os.print("Content-type:text/plain"+"\r\n\r\n");
      }
      os.close();
      connexionClient.close();
    }
    catch(IOException e){e.printStackTrace(); System.exit(1);}
  }

  void executeGet(String parametreFichier, PrintStream os){
    System.out.println(ServeurMultiClient.documentRoot + "fichier=" + parametreFichier.substring(1,parametreFichier.length()));
    String nomFichier = ServeurMultiClient.documentRoot + parametreFichier.substring(1,parametreFichier.length());
    Fichier fichier = new Fichier(nomFichier);

    os.print("HTTP/1.1 200 OK\r\n");
    Date today = new Date();
    os.print("Date: " + today + "\r\n");
    os.print("Serveur: SimpleWebServer EL 1.0\r\n");
    os.print("Content-length:" + fichier.longueurDonnees + "\r\n");
    os.print("Content-type:" + fichier.typeDonnees + "\r\n\r\n");
    //System.out.println(fichier.donnees);
    os.write(fichier.donnees, 0, fichier.longueurDonnees);
  }
}

/**
 * Classe permettant de gérer un fichier à envoyer.
 */
class Fichier {
   String nom;
   int longueurDonnees;
   String typeDonnees;
   byte[] donnees;

   public Fichier(String nom) {
     this.nom = nom;
     lireFichier();
     this.longueurDonnees = this.donnees.length;
     this.typeDonnees = decodeType();
   }

   private void lireFichier() {
     try {
       File fichierCourant = new File(ServeurMultiClient.documentRoot, nom.substring(1, nom.length()));
       FileInputStream fis = new FileInputStream(fichierCourant);
       this.donnees = new byte[(int)fichierCourant.length()];
       fis.read(this.donnees);
       fis.close();
     } catch(IOException e) {
       e.printStackTrace();
       System.exit(1);
     }
   }

   private String decodeType(){
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
}
