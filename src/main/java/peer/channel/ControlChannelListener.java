package peer.channel;

import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to the multicast control channel (MC)
*/
public class ControlChannelListener extends ChannelListener {

  /** {@link ChannelListener#channelName} */
  public static final String CHANNEL_NAME = "(MC) Control Channel";
  /** {@link ChannelMessenger#messengerName} */
  public static final String MESSENGER_NAME = CHANNEL_NAME + " Messenger";
  /** {@link ChannelListener#channelPort} */
  public static final int CHANNEL_PORT = 8081;
  /** {@link ChannelListener#channelAddress} */
  public static final String CHANNEL_ADDRESS = "230.0.0.1";
  /** {@link ChannelListener#bufferSize} */
  public static final int BUFFER_SIZE = 256;

  /**
  * Constructor
  */
  public ControlChannelListener() {
    super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
  }

  @Override
  protected void handler(String received) {

  }

  /**
  * Create a Messenger to send a message to this channel
  *
  * @param msg {@link ChannelMessenger#message}
  * @param delay {@link ChannelMessenger#delay}
  */
  public static void sendMessage(String msg, int delay) {
    new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, msg, delay)).start();
  }

}
