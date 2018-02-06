import java.util.*;

// cette version n'utilise pas wait et notify, elle ne respecte pas exactement l'ennonce

/* Atention par defaut LinkedList n'est pas synchronized
 mais la référence n'est pas partagée et les méthodes sur 
 la classe SalleAttente sont synchronized => pas de probleme
 Rq : LinkedList est serializable */

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
  return 0;
  }
  else {
  return -1;
  }
 }

 synchronized public Client retirer(){
  return list.removeFirst();
 }

 public int nombreElements(){
  return list.size();
 }
}

// le fauteuil et l'activite du coiffeur son lies
// on peut déduire l'état de l'un par rapport à l'autre
// => inutile de creer une classe fauteuil

class Barber extends Thread{
 private SalleAttente s;
 private boolean arret;
 private int nbClientsTraites;

 public Barber(SalleAttente s){
  this.s=s;
  arret=false;
  nbClientsTraites=0;
 }

 public void run(){
  while(!arret){
  if (s.nombreElements()>0){
   s.retirer();
   nbClientsTraites++;
   try{
    System.out.println("The baber is working...");
    Thread.sleep((int)(Math.random() * 2000));
   }
   catch(InterruptedException e){e.printStackTrace();}  
  }
  else {
   try{
    System.out.println("The baber is sleeping..."); 
    Thread.sleep((int)(Math.random()*1000));} 
   catch(InterruptedException e){e.printStackTrace();}  
  }
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
    try{ Thread.sleep(400); } catch(InterruptedException e){e.printStackTrace();}  
  }
   System.out.println(" Nombre de clients arrives = "+nbClients);
   System.out.println(" Nombre de clients partis  = "+nbClientsPartis);
  }
}

public class SleepingBarber00{

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
 }
}



