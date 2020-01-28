class Worker extends Thread{
 private String name;
 private int id;
 private MutualExclusion sharedMutex;
 private boolean arret;
 private static final int TIME=3000;

 public Worker(String name, int id, MutualExclusion sharedMutex){
  this.name=name;
  this.id=id;
  this.sharedMutex=sharedMutex;
  arret=false;
 }

 public void run(){
  while(!arret){
    sharedMutex.enteringCriticalSection(id);
    //System.out.println(name+" id="+id+" entre dans la section critique");
    criticalSection();
    sharedMutex.leavingCriticalSection(id);
    //System.out.println(name+" id="+id+" est sorti de la section critique");
    nonCriticalSection();
  }
 }

 private void criticalSection(){
  // execute code avec variables/elemets partages
  try{
   Thread.sleep((int)(Math.random()*TIME));
  }catch(InterruptedException e){e.printStackTrace();}
 }
 
 private void nonCriticalSection(){
  // execute code dans modifier des elements partages
  try{
   Thread.sleep((int)(Math.random()*TIME));
  }catch(InterruptedException e){e.printStackTrace();}
 }
}


abstract class MutualExclusion{
 public static final int TURN_0=0;
 public static final int TURN_1=1;
 public abstract void enteringCriticalSection(int id);
 public abstract void leavingCriticalSection(int id);
}


class MutexDijkstra extends MutualExclusion{
 private volatile int turn;
 public MutexDijkstra(){
  turn=TURN_0;
 }
 public void enteringCriticalSection(int id){
   while(turn!=id){
    Thread.yield();}
   System.out.println(id+" entree section critique");
 }
 public void leavingCriticalSection(int id){
  System.out.println(id+" sortie section critique"); 
  turn=1-id; 
 }
}

class MutexDijkstra2 extends MutualExclusion{
 private volatile boolean flag[]=new boolean[2];
 public MutexDijkstra2(){
  flag[0]=false;
  flag[1]=false;
 }
 public void enteringCriticalSection(int id){
   int other;
   other=1-id;
   flag[id]=true;
   while(flag[other]==true){
    Thread.yield();}
   System.out.println(id+" entree section critique");
 }
 public void leavingCriticalSection(int id){
  System.out.println(id+" sortie section critique"); 
  flag[id]=false; 
 }
}

class MutexPeterson extends MutualExclusion{
 private volatile boolean flag[]=new boolean[2];
 private volatile int turn;
 public MutexPeterson(){
  flag[0]=false;
  flag[1]=false;
  turn=TURN_0;
 }
 public void enteringCriticalSection(int id){
   int other;
   other=1-id;
   flag[id]=true;
   turn=other;
   while((flag[other]==true)&&(turn==other)){
    Thread.yield();}
   System.out.println(id+" entree section critique");
 }
 public void leavingCriticalSection(int id){
  System.out.println(id+" sortie section critique"); 
  flag[id]=false; 
 }
}

class MutexDekker extends MutualExclusion{
 private volatile boolean flag[]=new boolean[2];
 private volatile int turn;
 public MutexDekker(){
  flag[0]=false;
  flag[1]=false;
  turn=TURN_0;
 }
 public void enteringCriticalSection(int id){
   int other;
   other=1-id;
   flag[id]=true;
      while(flag[other]==true){
       if (turn==other){
	flag[id]=false;
        while(turn==other)
	 Thread.yield();
	flag[id]=true;
       }
      }
   System.out.println(id+" entree section critique");
 }
 public void leavingCriticalSection(int id){
  System.out.println(id+" sortie section critique"); 
  turn=1-id;
  flag[id]=false; 
 }
}


public class CriticalSectionTest{
 public static void main(String args[]){

  MutualExclusion mutexAlg = new MutexDekker();

  Worker firstWorker = new Worker("worker1",0,mutexAlg);
  Worker secondWorker = new Worker("worker2",1,mutexAlg);

  firstWorker.start();
  secondWorker.start();

}
}
