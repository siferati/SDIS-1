import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

  private static final int PORT = 8080;
  private static final String ADDRESS = "localhost";
  private static final int BUFFER_SIZE = 256;

  public static void main(String[] args) throws IOException {

    if (args.length != 1) {
      System.out.println("Usage: java Client <string>");
      return;
    }

    // get a datagram socket
    DatagramSocket socket = new DatagramSocket();

    //get inet address
    InetAddress inetAddress = InetAddress.getByName(ADDRESS);

    // initialize buffer
    byte[] buffer = new byte[BUFFER_SIZE];

    // send request
    buffer = args[0].getBytes(); //get request from function call
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, PORT);
    socket.send(packet);

    // reset buffer
    buffer = new byte[BUFFER_SIZE];

    // get response
    packet = new DatagramPacket(buffer, buffer.length);
    socket.receive(packet);

    // display response
    //String reply = packet.getData().toString();
    String reply = new String(packet.getData(), 0, packet.getLength());
    System.out.println("Server reply: " + reply);

    socket.close();
  }
}
