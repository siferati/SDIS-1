import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

  private static final int PORT = 8081;
  private static final int PORT2 = 8082;
  private static final String GROUP_ADDRESS = "230.0.0.1";
  private static final String ADDRESS = "localhost";
  private static final int BUFFER_SIZE = 256;
  protected static boolean done = false;

  public static void main(String[] args) throws IOException {

    // get a multicast socket
    MulticastSocket socket = new MulticastSocket(PORT);

    // get a datagram socket
    DatagramSocket socket2 = new DatagramSocket();

    //get inet address
    InetAddress inetAddress = InetAddress.getByName(ADDRESS);

    //get group address
    InetAddress groupAddress = InetAddress.getByName(GROUP_ADDRESS);

    // join multicast group
    socket.joinGroup(groupAddress);

    while (!done) {

      byte[] buf = new byte[256];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);

      String received = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Received: " + received);

      // initialize buffer
      byte[] buffer = new byte[BUFFER_SIZE];

      // send request
      buffer = args[0].getBytes(); //get request from function call
      DatagramPacket requestpacket = new DatagramPacket(buffer, buffer.length, inetAddress, PORT2);
      socket2.send(requestpacket);

      // reset buffer
      buffer = new byte[BUFFER_SIZE];

      // get response
      packet = new DatagramPacket(buffer, buffer.length);
      socket2.receive(packet);

      // display response
      String reply = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Server reply: " + reply);
    }

    socket.leaveGroup(groupAddress);
    socket.close();
    socket2.close();
  }
}
