import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to send messages to the multicast control channel (MC)
*/
public class ControlChannelMessenger implements Runnable {

  // #define
  private static final int MY_PORT = 8080;
  private static final int BROADCAST_PORT = 8081;
  private static final String GROUP_ADDRESS = "230.0.0.1";
  private static final int BUFFER_SIZE = 256;

  // attrs
  DatagramSocket socket;
  InetAddress groupAddress;
  String message;

  public ControlChannelMessenger(String message) throws IOException {

    // get a multicast socket
    socket = new DatagramSocket(MY_PORT);

    //get group address
    groupAddress = InetAddress.getByName(GROUP_ADDRESS);

    this.message = message;
  }

  @Override
  public void run() {

    // initialize buffer
    byte[] buffer = new byte[BUFFER_SIZE];

    // turn the message string into bytes
    buffer = message.getBytes();

    // fill packet with message
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddress, BROADCAST_PORT);

    try {
      // send message
      socket.send(packet);
    }
    catch (Exception e) {
      //
    }

    // end communication
    socket.close();
  }
}
