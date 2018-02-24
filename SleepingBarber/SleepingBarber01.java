import java.util.*;

class Client{
 private int id;
 private static int courant=0;
 public Client(){
  this.id=courant++;
 }
 public Client(int id){
  this.id=id;
 }
}

// Representation de la salle d'attente

class SalleAttente{
 private LinkedList<Client> list;
 private int maxElements=0;
 
 public SalleAttente(int max){
  list= new LinkedList<Client>();
  maxElements=max;
 }

 synchronized public int ajouter(Client c){
  if (this.nombreElements() < maxElements){
   list.addLast(c);
   notifyAll();
   return 0;
  }
  else {
  return -1;
  }
 }

 synchronized public Client retirer(){
  while (nombreElements()==0) {
   System.out.println("Attente...(barber sleeping)"); // affichage mal place mais comment faire...
   try{
    wait();
   }catch(InterruptedException e){e.printStackTrace();} 
  }
  return list.removeFirst();
 }

 public int nombreElements(){
  return list.size();
 }

 public synchronized void fermer(){
  notifyAll();
 }
}


class Barber extends Thread{
 private SalleAttente s;
 private volatile boolean arret;
 private int nbClientsTraites;

 public Barber(SalleAttente s){
  this.s=s;
  arret=false;
  nbClientsTraites=0;
 }

 public void run(){
  while(!arret){
   s.retirer();
   nbClientsTraites++;
   try{
    System.out.println("The baber is working...");
    Thread.sleep((int)(Math.random() * 2000));

   } catch(InterruptedException e){e.printStackTrace();}  
  }
 }

 public void arreter(){ 
  arret=true;
  System.out.println("Nombre de clients traites = "+nbClientsTraites);
 }
}

class SimulArriveeClient extends Thread{
 private int nbClients;
 private SalleAttente s;
 private int nbClientsPartis;
 public SimulArriveeClient(SalleAttente s, int nbClients){
  this.nbClients=nbClients;
  this.s=s;
  nbClientsPartis=0;
 }
 public void run(){
  for(int i=0; i<nbClients; i++){
   System.out.println("Salle d'attente occupee a ="+s.nombreElements());
   if (s.ajouter(new Client())!=-1)
    System.out.println("un nouveau client est arrive");
   else { 
    System.out.println("salle pleine, un client est repartis");
    nbClientsPartis++;
   }
    try{ Thread.sleep((int)(Math.random() * 2000)); } catch(InterruptedException e){e.printStackTrace();}  
  }
   System.out.println(" Nombre de clients arrives = "+nbClients);
   System.out.println(" Nombre de clients partis  = "+nbClientsPartis);
  }
}

public class SleepingBarber01{

 public static void main(String[] arg){
  // la salle d'attente est geree comme une file
  SalleAttente laSalleAttente=new SalleAttente(5);

  // on lance lethread barber
  Barber b=new Barber(laSalleAttente);
  b.start();
 

  // on lance la simulation de l'arrivee des clients
  SimulArriveeClient simul = new SimulArriveeClient(laSalleAttente,20);
  simul.start();

  // on attend la fin de la simulation pour arreter le coiffeur
  try{
   simul.join();
  }catch(InterruptedException e){e.printStackTrace();}
  // tant que la salle d'attente n est pas vide on n'arrete pas le coiffeur
  while(laSalleAttente.nombreElements()!=0){
   try{Thread.sleep(4000);} catch(InterruptedException e){e.printStackTrace();}  
  }
  b.arreter();
  laSalleAttente.fermer(); // pour pouvoir arreter le baber de son wait final, notez l'usage du modifieur volatile sur le boolean !
 }
}



