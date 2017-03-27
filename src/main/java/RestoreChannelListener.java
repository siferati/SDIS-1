import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to the multicast data restore channel (MDR)
*/
public class RestoreChannelListener extends ChannelListener {

  /** Name of the listened channel */
  public static final String CHANNEL_NAME = "(MDR) Data Restore Channel";
  /** Name of the messenger */
  public static final String MESSENGER_NAME = CHANNEL_NAME + " Messenger";
  /** Port number of listened channel */
  public static final int CHANNEL_PORT = 8083;
  /** IP multicast address of listened channel */
  public static final String CHANNEL_ADDRESS = "230.0.0.3";
  /** Size of packet buffer */
  public static final int BUFFER_SIZE = 256;

  /**
  * Constructor
  */
  public RestoreChannelListener() {
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

  /**
  * Create a Messenger to send a message to this channel
  *
  * @param msg Message to send
  */
  public static void sendMessage(String msg) {
    new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, msg)).start();
  }

}
