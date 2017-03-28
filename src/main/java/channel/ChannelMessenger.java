package channel;

import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to send messages to a multicast channel
*/
public class ChannelMessenger implements Runnable {

  /** Name of the messenger (usually contains the name of the destination channel) */
  protected String messengerName;
  /** Port number of destination channel */
  protected int channelPort;
  /** IP multicast address of destination channel */
  protected String channelAddress;
  /** Size of packet buffer */
  protected int bufferSize;
  /** Message to send to the destination channel */
  protected String message;
  /** Datagram Socket */
  protected MulticastSocket socket;
  /** Inet address of listened channel */
  protected InetAddress channelInetAddress;

  /**
  * Constructor (called by subclasses)
  *
  * @param messengerName Name of the messenger
  * @param channelPort Port number of destination channel
  * @param channelAddress IP multicast address of destination channels
  * @param bufferSize Size of packet buffer
  * @param message Message to send to the destination channel
  */
  public ChannelMessenger(String messengerName, int channelPort, String channelAddress, int bufferSize, String message) {

    this.messengerName = messengerName;
    this.channelPort = channelPort;
    this.channelAddress = channelAddress;
    this.bufferSize = bufferSize;
    this.message = message;

    try {
      // get a multicast socket (no need to bind it to a port)
      socket = new MulticastSocket();
    }
    catch (IOException e) {
      System.out.println(messengerName + ": Error creating datagram socket!");
    }

    try {
      // get channel address
      channelInetAddress = InetAddress.getByName(channelAddress);
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
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, channelInetAddress, channelPort);

    try {
      // send message
      socket.send(packet);
    }
    catch (Exception e) {
      System.out.println(messengerName + ": Error sending the message!");
    }

    // end communication
    socket.close();
  }
}
