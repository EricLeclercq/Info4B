class MonThread extends Thread {
   public void run() {
   	for (int i=1 ; i<50 ; i++){
      	System.out.println("je suis le thread "+getName()+" i="+i);
        try{
          sleep((int)(Math.random() * 100));
        }catch(InterruptedException e){e.printStackTrace();}
    }
   }
}

public class TestThread1 {
    public static void main(String args[]) {
        Thread th1 = new MonThread();
        Thread th2 = new MonThread();
        th1.start();
        th2.start();
        System.out.println("Je suis le thread principal");
    }
}
