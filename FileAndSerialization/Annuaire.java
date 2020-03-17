import java.io.*;
import java.util.*;

/**
 * @author Annabelle Gillet
 */
public class Annuaire {
	public static void main(String[] arg) throws IOException {
		//write();
		read();

	}

	public static void write() {
		// Initialisation des annuaires
		Hashtable<String, Hashtable<String, Personne>> annuaireNom = new Hashtable<String, Hashtable<String, Personne>>();
		Hashtable<String, Hashtable<String, Personne>> annuaireNumero = new Hashtable<String, Hashtable<String, Personne>>();

		// Initialisation des personnes qui vont peupler l'annuaire
		Personne p1 = new Personne("p1", "1");
		Personne p2 = new Personne("p2", "2");

		// Initialisation du sous-annuaire concernant une ville en particulier, en recherchant avec le nom de la personne
		Hashtable<String, Personne> annuaireNomVille1 = new Hashtable<String, Personne>();
		annuaireNomVille1.put("p1", p1);
		annuaireNomVille1.put("p2", p2);
		// Remplissage de l'annuaire principal avec le sous-annuaire de la ville
		annuaireNom.put("ville1", annuaireNomVille1);

		// Initialisation du sous-annuaire concernant une ville en particulier, en recherchant avec le numéro de la personne
		Hashtable<String, Personne> annuaireNumeroVille1 = new Hashtable<String, Personne>();
		annuaireNumeroVille1.put("1", p1);
		annuaireNumeroVille1.put("2", p2);
		// Remplissage de l'annuaire principal avec le sous-annuaire de la ville
		annuaireNumero.put("ville1", annuaireNumeroVille1);

		// Sérialisation de l'annuaire permettant la recherche à partir de la ville puis du nom de la personne
		try {
			FileOutputStream fileOut = new FileOutputStream("annuaireNom.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(annuaireNom);
		}
		catch (Exception e) {
			System.out.println(e);
		}

		// Sérialisation de l'annuaire permettant la recherche à partir de la ville puis du numéro de la personne
		try {
			FileOutputStream fileOut = new FileOutputStream("annuaireNumero.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(annuaireNumero);
		}
		catch (Exception e) {
			System.out.println(e);
		}

		// Vérification du contenu de l'annuaire permettant la recherche à partir de la ville puis du nom de la personne
		System.out.println(annuaireNom.get("ville1").get("p1"));
		System.out.println(annuaireNom.get("ville1").get("p2"));

		// Vérification du contenu de l'annuaire permettant la recherche à partir de la ville puis du numéro de la personne
		System.out.println(annuaireNumero.get("ville1").get("1"));
		System.out.println(annuaireNumero.get("ville1").get("2"));
	}

	public static void read() {

		try {
			FileInputStream fileIn = new FileInputStream("annuaireNom.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Hashtable<String, Hashtable<String, Personne>> annuaireNom = (Hashtable<String, Hashtable<String, Personne>>)in.readObject();
			System.out.println(annuaireNom.get("ville1").get("p1"));
			System.out.println(annuaireNom.get("ville1").get("p2"));
		}
		catch (Exception e) {
			System.out.println(e);
		}

		try {
			FileInputStream fileIn = new FileInputStream("annuaireNumero.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Hashtable<String, Hashtable<String, Personne>> annuaireNumero = (Hashtable<String, Hashtable<String, Personne>>)in.readObject();
			System.out.println(annuaireNumero.get("ville1").get("1"));
			System.out.println(annuaireNumero.get("ville1").get("2"));
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}

/**
 * La classe Personne doit implémenter l'interface Serializable afin que l'annuaire
 * puisse être sérialisé.
 */
class Personne implements Serializable {
	private String nom;
	private String numero;

	public Personne(String nom, String numero) {
		this.nom = nom;
		this.numero = numero;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return nom + " : " + numero;
	}
}
