import java.io.*;
import java.util.*;

/**
 * Classe contenant un type paramétré permettant de définir le type que la clé utilisée doit avoir
 * uniquement au moment de l'instanciation.
 * Cela permet d'avoir une classe commune, quelque soit le type utilisé pour la clé, et ne pas avoir
 * à réimplémenter la classe Annuaire à chaque fois qu'un nouveau type est nécessaire.
 *
 * @author Annabelle Gillet
 */
public class Annuaire<T> {
  private Hashtable<String, Hashtable<T, Personne>> annuaire;

  public Annuaire() {
    this.annuaire = new Hashtable<String, Hashtable<T, Personne>>();
  }

  /**
   * Constructeur alternatif permettant de charger un annuaire
   * depuis un fichier qui contient un annuaire qui a été sérialisé.
   */
  public Annuaire(String file) {
    try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			this.annuaire = (Hashtable<String, Hashtable<T, Personne>>)in.readObject();
		}
		catch (Exception e) {
			System.out.println(e);
		}
  }

  /**
   * Méthode permettant d'ajouter une personne dans l'annuaire en fonction de sa ville et de la clé données en paramètres.
   */
  public void addPersonne(String ville, Personne personne, T cle) {
    if (this.annuaire.containsKey(ville)) {
      // Si l'annuaire contient déjà la ville de la personne qu'on souhaite ajouter,
      // on récupère le sous-annuaire de la ville en question pour ajouter la personne,
      // puis on réinjecte le sous-annuaire dans l'annuaire principal
      Hashtable<T, Personne> annuaireVille = this.annuaire.get(ville);
      annuaireVille.put(cle, personne);
      this.annuaire.put(ville, annuaireVille);
    } else {
      // Sinon, on crée le sous-annuaire de la ville, on lui ajoute la personne,
      // puis on ajoute le sous-annuaire dans l'annuaire principal
      Hashtable<T, Personne> annuaireVille = new Hashtable<T, Personne>();
      annuaireVille.put(cle, personne);
      this.annuaire.put(ville, annuaireVille);
    }
  }

  /**
   * Méthode permettant de retrouver une personne dans l'annuaire en fonction de sa ville et de la clé données en paramètres.
   *
   * @return La personne si elle existe, null autrement
   */
  public Personne getPersonne(String ville, T cle) {
    if (this.annuaire.containsKey(ville)) {
      if (this.annuaire.get(ville).containsKey(cle)) {
        return this.annuaire.get(ville).get(cle);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Méthode permettant de sérialiser l'annuaire dans le fichier donné en paramètre.
   */
  public void write(String file) {
    try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.annuaire);
		}
		catch (Exception e) {
			System.out.println(e);
		}
  }
}
