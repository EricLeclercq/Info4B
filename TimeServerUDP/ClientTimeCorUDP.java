import java.io.*;
import java.net.*;

public class ClientTimeCorUDP {

  static final String HOST = "localhost";
  static final int PORTSRV = 45678;
  static final int PORTCLI = 45679;
  static final int BUFFERSIZE = 256;



  public static void main(String args[]) {
     long debutMesure=0;
     long finMesure=0;
     try {

      String message = "GET TIMESTAMP";
      byte [] byteMessage = message.getBytes();

      InetAddress address = InetAddress.getByName(HOST);
      DatagramSocket sock = new DatagramSocket(PORTCLI);

      for (int i=0; i<10; i++){
	      DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, address, PORTSRV);
        DatagramSocket dsocket = new DatagramSocket();
	      dsocket.send(packet);
	      dsocket.close();

	      DatagramPacket packetHeure = new DatagramPacket(new byte[BUFFERSIZE],BUFFERSIZE);
	      sock.receive(packetHeure);
	      String recu = new String(packetHeure.getData(),0,packetHeure.getLength());
        if (i==0) debutMesure=Long.valueOf(recu);
	      if (i==9) finMesure=Long.valueOf(recu);
       }


    } catch (Exception e) {
      System.err.println(e);
    }
    System.out.println(debutMesure+" --- "+finMesure);
    long ecart=finMesure - debutMesure;
    System.out.println("correction = "+ecart/20.0+"ms");
  }
}
