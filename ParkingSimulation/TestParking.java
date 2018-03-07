class Parking{
 private PorteSortie PS[];
 private PorteEntree PE[];
 private int nbPE, nbPS;
 private int nbPlaces, nbPlacesLibres, nbPlacesOccupees;

 public int nbPlacesLibres(){return nbPlacesLibres;}

 public Parking(int nbPE, int nbPS, int places){
  this.nbPE=nbPE;
  this.nbPS=nbPS;
  nbPlaces=places;
  nbPlacesLibres=nbPlaces;
  nbPlacesOccupees=0;
  // initialisation PE
  PE=new PorteEntree[nbPE];
  for (int i=0; i<nbPE; i++){PE[i]=new PorteEntree();}
  // initialisation PS
  PS=new PorteSortie[nbPS];
  for (int i=0; i<nbPS; i++){PS[i]=new PorteSortie();}
 }

 synchronized int validerEntree(){
  if (nbPlacesLibres==0) return -1;
  nbPlacesLibres--;
  nbPlacesOccupees++;
  notifyAll(); 
  return 0;
 }

 synchronized int validerSortie(){
  if (nbPlacesOccupees<=0) return -1;
  nbPlacesLibres++;
  nbPlacesOccupees--;
  notifyAll();
  return 0;
 }

 public synchronized void demandeEntree(int i){
  while (validerEntree()==-1) {
   System.out.println("Attente entrée -> "+i);
   try{ 
    wait();
   }catch(InterruptedException e){e.printStackTrace();}
  }
   PE[i].entreeVoiture();
 }

 public synchronized void demandeSortie(int i){
  while (validerSortie()==-1) {
   System.out.println("Attente sortie -> "+i);
   try{
    wait();
   }catch(InterruptedException e){e.printStackTrace();}
  }
  // PS[i].ouvrir();
  // PS[i].fermer();
  // mauvaise solution, preferer. Pourquoi ?
   PS[i].sortieVoiture();
 }
 
 public synchronized void attenteEvt(){
   try{
    wait();
   }catch(InterruptedException e){e.printStackTrace();}
 } 
 
}


class PorteSortie{
 private boolean ouvert=false;
 
 private void ouvrir(){ouvert=true;}
 private void fermer(){ouvert=false;}
 
 public synchronized void sortieVoiture(){
   this.ouvrir();
   try{
    Thread.sleep((int)(Math.random()*5));
   }catch(InterruptedException e){e.printStackTrace();}
  this.fermer();
  }
}

class PorteEntree{
 private boolean ouvert=false;
 
 private void ouvrir(){ouvert=true;}
 private void fermer(){ouvert=false;}
 
 public synchronized void entreeVoiture(){
  this.ouvrir();
  try{
   Thread.sleep((int)(Math.random()*5));
  } catch(InterruptedException e){e.printStackTrace();}
  this.fermer();
 }
}


class Afficheur extends Thread{
 private boolean continuer=true;
 private Parking p;
 public Afficheur(Parking p){
  this.p=p;
 }
 public void run(){
  while(continuer){
   System.out.println("Libre->"+p.nbPlacesLibres());
   p.attenteEvt();
  }
 }
}

class SimulationArrivee extends Thread{
 private int nbVoitures;
 private Parking p;
 private int nbPE;
 public SimulationArrivee(int nb, Parking p, int nbPE){
  nbVoitures=nb;
  this.nbPE=nbPE;
  this.p=p;
 }
 public void run(){
  for(int i=1;i<=nbVoitures;i++){
  try{
   Thread.sleep((int)(Math.random()*40));
  }  catch(InterruptedException e){e.printStackTrace();}
  p.demandeEntree((int)(Math.random()*nbPE));			 	 
  }
 }
}

class SimulationSortie extends Thread{
 private int nbVoitures;
 private Parking p;
 private int nbPS;
 public SimulationSortie(int nb, Parking p, int nbPS){
  nbVoitures=nb;
  this.nbPS=nbPS;
  this.p=p;
 }
 public void run(){
  for(int i=1;i<=nbVoitures;i++){
   try{
    Thread.sleep((int)(Math.random()*30));
   }catch(InterruptedException e){e.printStackTrace();}
   p.demandeSortie((int)(Math.random()*nbPS));	
  }
 }
}


public class TestParking{
 public static void main(String arg[]){
  int nbPE=2;
  int nbPS=5;
  int nbPlaces=100;
  int nbVehiculesSimul=2000;
  Parking p=new Parking(nbPE,nbPS,nbPlaces);
  
  Afficheur a=new Afficheur(p);
  a.start();
  
  SimulationArrivee sa1=new SimulationArrivee(nbVehiculesSimul/2,p,nbPE);
  SimulationArrivee sa2=new SimulationArrivee(nbVehiculesSimul/2,p,nbPE);

  sa1.start(); 
  sa2.start();
	
  SimulationSortie ss=new SimulationSortie(nbVehiculesSimul,p,nbPS);
  ss.start();
	}
}


