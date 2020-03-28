import java.io.*;
import java.net.*;

/**
 * Contributeur : Eric Leclercq
 * Refactoring by : Annabelle Gillet
 */
public class ClientTimeCorUDPv2 {

	static final String HOST = "localhost";
	static final int PORTSRV = 45678;
	static final int PORTCLI = 45679;
	static final int BUFFERSIZE = 256;

	public static void main(String args[]) {
		long debutMesure = 0;
		long finMesure = 0;
		int nbEnvois = 10;
		String message = "GET TIMESTAMP";
		byte [] byteMessage = message.getBytes();
		try {
			InetAddress address = InetAddress.getByName(HOST);
			DatagramSocket sock = new DatagramSocket(PORTCLI);
			
			debutMesure = System.currentTimeMillis();
			for (int i = 0; i < nbEnvois; i++) {
				DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, address, PORTSRV);
				DatagramSocket dsocket = new DatagramSocket();
				dsocket.send(packet);
				dsocket.close();

				DatagramPacket packetHeure = new DatagramPacket(new byte[BUFFERSIZE], BUFFERSIZE);
				sock.receive(packetHeure);
				String recu = new String(packetHeure.getData(), 0, packetHeure.getLength());
			}
			finMesure = System.currentTimeMillis();

		} catch (Exception e) {
			System.err.println(e);
		}
		System.out.println(debutMesure + " --- " + finMesure);
		long ecart = finMesure - debutMesure;
		double correction = ecart / (double) (nbEnvois * 2);
		System.out.println("correction = " + correction + "ms");
	}
}
