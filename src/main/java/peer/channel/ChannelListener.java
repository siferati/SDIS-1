package peer.channel;

import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to a multicast channel
*/
public abstract class ChannelListener implements Runnable {

  /** Name of the listened channel */
  protected String channelName;
  /** Port number of listened channel */
  protected int channelPort;
  /** IP multicast address of listened channel */
  protected String channelAddress;
  /** Size of packet buffer */
  protected int bufferSize;
  /** TRUE while listener is active */
  protected boolean open;
  /** Multicast Socket */
  protected MulticastSocket socket;
  /** Inet address of listened channel */
  protected InetAddress channelInetAddress;

  /**
  * Constructor (called by subclasses)
  *
  * @param channelName {@link #channelName}
  * @param channelPort {@link #channelPort}
  * @param channelAddress {@link #channelAddress}
  * @param bufferSize {@link #bufferSize}
  */
  public ChannelListener(String channelName, int channelPort, String channelAddress, int bufferSize) {

    this.channelName = channelName;
    this.channelPort = channelPort;
    this.channelAddress = channelAddress;
    this.bufferSize = bufferSize;

    // allow communication
    open = true;

    try {
      // get a multicast socket
      socket = new MulticastSocket(channelPort);
    }
    catch (IOException e) {
      System.out.println(channelName + ": Error creating multicast socket!");
    }

    try {
      // get channel address
      channelInetAddress = InetAddress.getByName(channelAddress);
    }
    catch (UnknownHostException e) {
      System.out.println(channelName + ": Error getting Inet Address!");
    }

    try {
      // join multicast group
      socket.joinGroup(channelInetAddress);
    }
    catch (IOException e) {
      System.out.println(channelName + ": Error joining multicast group!");
    }
  }

  /**
  * Handler called when a message is received.
  * Subclasses should override this
  *
  * @param received Message received
  */
  protected abstract void handler(String received);

  /**
  * Handler called when a message is received
  *
  * @see #handler
  *
  * @param received Message received
  */
  private final void superHandler(String received) {

    if (received.equals("exit")) {
      open = false;
    }
    else {
      handler(received);
    }
  }

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

      System.out.println(channelName + ": " + received);

      superHandler(received);
    }

    // end communications
    try {
      socket.leaveGroup(channelInetAddress);
    }
    catch (IOException e) {
      System.out.println(channelName + ": Error leaving multicast group!");
    }

    socket.close();

    System.out.println(channelName + ": " + "Communication closed");
  }

}
