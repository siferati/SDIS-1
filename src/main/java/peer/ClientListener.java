package peer;

import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to a client
*/
public class ClientListener implements Runnable {

  private String address;
  private int port;
  protected int bufferSize;
  protected boolean open = true;
  protected DatagramSocket socket;
  protected InetAddress inetAddress;
    private static int BUFFER_SIZE = 256;

  public ClientListener(String address, String port) {

    this.address = address;
    this.port = Integer.parseInt(port);

    try {
      socket = new DatagramSocket(this.port);
      inetAddress = InetAddress.getByName(address);
    }
    catch (Exception e) {}

  }

  @Override
  public void run() {

    while (open) {
      try {
        // initialize buffer
        byte[] buffer = new byte[BUFFER_SIZE];

        // receive request
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        // turn the request buffer into a readable string
        String requestString = new String(packet.getData(), 0, packet.getLength());

        System.out.println("Request: " + requestString);

        String[] args = requestString.split(" ");

        String op = args[0];

        switch (op) {

          case "BACKUP":
            Peer.backup(args[1], args[2]);
            break;

          case "DELETE":
            Peer.deleteFile(args[1]);
            break;

          case "RESTORE":
            //Peer.restore(args[1]);
            break;

          case "RECLAIM":
            // TODO
            break;

          default:
            break;

        }

        // figure out reply
        String reply = "ok";

        System.out.println("Reply: " + reply);

        // turn the reply string into bytes
        buffer = reply.getBytes();

        // send the response to the client at "address" and "port"
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        // overwrite packet with reply
        packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
      } catch (IOException e) {
        e.printStackTrace();
        // flag for server closure
        open = false;
      }
    }
    socket.close();
  }


}
