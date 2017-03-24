import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to the multicast control channel (MC)
*/
public class ControlChannelListener extends ChannelListener {

  /** Name of the listened channel */
  public static final String CHANNEL_NAME = "(MC) Control Channel";
  /** Name of the messenger */
  public static final String MESSENGER_NAME = CHANNEL_NAME + " Messenger";
  /** Port number of listened channel */
  public static final int CHANNEL_PORT = 8081;
  /** IP multicast address of listened channel */
  public static final String CHANNEL_ADDRESS = "230.0.0.1";
  /** Size of packet buffer */
  public static final int BUFFER_SIZE = 256;

  /**
  * Constructor
  */
  public ControlChannelListener() {
    super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
  }

  @Override
  protected void handler(String received) {
    try {
      Thread.sleep(1000);

      // send a message to the channel
      new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, received)).start();
    }
    catch (Exception e){
      // ...
    }
  }

}
