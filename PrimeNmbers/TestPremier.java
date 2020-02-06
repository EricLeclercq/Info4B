
class Premier{
 public static boolean isPrime(int n){
  for (int i=2; i<=Math.sqrt(n); i++)
   if (n%i == 0)
   return false;
  return true;
 }
}

class TestIntervalle extends Thread{
 private int borneInf, borneSup;
 public TestIntervalle(int borneInf, int borneSup){
  this.borneInf=borneInf;
  this.borneSup=borneSup;
 }
 public void run(){
  for (int i=borneInf; i<borneSup; i++)
   if (Premier.isPrime(i)==true) System.out.println(i);
 }
}

/* 1- utiliser htop et remarquez la faible utilisation des coeurs de CPU ainsi
      que l'importance des E/S (IO) due aux affichages (X)
   2- en fin d'execution, il ne reste plus qu'un thread actif (celui qui a le 
      plus de diviseurs à tester 
   Conclusion : ce programme a du mal a exploiter les ressources de la machine,
   il faut trouver un autre modèle : x threads venant chercher le travail à 
   faire auprès d'un distributeur.
*/
public class TestPremier{
 public static void main(String argv[]) {
  //Thread t1=new TestIntervalle(2,50000000);
  Thread t1=new TestIntervalle(2,25000000);
  Thread t2=new TestIntervalle(25000000,50000000);
  t1.start();
  t2.start();
}
}

/* (X) la commande time java TestPremier donne :
real	1m3.806s
user	1m31.544s
sys	0m7.432s
on constate que le temps des E/S n'est pas négligeable et que le temps real et proch
du temps user (temps CPU cumulé de chaque coeur)
*/

