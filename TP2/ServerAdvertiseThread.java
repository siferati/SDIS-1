import java.io.*;
import java.net.*;
import java.util.*;

public class ServerAdvertiseThread extends Thread {

  protected DatagramSocket socket;
  protected boolean open = true;

  private static final int SERVER_PORT = 8080;
  private static final int BROADCAST_PORT = 8081;
  private static final String GROUP_ADDRESS = "230.0.0.1";
  private static final int BUFFER_SIZE = 256;

  public ServerAdvertiseThread() throws IOException {
    this("ServerAdvertiseThread");
  }

  public ServerAdvertiseThread(String name) throws IOException {
    super(name);
    socket = new DatagramSocket(SERVER_PORT);
  }

  @Override
  public void run() {

    while (open) {
      try {
        // initialize buffer
        byte[] buffer = new byte[BUFFER_SIZE];

        // figure out message
        String message = "hello, this is an advertisement!";

        System.out.println("Broadcast: " + message);

        // turn the message string into bytes
        buffer = message.getBytes();

        //get inet address
        InetAddress groupAddress = InetAddress.getByName(GROUP_ADDRESS);
        // fill packet with message
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddress, BROADCAST_PORT);

        socket.send(packet);

        try {
          Thread.sleep(1000);
        }
        catch (Exception e) {
          // ...
        }

      } catch (IOException e) {
        e.printStackTrace();
        // flag for server closure
        open = false;
      }
    }
    socket.close();
  }
}
