import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to send messages to the multicast control channel (MC)
*/
public class ControlChannelMessenger extends ChannelMessenger {

  /** Name of the messenger (usually contains the name of the destination channel) */
  private static final String MESSENGER_NAME = "(MCM) Multicast Control Channel Messenger";
  /** Port number of destination channel */
  private static final int CHANNEL_PORT = 8081;
  /** IP multicast address of destination channel */
  private static final String CHANNEL_ADDRESS = "230.0.0.1";
  /** Size of packet buffer */
  private static final int BUFFER_SIZE = 256;

  /**
  * Constructor
  *
  * @param message Message to send to the destination channel
  */
  public ControlChannelMessenger(String message) {
    super(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, message);
  }

}
