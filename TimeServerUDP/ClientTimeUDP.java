import java.io.*;
import java.net.*;

public class ClientTimeUDP {

  static final String HOST = "localhost";
  static final int PORTSRV = 45678;
  static final int PORTCLI = 45679;
  static final int BUFFERSIZE = 256;

  public static void main(String args[]) {
     try {

      String message = "GET TIME";
      byte [] byteMessage = message.getBytes();

      InetAddress address = InetAddress.getByName(HOST);

      DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, address, PORTSRV);

      DatagramSocket dsocket = new DatagramSocket();
      dsocket.send(packet);
      dsocket.close();


      DatagramSocket sock = new DatagramSocket(PORTCLI);
      DatagramPacket packetHeure = new DatagramPacket(new byte[BUFFERSIZE],BUFFERSIZE);
      sock.receive(packetHeure);
      String recu = new String(packetHeure.getData(),0,packetHeure.getLength());
      System.out.println(recu);

    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
