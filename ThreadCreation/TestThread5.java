class MonThread implements Runnable {
  public void run() {
    Thread  thread = Thread.currentThread ();
   	for (int i = 1 ; i < 100 ; i++){
      	System.out.println("je suis le thread "+thread.getName()+" avec l'id "+thread.getId()+" i="+i);
        try{
          Thread.sleep((int)(Math.random() * 100));
        }catch(InterruptedException e){e.printStackTrace();}
    }
   }
}

public class TestThread5 {
    public static void main(String args[]) {
        final int NBTH = 100;
        Thread TabThread[]= new Thread[NBTH];

        for (int i=0; i<NBTH; i++){
           TabThread[i]= new Thread(new MonThread());
           TabThread[i].setName("thread"+i);
        }

        TabThread[0].setPriority(Thread.MIN_PRIORITY);
        TabThread[NBTH - 1].setPriority(Thread.MAX_PRIORITY);

        for (int i=0; i<NBTH; i++){
          TabThread[i].start();
        }

        System.out.println("Je suis le thread principal");

        try{
          for (int i=0; i<NBTH; i++){
            TabThread[i].join();
          }
        }catch(InterruptedException e){e.printStackTrace();}

        System.out.println("Les threads ont terminé, je quitte");
    }
}
