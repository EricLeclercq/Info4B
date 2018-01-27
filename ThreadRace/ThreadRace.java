
class Courreur extends Thread{
 Classement c;
 public Courreur(Classement c){
  this.c=c;
 }
 public void run(){
   for(int i=1; i<=1000; i++){
     System.out.println("Thread "+getName()+" i="+i);
     try{
      sleep((int)(Math.random()*10));
     }catch(InterruptedException e){e.printStackTrace();}
   }
   c.jarrive(getName());
 }
}

class Classement{
 private String tab[];
 private int pos;
 public Classement(int nbe){ tab=new String[nbe]; }
 public void afficher(){ 
  for(int i=0; i<tab.length; i++)
   System.out.println(tab[i]);
 }
 public synchronized void jarrive(String s){
  tab[pos]=s;
  pos++;
 }
}

public class ThreadRace{
 public static void main(String arg[]){
  final int nbc =10;
  Classement c=new Classement(nbc);
  Courreur tc[]=new Courreur[nbc];
  
  for(int i=0; i<nbc; i++)
   tc[i]=new Courreur(c);

  for(int i=0; i<nbc; i++)
   tc[i].start();
  // attente de la fin de tous les courreurs
  for(int i=0; i<nbc; i++){
   try{
     tc[i].join();
   }catch(InterruptedException e){e.printStackTrace();}
  }
  c.afficher();
 }
}


