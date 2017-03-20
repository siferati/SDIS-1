import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to send messages to a multicast channel
*/
public abstract class ChannelMessenger implements Runnable {

  // attrs obtained from subclasses
  protected String messengerName;
  protected int port;
  protected int groupPort;
  protected String groupAddress;
  protected int bufferSize;
  protected String message;

  // common attrs
  protected DatagramSocket socket;
  protected InetAddress groupInetAddress;

  public ChannelMessenger(String messengerName, int port, int groupPort, String groupAddress, int bufferSize, String message) {

    this.messengerName = messengerName;
    this.port = port;
    this.groupPort = groupPort;
    this.groupAddress = groupAddress;
    this.bufferSize = bufferSize;
    this.message = message;

    try {
      // get a multicast socket
      socket = new DatagramSocket(port);
    }
    catch (SocketException e) {
      System.out.println(messengerName + ": Error creating datagram socket!");
    }

    try {
      // get group address
      groupInetAddress = InetAddress.getByName(groupAddress);
    }
    catch (UnknownHostException e) {
      System.out.println(messengerName + ": Error getting Inet Address!");
    }
  }

  @Override
  public void run() {

    // initialize buffer
    byte[] buffer = new byte[bufferSize];

    // turn the message string into bytes
    buffer = message.getBytes();

    // fill packet with message
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupInetAddress, groupPort);

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
