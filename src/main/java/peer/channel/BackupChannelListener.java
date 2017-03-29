package peer.channel;

import peer.message.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
* Peer thread to listen to the multicast data backup channel (MDB)
*/
public class BackupChannelListener extends ChannelListener {

  /** {@link ChannelListener#channelName} */
  public static final String CHANNEL_NAME = "(MDB) Data Backup Channel";
  /** {@link ChannelMessenger#messengerName} */
  public static final String MESSENGER_NAME = CHANNEL_NAME + " Messenger";
  /** {@link ChannelListener#channelPort} */
  public static final int CHANNEL_PORT = 8082;
  /** {@link ChannelListener#channelAddress} */
  public static final String CHANNEL_ADDRESS = "230.0.0.2";
  /** {@link ChannelListener#bufferSize} */
  public static final int BUFFER_SIZE = 64 * 1024;

  /**
  * Constructor
  */
  public BackupChannelListener() {
    super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
  }

  @Override
  protected void handler(String received) {

    // parse message
    Message inmsg = Message.parser(received);

    // figure out what to do based on message type
    switch (inmsg.getType()) {
      case "PUTCHUNK":
        StoredMessage outmsg = new StoredMessage("1.0", "1", "A1B2C3", "0");
        // generate a random delay [1-400]ms
        int delay = ThreadLocalRandom.current().nextInt(1, 401);
        // ask a messenger to deliver the message
        ControlChannelListener.sendMessage(outmsg.toString(), delay);
        break;
      default:
        break;
    }
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
