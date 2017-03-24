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
