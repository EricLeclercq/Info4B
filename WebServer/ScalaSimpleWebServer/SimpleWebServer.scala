import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.routing.SmallestMailboxPool

import java.net._
import java.io._
import java.util._

import scala.io.Source

/** 
 * Contributeurs : Annabelle Gillet
 *
 * donner deux paramètres au démarrage :
 * 1 - le port d'écoute
 * 2 - le fichier à délivrer
 */
class ServeurMultiClient(port: Int, nomFichier: String) {
	private var arret: Boolean = false;

    Cache.chargerCache(nomFichier)
	
	// Cration du système d'acteurs
	private val system: ActorSystem = ActorSystem.create("SimpleWebServer")
	/* Le router permet d'instancier plusieurs un même type d'acteur, afin de répartir les messages
	entre ces acteurs, afin d'équilibrer la charge.
	Ici, nous avons un ensemble de 5 acteurs de type "ExpediteurFichier", et la stratégie de répartition des messages est "SmallestMailbox", 
	c'est à dire que le message sera envoyé à l'acteur ayant la boîte au lettre la moins remplie.
	D'autres stratégie de répartition existent, comme RoundRobin, qui permet de distribuer les messages en tournant entre les acteurs.
	*/
	private val router = system.actorOf(SmallestMailboxPool(5).props(ExpediteurFichier.props))

	try {
		val listener: ServerSocket = new ServerSocket(port)
		while (!arret) {
			val connexionClient: Socket = listener.accept()
			println("Connection from " + connexionClient.getInetAddress().getHostName() + "\n" + connexionClient);
			
			// Envoi du message à traiter à un des acteurs
			router ! ExpediteurFichier.EnvoyerFichier(connexionClient)
		}
	} catch {
		case e: IOException => e.printStackTrace()
	}

}

/*
 * Classe équivalent à un singleton en Java : toutes les classes qui y accèdent auront affaire à la même instance
 */
object Cache {
	var donneesFichier: String = ""
	var longueurDonnees: Int = 0
	var typeDonnees: String = ""
	
	def chargerCache(nomFichier: String) = {
		if (nomFichier.endsWith(".html") || nomFichier.endsWith(".htm")) {
			typeDonnees = "text/html";
		}
		else {
			typeDonnees = "text/plain";
		}
		
		try {
			val bufferedSource = Source.fromFile(nomFichier)
			// mkString() permet de construire un String à partir d'une collection
			// avec le séparateur indiqué en paramètre
			donneesFichier = bufferedSource.getLines.mkString("\n")
			bufferedSource.close
			
			longueurDonnees = donneesFichier.length()
		} catch {
			case e: IOException => {
				e.printStackTrace()
				System.exit(1)
			}
		}
	}
}
 
object SimpleWebServer {
	def main(args: Array[String]): Unit = {
     	val smc: ServeurMultiClient = new ServeurMultiClient(args(0).toInt, args(1))
	}
}

class ExpediteurFichier extends Actor {
	// Méthode permettant de définir le comportement de l'acteur lorsqu'il reçoit un message
	def receive = {
		// Lorsque le message est du type "EnvoyerFichier"
		case ExpediteurFichier.EnvoyerFichier(connexionClient) => {
			try {
				val os: PrintStream = new PrintStream(connexionClient.getOutputStream())
				val is: BufferedReader = new BufferedReader(new InputStreamReader(connexionClient.getInputStream()))
				var requete: String = is.readLine()
				println(requete)
				// on regarde si le client envoie une requete HTTP
				if (requete.indexOf("HTTP/") != -1) {
					// on passe le reste
					while ({requete = is.readLine; !requete.trim.isEmpty}) {
						println(requete)
					}
					// on envoie le fichier depuis le cache
					os.print("HTTP/1.1 200 OK\r\n");
					val today: Date = new Date();
					os.print("Date: " + today + "\r\n");
					// envoi de la signature du serveur
					os.print("Serveur: SimpleWebServer EL 1.0\r\n");
					os.print("Content-length:" + Cache.longueurDonnees + "\r\n");
					os.print("Content-type:" + Cache.typeDonnees + "\r\n\r\n");
				}
				println(Cache.donneesFichier)
				os.println(Cache.donneesFichier);
				connexionClient.close()
			} catch {
				case e: IOException => {
					e.printStackTrace()
					System.exit(1)
				}
			}
		}
		// Lorsque le message n'est pas reconnu
		case _ => {
			println("Message non reconnu.")
		}
	}
}

/**
 * Classe compagnon, permettant d'avoir l'équivalent des méthodes et variables de classe en Java
 */
object ExpediteurFichier {
	// Classes définissant le type des messages qui vont être reçu par l'acteur
	sealed trait Message
	case class EnvoyerFichier(connexionClient: Socket) extends Message

	// Méthode permettant d'instancier un acteur
    def props() = Props(classOf[ExpediteurFichier])
}


