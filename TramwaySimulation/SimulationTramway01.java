// coeur du programme pour le déplacement des rames sur une ligne 
// composee de troncons
// pas de gestion de concurrence

// classes identifiées :
// Ressource : 
// - la ligne (ressource composite)
// - les troncons partagés par n rames 
// inutile de modéliser les stations
// le troncon tc etant partagé il y aura forcement des problèmes de concurrence
// Threads : les rammes


public class SimulationTramway00{
 public static void main(String[] args){
 	Ligne l=new Ligne(6);
        // au total 6 troncons pour coder aller et retour
        // t1,tc,t2,t3,tc,t4,t1,tc,t2,t3,tc,t4,...
	l.addTroncon(new Troncon("t1"));
        Troncon tc=new Troncon("tc");
        l.addTroncon(tc);
	l.addTroncon(new Troncon("t2"));
 	l.addTroncon(new Troncon("t3"));
        l.addTroncon(tc);
        l.addTroncon(new Troncon("t4"));
 	
 	// on positione la rame 1 sur le troncon 1, etc.
 	Rame r1=new Rame(0,l);
 	//Rame r2=new Rame(2,l);
 	r1.start();
 	//r2.start();
 }
}


class Ligne{
	// on peut laisser le tableau de troncons en public pour faciliter l'intialisation
	// mais pour pouvoir gérer la concurrence il est préférable de définir une méthode
	private Troncon[] t;
	private int courant=0;
        public Ligne(int nbTroncons){
		t=new Troncon[nbTroncons];		
        }
	public synchronized void addTroncon(Troncon ta){
		t[courant]=ta;
		courant++;
	}
	public Troncon getTroncon(int i){
		return t[i];
		}
	}

class Troncon{
	private String nom; 
	// private Troncon suivant;
	public Troncon(String nom){this.nom=nom;}
	public String toString(){
	 	return this.nom;
	}
}
	

class Rame extends Thread{
	private int indiceTronconCourant;
	private Ligne l;
	private Troncon tronconCourant;
	public Rame(int i, Ligne l){
		indiceTronconCourant=i;
		this.l=l;
		tronconCourant=l.getTroncon(indiceTronconCourant);
		}
	public void run(){
		int j=0;
                
		while(j<=60){
			tronconCourant=l.getTroncon(indiceTronconCourant);
			System.out.println(indiceTronconCourant+"\t"+tronconCourant+"\t"+Thread.currentThread());
			try{
				Thread.sleep((int)(Math.random()*10));
			}catch(InterruptedException e){e.printStackTrace();}
                        indiceTronconCourant++;
			indiceTronconCourant=indiceTronconCourant % 6;
			j++;
		}
		}
}
	
