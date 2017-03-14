import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to the multicast control channel (MC)
*/
public class ControlChannelListener implements Runnable {

  // #define
  private static final int PORT = 8081;
  private static final String GROUP_ADDRESS = "230.0.0.1";
  private static final int BUFFER_SIZE = 256;

  // attrs
  protected boolean open;
  MulticastSocket socket;
  InetAddress groupAddress;

  public ControlChannelListener() throws IOException {

    // allow communication
    open = true;

    // get a multicast socket
    socket = new MulticastSocket(PORT);

    //get group address
    groupAddress = InetAddress.getByName(GROUP_ADDRESS);

    // join multicast group
    socket.joinGroup(groupAddress);
  }

  @Override
  public void run() {

    while (open) {

      byte[] buf = new byte[BUFFER_SIZE];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);

      try {
        // listen to the channel
        socket.receive(packet);
      }
      catch (IOException e){
        System.out.println("(MC) Control Channel: Error receiving packet from socket!");
      }

      // get received string
      String received = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Received: " + received);

      try {
        Thread.sleep(1000);

        // send a message to the channel
        new Thread(new ControlChannelMessenger(received)).start();
      }
      catch (Exception e) {
        // ...
      }
    }

    // end communications
    try {
      socket.leaveGroup(groupAddress);
    }
    catch (IOException e) {
      System.out.println("(MC) Control Channel: Error leaving multicast group!");
    }

    socket.close();
  }
}
