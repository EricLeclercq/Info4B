import java.io.*;

class SommeEntiers{
  public static void main (String[] arg) throws IOException{
    int somme = 0;
    BufferedReader fichier = new BufferedReader(new FileReader(arg[0]));
    StreamTokenizer lexeme = new StreamTokenizer(fichier);
    while(lexeme.nextToken() != StreamTokenizer.TT_EOF)
   	if (lexeme.ttype==StreamTokenizer.TT_NUMBER)
   	 somme += (int)lexeme.nval;
    System.out.println("La somme vaut : " + somme );
    fichier.close();
  }
}

