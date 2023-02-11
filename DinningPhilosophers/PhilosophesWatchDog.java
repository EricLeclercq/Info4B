import java.util.*;

public class PhilosophesWatchDog {
    public static void main(String[] args) {
        int nbPhilosophes = 5;
        List<Fourchette> fourchettes = new ArrayList<>();
        for (int i = 0; i < nbPhilosophes; i++) {
            fourchettes.add(new Fourchette());
        }
        List<Philosophe> philosophes = new ArrayList<>();
        for (int i = 0; i < nbPhilosophes; i++) {
            philosophes.add(new Philosophe(fourchettes.get(i), fourchettes.get((i + 1) % nbPhilosophes)));
        }
        // On démarre le Watch Dog
        WatchDog watchDog = new WatchDog(philosophes);
        watchDog.start();

        // On démarre les philosophes
        for (Philosophe philosophe : philosophes) {
            philosophe.start();
        }
        // On arrête le Watch Dog une fois que tous les philosophes ont terminé
        for (Philosophe philosophe : philosophes) {
            try {
                philosophe.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        watchDog.arret = true;
    }
}

/**
 * Thread. Un philosophe cherche à prendre 2 fourchettes pour pouvoir manger.
 */ 
class Philosophe extends Thread {
    private Fourchette fourchetteGauche;
    private Fourchette fourchetteDroite;
    boolean fourchetteDroitePrise = false;
    boolean fourchetteGauchePrise = false;
        
    public Philosophe(Fourchette fourchetteGauche, Fourchette fourchetteDroite) {
        this.fourchetteGauche = fourchetteGauche;
        this.fourchetteDroite = fourchetteDroite;
    }

    public int getNbFourchettesPrises() {
        int nbFourchettes = 0;
        if (fourchetteDroitePrise) nbFourchettes += 1;
        if (fourchetteGauchePrise) nbFourchettes += 1;
        return nbFourchettes;
    }

    @Override
    public void run() {
        // Le while sert à recommencer le processus de prise de fourchettes
        // depuis le début si le philosophe a été interrompu.
        while (!fourchetteDroitePrise || !fourchetteGauchePrise) {
            try {
                fourchetteDroite.prendre();
                fourchetteDroitePrise = true;
                System.out.println("Le philosophe " + getName() + " a pris la fourchette droite.");
                // A décommenter pour provoquer le deadlock à coup sûr
                /*try { 
                    sleep((int) (Math.random() * 1) * 1000); 
                } catch (InterruptedException e) { 
                    e.printStackTrace(); 
                }*/
                fourchetteGauche.prendre();
                fourchetteGauchePrise = true;
                System.out.println("Le philosophe " + getName() + " a pris la fourchette gauche.");
            } catch (InterruptedException e) {
                // Une InterruptedException a eu lieu, probablement provoquée par le Watch Dog
                if (fourchetteDroitePrise) {
                    // On repose la fourchette droite
                    fourchetteDroitePrise = false;
                    fourchetteDroite.reposer();
                }
                if (fourchetteGauchePrise) {
                    // On repose la fourchette gauche
                    fourchetteGauchePrise = false;
                    fourchetteGauche.reposer();
                }
                System.out.println("Le philosophe " + getName() + " lâche la fourchette !");
            }
        }
        
        System.out.println("Le philosophe " + getName() + " mange.");
        try { 
            sleep((int) (Math.random() * 4) * 1000); 
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }
        System.out.println("Le philosophe " + getName() + " a fini de manger.");
        fourchetteGauche.reposer();
        fourchetteDroite.reposer();
    }
}

/**
 * Thread. Le Watch Dog surveille l'ensemble des philosophes, détecte les
 * situations de deadlock et les résoud.
 */
class WatchDog extends Thread {
    private List<Philosophe> philosophes;
    private int[] nbFourchettes;
    public boolean arret = false;

    public WatchDog(List<Philosophe> philosophes) {
        this.philosophes = philosophes;
        nbFourchettes = new int[philosophes.size()];
        for (int i = 0; i < philosophes.size(); i++) {
            nbFourchettes[i] = 0;
        }
    }
    
    @Override
    public void run() {
        while (!arret) {
            try { 
                sleep(2000); // On surveille l'état des philosophes toutes les 2s
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
            // Si tous les philosophes sont dans le même état que lors de la 
            // dernière surveillance, ils sont en situation de deadlock
            boolean deadlock = true;
            for (int i = 0; i < philosophes.size(); i++) {
                int nbFourchettesPhilosophe = philosophes.get(i).getNbFourchettesPrises();
                if (nbFourchettesPhilosophe != nbFourchettes[i] || nbFourchettesPhilosophe != 1) {
                    // Si un philosophe n'a pas le même nombre de fourchettes 
                    // depuis la dernière surveillance ou si un philosophe est 
                    // en train de manger ou n'a pas de fourchette, il n'y a pas deadlock.
                    deadlock = false;
                }
                nbFourchettes[i] = nbFourchettesPhilosophe;
            }
            if (deadlock) {
                // On cherche un philosophe qui n'a qu'une fourchette et n'a    
                // pas fini de s'exécuter
                for (Philosophe philosophe : philosophes) {
                    if (philosophe.isAlive() && philosophe.getNbFourchettesPrises() == 1) {
                        philosophe.interrupt(); // Va servir à déclencher une InterruptedException sur le thread en wait sur une fourchette
                        // On réinitialise l'état de la surveillance des 
                        // philosophes pour éviter de déclencher une nouvelle
                        // interruption trop tôt.
                        for (int i = 0; i < philosophes.size(); i++) {
                            nbFourchettes[i] = 0;
                        }
                        break; // On a uniquement besoin de libérer un philosophe pour que régler le deadlock, donc on coupe l'exécution du for lorsque c'est fait
                    }
                }
            }
        }
    }
}

/**
 * Ressource
 */
class Fourchette {
    private boolean estPrise;

    public Fourchette() {
        this.estPrise = false;
    }

    /**
     * On propage l'InterruptedException au thread appelant la méthode plutôt 
     * que de la gérer dans un try-catch pour que le thread sache que le 
     * Watch Dog est intervenu.
     */
    public synchronized void prendre() throws InterruptedException {
        if (estPrise) {
            this.wait();
        }
        estPrise = true;
    }
    
    public synchronized void reposer() {
        estPrise = false;
        this.notify();
    }
}
