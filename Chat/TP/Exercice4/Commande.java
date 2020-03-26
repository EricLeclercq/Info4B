import java.io.*;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public enum Commande implements Serializable {
	CreateChannel("CREATE CHANNEL"),
	SetChannel("SET CHANNEL"),
	SubscribeChannel("SUBSCRIBE CHANNEL"),
	UnsubscribeChannel("UNSUBSCRIBE CHANNEL"),
	List("LIST"),
	Message("");

	public String texte = "";
	
	private Commande(String texte) {
		this.texte = texte;
	}
}
