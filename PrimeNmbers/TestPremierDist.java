class Premier{
 public static boolean isPrime(int n){
  for (int i=2; i<=Math.sqrt(n); i++)
   if (n%i == 0)
   return false;
  return true;
 }
}

class TestIntervalle extends Thread{
 private Distributeur d;
 public TestIntervalle(Distributeur d){
   this.d=d;
  }
 public void run(){
  int i;
  while ((i=d.getNumber())!=-1)
   if (Premier.isPrime(i)==true) System.out.println(i);
 }
}

class Distributeur{
 private int borneSup;
 private int courant=2;
 public Distributeur(int max){
  borneSup=max;
 }
 public int getNumber(){
  if (courant >borneSup) return -1;
  courant++;
  return courant;
 }
}



public class TestPremierDist{
 public static void main(String argv[]) {
  Distributeur d = new Distributeur(50000000);
  Thread t1=new TestIntervalle(d);
  Thread t2=new TestIntervalle(d);
  Thread t3=new TestIntervalle(d);
  Thread t4=new TestIntervalle(d);
  Thread t5=new TestIntervalle(d);
  Thread t6=new TestIntervalle(d);
  t1.start();
  t2.start();
  t3.start();
  t4.start();
  t5.start();
  t6.start();
}
}
