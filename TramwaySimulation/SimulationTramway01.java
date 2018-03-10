import java.util.concurrent.Semaphore;

public class SimulationTramway01{
 public static void main(String[] args){
 	Ligne l=new Ligne(6);
        // au total 6 troncons pour coder aller et retour
        // t1,tc,t2,t3,tc,t4,t1,tc,t2,t3,tc,t4,...
	l.addTroncon(new Troncon("t1"));
        Troncon tc=new TronconCommun("tc");
        l.addTroncon(tc);
	l.addTroncon(new Troncon("t2"));
 	l.addTroncon(new Troncon("t3"));
        l.addTroncon(tc);
        l.addTroncon(new Troncon("t4"));
 	
 	// on positione la rame 1 sur le troncon 1, etc.
 	Rame r1=new Rame(0,l);
 	Rame r2=new Rame(0,l);
 	r1.start();
 	r2.start();
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
	protected String nom; 
	// private Troncon suivant;
	public Troncon(String nom){this.nom=nom;}
	public String toString(){
	 	return this.nom;
	}
}

class TronconCommun extends Troncon{
 	private Semaphore s;
 	public TronconCommun(String nom){
 		super(nom);
		s=new Semaphore(1);
	}
	public void demandeEntree(){
		System.out.println("Demande entree tc "+Thread.currentThread());
		try{
			s.acquire();
		}catch(InterruptedException e){e.printStackTrace();}
		System.out.println("Entree tc OK "+Thread.currentThread());
	}
	public void sortie(){
		s.release();
		System.out.println("Sortie tc OK "+Thread.currentThread());
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
                
		while(j<=31){
			tronconCourant=l.getTroncon(indiceTronconCourant);
			System.out.println(indiceTronconCourant+"\t"+tronconCourant+"\t"+Thread.currentThread());
			try{
				Thread.sleep((int)(Math.random()*10));
			}catch(InterruptedException e){e.printStackTrace();}
			if(tronconCourant.toString().equals("t1")||tronconCourant.toString().equals("t3")) {
				Troncon tsuivant;
				tsuivant=l.getTroncon(indiceTronconCourant+1);
				((TronconCommun)tsuivant).demandeEntree();
			}
			if(tronconCourant.toString().equals("tc")){
				((TronconCommun)tronconCourant).sortie();
			}
                        indiceTronconCourant++;
			indiceTronconCourant=indiceTronconCourant % 6;
			j++;
		}
		}
}
	
