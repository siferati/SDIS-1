package peer.channel;

import peer.message.*;

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
  protected Message message;
  /** Datagram Socket */
  protected MulticastSocket socket;
  /** Inet address of destination channel */
  protected InetAddress channelInetAddress;
  /** Number of milliseconds to wait before sending the message */
  protected int delay;

  /**
  * Constructor (called by subclasses)
  *
  * @param messengerName {@link #messengerName}
  * @param channelPort {@link #channelPort}
  * @param channelAddress {@link #channelAddress}
  * @param bufferSize {@link #bufferSize}
  * @param message {@link #message}
  * @param delay {@link #delay}
  */
  public ChannelMessenger(String messengerName, int channelPort, String channelAddress, int bufferSize, Message message, int delay) {

    this.messengerName = messengerName;
    this.channelPort = channelPort;
    this.channelAddress = channelAddress;
    this.bufferSize = bufferSize;
    this.message = message;
    this.delay = delay;


    try {
      // get a multicast socket (no need to bind it to a port)
      socket = new MulticastSocket();
    }
    catch (IOException e) {
      System.out.println(messengerName + ": Error creating datagram socket: " + e);
    }

    try {
      // get channel address
      channelInetAddress = InetAddress.getByName(channelAddress);
    }
    catch (UnknownHostException e) {
      System.out.println(messengerName + ": Error getting Inet Address: " + e);
    }
  }

  @Override
  public void run() {

    // initialize buffer
    byte[] buffer = new byte[bufferSize];

    // turn the message into bytes
    buffer = message.toBytes();

    // fill packet with message
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, channelInetAddress, channelPort);

    try {
      if (delay > 0) {
        // wait for delay
        Thread.sleep(delay);
      }

      // send message
      socket.send(packet);
    }
    catch (Exception e) {
      System.out.println(messengerName + ": Error sending the message: " + e);
    }

    // end communication
    socket.close();
  }
}
