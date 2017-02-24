import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {

  protected DatagramSocket socket;
  protected boolean open = true;

  private static final int PORT = 8082;
  private static final int BUFFER_SIZE = 256;

  public ServerThread() throws IOException {
    this("ServerThread");
  }

  public ServerThread(String name) throws IOException {
    super(name);
    socket = new DatagramSocket(PORT);
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

        // figure out reply
        String reply;
        if (requestString.equals("hello")) {
          reply = "ola";
        } else if (requestString.equals("goodbye")) {
          reply = "adeus";
          open = false;
        } else {
          reply = "unknown request";
        }

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
