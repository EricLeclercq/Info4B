public class Baboons {
	public static void main (String[] args) {
		int nbBaboons = 100;
		try {
			nbBaboons = Integer.parseInt(args[0]);
		} catch (Exception e) {}
		
		LianeSemaphore liane = new LianeSemaphore(5);
		
		Baboon[] baboons = new Baboon[nbBaboons];
		for (int i = 0; i < nbBaboons; i++) {
			int sens = 1;
			if (Math.random() >= 0.5) {
				sens = 2;
			}
			baboons[i] = new Baboon(liane, sens);
			baboons[i].start();
			try {
				Thread.sleep((int) (Math.random() * 250));
			} catch (InterruptedException e) {}
		}
		
		// On attend que tous les singes aient traversé
		for (int i = 0; i < nbBaboons; i++) {
			try {
				baboons[i].join();
			} catch (InterruptedException e) {}
		}
	}
}

class Baboon extends Thread {
	private int sens;
	private LianeSemaphore liane;

	public Baboon(LianeSemaphore liane, int sens) {
		this.liane = liane;
		this.sens = sens;
	}
	
	public void run() {
		liane.prendre(sens);
		try {
			// Le temps que le singe se mette en position
			sleep((int) (Math.random() * 500));
		} catch (InterruptedException e) {}
		liane.traverser();
		liane.laisser();
	}
}

class LianeSemaphore {
	private int nbPlacesTotal;
	private int nbPlacesRestantes;
	/* Permet de connaître le sens de la liane
	0 = indéterminé
	1 = A -> B
	2 = B -> A
	*/
	private int sens;
	private Boolean enMouvement;
	private boolean sens1EnAttente;
	private boolean sens2EnAttente;
	private int nbTraverse;
	
	public LianeSemaphore(int nbPlaces) {
		this.nbPlacesTotal = nbPlaces;
		this.nbPlacesRestantes = nbPlaces;
		this.sens = 0;
		this.enMouvement = false;
		this.sens1EnAttente = false;
		this.sens2EnAttente = false;
		this.nbTraverse = 0;
	}
	
	public synchronized void prendre(int sens) {
		// Tant que la liane est en mouvement, qu'il n'y a plus de place ou que le sens n'est pas bon, on attend
		while (this.enMouvement || this.nbPlacesRestantes <= 0 || (this.sens != 0 && this.sens != sens)) {
			if (sens == 1) {
				sens1EnAttente = true;
			} else {
				sens2EnAttente = true;
			}
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		
		// On place la liane si ce n'est pas déjà fait
		if (this.sens == 0) {
			this.sens = sens;
		}
		
		// On prend une place sur la liane
		this.nbPlacesRestantes--;
	}
	
	public synchronized void traverser() {
		this.enMouvement = true;
		this.nbTraverse++;
		if (this.nbTraverse >= (this.nbPlacesTotal - this.nbPlacesRestantes)) {
			System.out.println((this.nbPlacesTotal - this.nbPlacesRestantes) + " singes ont traversé dans le sens " + this.sens);
			this.notifyAll();
		} else {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
	}
	
	public synchronized void laisser() {
		this.nbPlacesRestantes++;
		if (this.nbPlacesRestantes >= this.nbPlacesTotal) {
			this.enMouvement = false;
			this.nbTraverse = 0;
			if (this.sens == 1 && this.sens2EnAttente) {
				this.sens = 2;
				this.sens2EnAttente = false;
				System.out.println("Nouveau sens de la liane : " + this.sens);
			} else if (this.sens == 2 && this.sens1EnAttente) {
				this.sens = 1;
				this.sens1EnAttente = false;
				System.out.println("Nouveau sens de la liane : " + this.sens);
			} else {
				this.sens = 0;
				this.sens1EnAttente = false;
				this.sens2EnAttente = false;
			}
			notifyAll();
		}
	}
}

