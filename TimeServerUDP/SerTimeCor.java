import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SerTimeCor {
  static final int BUFFERSIZE = 256;
  static final int PORTSRV = 45678;
  static final int PORTCLI = 45679;

  public static void main(String[] args) {

    String PCOMMAND = "GET TIME";
    String PCOMMAND1 = "GET TIMESTAMP";
    DatagramSocket sock;
    DatagramPacket pack = new DatagramPacket(new byte[BUFFERSIZE],BUFFERSIZE);
    DatagramPacket reponse = new DatagramPacket(new byte[BUFFERSIZE],BUFFERSIZE);

    try {
      sock = new DatagramSocket(PORTSRV);
    } catch (SocketException e) { e.printStackTrace(); return; }

    while (true) {
      try {
	     System.out.println(MyDate.getDate()+" "+MyDate.getTimestamp());
       sock.receive(pack);

	     String recu = new String(pack.getData(),0,pack.getLength());
        //System.out.println(recu.length()+" - "+pack.getLength());

	     if (recu.equals(PCOMMAND)){
	        System.out.println("recu="+recu+" de = "+pack.getAddress());
          byte [] byteMessage = MyDate.getDate().getBytes();
          reponse.setAddress(pack.getAddress());
          reponse.setPort(PORTCLI);
	        reponse.setData(byteMessage);
          reponse.setLength(byteMessage.length);
          sock.send(reponse);
      }
	    else
        if (recu.equals(PCOMMAND1)){
          System.out.println("recu="+recu+" de = "+pack.getAddress());
          byte [] byteMessage = String.valueOf(MyDate.getTimestamp()).getBytes();
          reponse.setAddress(pack.getAddress());
          reponse.setPort(PORTCLI);
	        reponse.setData(byteMessage);
          reponse.setLength(byteMessage.length);
          sock.send(reponse);
        }
	else
         System.out.println("commande non reconnue de = "+pack.getAddress());
       } catch (IOException ioe) {ioe.printStackTrace();}
       MyDate.getDate();
    }
  }
}

class MyDate{
 public static String getDate(){
  Date date = new Date();
  SimpleDateFormat monFormat = new SimpleDateFormat ("YYYY-MM-dd hh:mm:ss:S");
  return(monFormat.format(date));
 }
 public static long getTimestamp(){
  Date date = new Date();
  return(date.getTime());
  }
}
