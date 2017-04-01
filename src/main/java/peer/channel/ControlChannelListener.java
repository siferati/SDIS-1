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

  @Override
  protected void handler(Message received) {

    // switch (received.getType()) {
    //
    //   case "STORED":
    //
    //     synchronized (waitingConfirmation) {
    //
    //       int i;
    //
    //       // check if this peer is interested in this store
    //       if ((i = waitingConfirmation.indexOf(received)) > 0) {
    //
    //         PutChunkMessage msg = waitingConfirmation.get(i);
    //
    //         // add sender to history
    //         if (msg.addSaver(received.getSenderId())) {
    //
    //           // add one to rep deg
    //           msg.addActualRepDeg();
    //         }
    //
    //         // if time window for stored is over
    //         if (!msg.getWaiting()) {
    //
    //           // if rep deg was achieved
    //           if (msg.getActualRepDeg() > Integer.parseInt(msg.getRepDeg())) {
    //
    //             // remove this message from the "queue"
    //             waitingConfirmation.remove(i);
    //           }
    //           else {
    //
    //             // send message again, this time doubling the time window
    //             if (!msg.resend()) {
    //
    //               // if max attempts to resend were achieved
    //               // remove this message from the "queue"
    //               waitingConfirmation.remove(i);
    //             }
    //           }
    //         }
    //       }
    //     }
    //
    //     break;
    //
    //   default:
    //     break;
    // }
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
