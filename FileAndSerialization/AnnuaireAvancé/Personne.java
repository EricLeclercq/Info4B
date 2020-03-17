import java.io.*;

/**
 * La classe Personne doit implémenter l'interface Serializable afin que l'annuaire
 * puisse être sérialisé.
 *
 * @author Annabelle Gillet
 */
public class Personne implements Serializable {
	private String nom;
  private String ville;
	private int numero;

	public Personne(String nom, String ville, int numero) {
		this.nom = nom;
    this.ville = ville;
		this.numero = numero;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

  public String getVille() {
		return this.ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return nom + " : " + numero;
	}
}
