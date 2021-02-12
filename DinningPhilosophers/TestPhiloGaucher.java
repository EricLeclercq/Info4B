  class Fourchette {
   private boolean utilisee = false;
   private int id;

   Fourchette(int id) {
    this.id = id;
   }

   synchronized void deposer() {
    utilisee = false;
    notify();
   }

   synchronized void prendre() throws InterruptedException {
    while(utilisee)
     wait();
    utilisee = true;
    }
  }



  class Philosophe extends Thread{
    protected int id;
    protected Fourchette droite;
    protected Fourchette gauche;
    protected boolean arret;

    public Philosophe(int id, Fourchette droite, Fourchette gauche) {
      this.id = id;
      this.droite = droite;
      this.gauche = gauche;
    }

    public void run() {
      while (!arret) {
        try {
          // penser
          System.out.println("philisophe " + id + " pense...");
          sleep((int)(Math.random() * 20));
          // affame
          System.out.println("philisophe " + id + " prend fourchette droite...");
          droite.prendre();
          System.out.println("philisophe " + id + " fourchette droite OK.");
          sleep((int)(Math.random() * 10));
          System.out.println("philisophe " + id + " prend fourchette gauche...");
          gauche.prendre();
          System.out.println("philisophe " + id + " fourchette gauche OK.");
          // manger
          System.out.println("philisophe " + id + " mange..");
          sleep((int)(Math.random() * 10));
          // reposer les fourchettes
          droite.deposer();
          gauche.deposer();
        } catch (InterruptedException e) {
            e.printStackTrace();
         }
        }
      }

      public void arreter() {
        arret = true;
      }

    }

    class PhilosopheGaucher extends Philosophe {
      PhilosopheGaucher(int id, Fourchette droite, Fourchette gauche) {
       super(id, droite, gauche);
      }

      public void run() {
       while(!arret) {
         try{
         // penser
         System.out.println("philisophe " + id + " pense...");
         sleep((int)(Math.random() * 20));
         // affame
         System.out.println("philisophe " + id + " prend fourchette gauche...");
         gauche.prendre();
         System.out.println("philisophe " + id + " fourchette gauche OK.");
         sleep((int)(Math.random() * 10));
         System.out.println("philisophe " + id + " prend fourchette droite...");
         droite.prendre();
         System.out.println("philisophe " + id + " fourchette droite OK.");
         // manger
         System.out.println("philisophe " + id + " mange..");
         sleep((int)(Math.random() * 10));
         // reposer les fourchettes
         droite.deposer();
         gauche.deposer();
         }catch (InterruptedException e){
           e.printStackTrace();
         }
       }
      }
    }


    public class TestPhiloGaucher {
      public static void main(String arg[]) {
        System.out.println("Initialisation...");
        Philosophe[] tablePhilo = new Philosophe[5];
        Fourchette[] tableFourchettes = new Fourchette[5];

        for (int i = 0; i <= 4; i++)
        tableFourchettes[i] = new Fourchette(i);

        for (int i=0; i<=3; i++)
          tablePhilo[i]=new Philosophe(i,
              tableFourchettes[i],
              tableFourchettes[(i+1)%5]);

        tablePhilo[4]=new PhilosopheGaucher(4,tableFourchettes[4],tableFourchettes[0]);
        for (int i = 0; i <= 4; i++)
        tablePhilo[i].start();

      }
    }
