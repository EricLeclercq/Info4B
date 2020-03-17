/**
 * @author Annabelle Gillet
 */
public class Serialization {
  public static void main(String[] arg) {
    /* A commenter/décommenter en fonction de l'action voulue :
     - write() : peuple un annuaire de recherche par nom et un annuaire de recherche par numéro, puis les sérialise
     - read() : désérialise l'annuaire de recherche par nom et l'annuaire de recherche par numéro. La méthode write() doit avoir été exécutée au préalable.
    */
		write();
		//read();

	}

  private static void write() {
    System.out.println("Exécution de la méthode write.");
    // Création des annuaires permettant la recherche par nom ou par numéro
    Annuaire<String> annuaireNom = new Annuaire<>();
    Annuaire<Integer> annuaireNumero = new Annuaire<>();

    // Instanciation des personnes à ajouter dans l'annuaire
    Personne p1 = new Personne("p1", "ville1", 1);
		Personne p2 = new Personne("p2", "ville1", 2);

    // Ajout des personnes dans les annuaires
    annuaireNom.addPersonne(p1.getVille(), p1, p1.getNom());
    annuaireNom.addPersonne(p2.getVille(), p2, p2.getNom());
    annuaireNumero.addPersonne(p1.getVille(), p1, p1.getNumero());
    annuaireNumero.addPersonne(p2.getVille(), p2, p2.getNumero());

    // Vérification que les personnes sont bien dans les annuaires
    System.out.println("Annuaire permettant la recherche par nom :");
    System.out.println(annuaireNom.getPersonne(p1.getVille(), p1.getNom()).toString());
    System.out.println(annuaireNom.getPersonne(p2.getVille(), p2.getNom()).toString());
    System.out.println("Annuaire permettant la recherche par numéro :");
    System.out.println(annuaireNumero.getPersonne(p1.getVille(), p1.getNumero()).toString());
    System.out.println(annuaireNumero.getPersonne(p2.getVille(), p2.getNumero()).toString());

    // Sérialisation des annuaires
    annuaireNom.write("annuaireNom.ser");
    annuaireNumero.write("annuaireNumero.ser");
  }

  private static void read() {
    System.out.println("Exécution de la méthode read.");
    // Récupération des annuaires
    Annuaire<String> annuaireNom = new Annuaire<>("annuaireNom.ser");
    Annuaire<Integer> annuaireNumero = new Annuaire<>("annuaireNumero.ser");

    // Vérification du contenu des annuaires
    System.out.println("Annuaire permettant la recherche par nom :");
    System.out.println(annuaireNom.getPersonne("ville1", "p1").toString());
    System.out.println(annuaireNom.getPersonne("ville1", "p2").toString());
    System.out.println("Annuaire permettant la recherche par numéro :");
    System.out.println(annuaireNumero.getPersonne("ville1", 1).toString());
    System.out.println(annuaireNumero.getPersonne("ville1", 2).toString());
  }
}
