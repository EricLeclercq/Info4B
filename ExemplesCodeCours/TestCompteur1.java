class Compteur extends Thread{
	private boolean arret=false;
	
	public void run(){
		int i=0;
		while(arret==false){
			i++;
			System.out.println(i);
		}
	}
	
	public void stopper(){
		arret=true;
	}
}

public class TestCompteur1 {
	public static void main(String[] args) {
		Compteur c=new Compteur();
		c.start();
		try{
			int touche=System.in.read();
		}catch(Exception e){e.printStackTrace();}

		c.stopper();
		System.out.println("Fin");
	}
}

