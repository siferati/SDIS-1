package peer.channel;

import peer.*;
import peer.message.*;
import peer.file.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;



/**
* Peer thread to listen to the multicast data backup channel (MDB)
*/
public class BackupChannelListener extends ChannelListener {

  /** {@link ChannelListener#channelName} */
  public static final String CHANNEL_NAME = "(MDB) Data Backup Channel";
  /** {@link ChannelMessenger#messengerName} */
  public static final String MESSENGER_NAME = CHANNEL_NAME + " Messenger";
  /** {@link ChannelListener#channelPort} */
  public static final int CHANNEL_PORT = Peer.MDB_PORT;
  /** {@link ChannelListener#channelAddress} */
  public static final String CHANNEL_ADDRESS = Peer.MDB_ADDRESS;
  /** {@link ChannelListener#bufferSize} */
  public static final int BUFFER_SIZE = Peer.BUFFER_SIZE;

  /**
  * Constructor
  */
  public BackupChannelListener() {
    super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
  }

  @Override
  protected void handler(Message received) {

    // figure out what to do based on message type
    switch (received.getType()) {

      case "PUTCHUNK":{
        StoredMessage outmsg = new StoredMessage(received.getVersion(), received.getSenderId(), received.getFileId(), received.getChunkNo());

        // generate a random delay [1-400]ms
        int delay = ThreadLocalRandom.current().nextInt(1, 401);

        // ask a messenger to deliver the message
        ControlChannelListener.sendMessage(outmsg, delay);

        // store this chunk
        new FileManager().store(received);

        break;
        }
        default:
        break;
    }
  }

  /**
  * Create a Messenger to send a message to this channel
  *
  * @param msg {@link ChannelMessenger#message}
  */
  public static void sendMessage(Message msg) {
    new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, msg, 0)).start();
  }

  }
