import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

  private static String address;
  private static int port;
  protected static int bufferSize;
  protected static boolean open = true;
  protected static DatagramSocket socket;
  protected static InetAddress inetAddress;

  private static int BUFFER_SIZE = 256;

  /**
  * Entry point of the program
  *
  * @param args Arguments passed in the terminal
  */
  public static void main(String[] args) {

    // address
    String[] add = args[0].split(":");

    address = add[0];
    port = Integer.parseInt(add[1]);

    try {
      // get a datagram socket
      DatagramSocket socket = new DatagramSocket();

      //get inet address
      InetAddress inetAddress = InetAddress.getByName(address);

      // initialize buffer
      byte[] buffer = new byte[BUFFER_SIZE];

      String request = "";
      for (int i = 1; i < args.length; i++) {
        if (i == args.length - 1) {
          request += args[i];
        } else {
          request += args[i] + " ";
        }
      }

      // send request
      buffer = request.getBytes();
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, port);
      socket.send(packet);

      // reset buffer
      buffer = new byte[BUFFER_SIZE];

      // get response
      packet = new DatagramPacket(buffer, buffer.length);
      socket.receive(packet);

      // display response
      String reply = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Server reply: " + reply);

      socket.close();
    }
    catch (Exception e) {}
  }
}
