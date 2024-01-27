class MonThread implements Runnable {
  public void run() {
    Thread  thread = Thread.currentThread ();
   	for (int i = 1 ; i < 5000 ; i++){
      	System.out.println("je suis le thread "+thread.getName()+" avec l'id "+thread.getId()+" i="+i);
        try{
          Thread.sleep((int)(Math.random() * 100));
        }catch(InterruptedException e){e.printStackTrace();}
    }
   }
}

public class TestThread3 {
    public static void main(String args[]) {
        Runnable mt1 = new MonThread();
        Runnable mt2 = new MonThread();

        Thread th1 = new Thread(mt1);
        Thread th2 = new Thread(mt2);

        th1.setName("thread1");
        th2.setName("thread2");

        th1.start();
        th2.start();
        System.out.println("Je suis le thread principal");

        try{
          th1.join();
          th2.join();
        }catch(InterruptedException e){e.printStackTrace();}

        System.out.println("Les threads ont terminé, je quitte");
    }
}
