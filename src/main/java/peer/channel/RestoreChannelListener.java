package peer.channel;

import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to the multicast data restore channel (MDR)
*/
public class RestoreChannelListener extends ChannelListener {

  /** {@link ChannelListener#channelName} */
  public static final String CHANNEL_NAME = "(MDR) Data Restore Channel";
  /** {@link ChannelMessenger#messengerName} */
  public static final String MESSENGER_NAME = CHANNEL_NAME + " Messenger";
  /** {@link ChannelListener#channelPort} */
  public static final int CHANNEL_PORT = 8083;
  /** {@link ChannelListener#channelAddress} */
  public static final String CHANNEL_ADDRESS = "230.0.0.3";
  /** {@link ChannelListener#bufferSize} */
  public static final int BUFFER_SIZE = 256;

  /**
  * Constructor
  */
  public RestoreChannelListener() {
    super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
  }

  @Override
  protected void handler(String received) {

  }

  /**
  * Create a Messenger to send a message to this channel
  *
  * @param msg {@link ChannelMessenger#message}
  */
  public static void sendMessage(String msg) {
    new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, msg, 0)).start();
  }

}
