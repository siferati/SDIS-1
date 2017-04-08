package peer.channel;

import peer.*;
import peer.message.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Collections;

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
  public static final int BUFFER_SIZE = Peer.BUFFER_SIZE;
  /** A synchronized arraylist holding messages waiting for STORED confirmation */
  public static ArrayList<PutChunkMessage> waitingConfirmation = new ArrayList<PutChunkMessage>(Collections.synchronizedList(new ArrayList<PutChunkMessage>()));

  /**
  * Constructor
  */
  public ControlChannelListener() {
    super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
  }


  /**
  * Searches {@link #waitingConfirmation} to see if the received STORED message is a match
  *
  * @param received Message to search
  *
  * @return Index of found match. -1 otherwise
  */
  private int searchWaitingConfirmation(Message received) {

    for (int i = 0; i < waitingConfirmation.size(); i++) {

      PutChunkMessage msg = waitingConfirmation.get(i);

      if (msg.getFileId().equals(received.getFileId()) && msg.getChunkNo().equals(received.getChunkNo())) {
        return i;
      }
    }

    return -1;
  }

  @Override
  protected void handler(Message received) {

    switch (received.getType()) {

      case "STORED":

        synchronized (waitingConfirmation) {

          int i;

          // check if this peer is interested in this store
          if ((i = searchWaitingConfirmation(received)) >= 0) {

            PutChunkMessage msg = waitingConfirmation.get(i);


            // add sender to history
            if (msg.addSaver(received.getSenderId())) {

              // add one to rep deg
              msg.addActualRepDeg();
            }

            // if time window for stored is over
            if (!msg.getWaiting()) {
              // check if repDeg was achieved and act accordingly
              msg.checkRepDeg();
            }
          }
        }

        break;

      default:
        break;
    }
  }

  /**
  * Create a Messenger to send a message to this channel
  *
  * @param msg {@link ChannelMessenger#message}
  * @param delay {@link ChannelMessenger#delay}
  */
  public static void sendMessage(Message msg, int delay) {
    new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, msg, delay)).start();
  }

}
