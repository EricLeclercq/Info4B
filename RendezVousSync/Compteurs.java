import java.util.concurrent.*;

public class Compteurs{
 public static void main(String arg[]){
  Sync s=new Sync();
  TCompteur cpt1 = new TCompteur(1,10000,2,s);
  TCompteur cpt2 = new TCompteur(2,10000,2,s);

  cpt1.start();
  cpt2.start();
 }
}

class Sync{
 private volatile int tour;
 private Semaphore s;
 public Sync(){
  s=new Semaphore(1);
  tour=0;
 }
 
 public void aMoi(int id){
  try{
   while (tour+1!=id){; ;}
   s.acquire();
   
  }
  catch(InterruptedException e){e.printStackTrace();}
 }

 public  void passe(){
  synchronized(this){
    s.release();
    tour=(tour+1)%2;
  }
 }
}

class TCompteur extends Thread{
 private int debut;
 private int fin;
 private int inc;
 private int id;
 private Sync s;

 public TCompteur(int debut, int fin, int inc, Sync s){
  this.debut=debut;
  this.fin=fin;
  this.inc=inc;
  this.id=debut;
  this.s=s;
 }

 public void run(){
  for (int i=debut;i<=fin; i=i+inc){
    s.aMoi(id);
	System.out.println("idt="+id+"=>"+i);
    s.passe();
  }
 }
}


