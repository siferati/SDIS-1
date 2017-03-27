import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to the multicast data backup channel (MDB)
*/
public class BackupChannelListener extends ChannelListener {

  /** Name of the listened channel */
  public static final String CHANNEL_NAME = "(MDB) Data Backup Channel";
  /** Name of the messenger */
  public static final String MESSENGER_NAME = CHANNEL_NAME + " Messenger";
  /** Port number of listened channel */
  public static final int CHANNEL_PORT = 8082;
  /** IP multicast address of listened channel */
  public static final String CHANNEL_ADDRESS = "230.0.0.2";
  /** Size of packet buffer */
  public static final int BUFFER_SIZE = 256;

  /**
  * Constructor
  */
  public BackupChannelListener() {
    super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
  }

  @Override
  protected void handler(String received) {

    if (received.equals("PUTCHUNK")) {
      // create message
      PutChunkMessage message = new PutChunkMessage("1.0", 1, "A1B2C3", 0, 1, "body");
      // send message
      new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, message.toString())).start();
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
