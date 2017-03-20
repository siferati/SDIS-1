import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to a multicast channel
*/
public abstract class ChannelListener implements Runnable {

  // attrs obtained from subclasses
  protected String channelName;
  protected int port;

  protected String groupAddress;
  protected int bufferSize;

  // common attrs
  protected boolean open;
  protected MulticastSocket socket;
  protected InetAddress groupInetAddress;

  public ChannelListener(String channelName, int port, String groupAddress, int bufferSize) {

    this.channelName = channelName;
    this.port = port;
    this.groupAddress = groupAddress;
    this.bufferSize = bufferSize;

    // allow communication
    open = true;

    try {
      // get a multicast socket
      socket = new MulticastSocket(port);
    }
    catch (IOException e) {
      System.out.println(channelName + ": Error creating multicast socket!");
    }

    try {
      // get group address
      groupInetAddress = InetAddress.getByName(groupAddress);
    }
    catch (UnknownHostException e) {
      System.out.println(channelName + ": Error getting Inet Address!");
    }

    try {
      // join multicast group
      socket.joinGroup(groupInetAddress);
    }
    catch (IOException e) {
      System.out.println(channelName + ": Error joining multicast group!");
    }
  }

  protected abstract void handler(String received);

  @Override
  public void run() {

    while (open) {

      byte[] buf = new byte[bufferSize];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);

      try {
        // listen to the channel
        socket.receive(packet);
      }
      catch (IOException e){
        System.out.println(channelName + ": Error receiving packet from socket!");
      }

      // get received string
      String received = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Received: " + received);

      handler(received);
    }

    // end communications
    try {
      socket.leaveGroup(groupInetAddress);
    }
    catch (IOException e) {
      System.out.println(channelName + ": Error leaving multicast group!");
    }

    socket.close();
  }

}
