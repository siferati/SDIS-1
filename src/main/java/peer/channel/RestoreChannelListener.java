package peer.channel;

import peer.*;
import peer.message.*;

import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
* Peer thread to listen to the multicast data restore channel (MDR)
*/
public class RestoreChannelListener extends ChannelListener {

    /** {@link ChannelListener#channelName} */
    public static final String CHANNEL_NAME = "(MDR) Data Restore Channel";
    /** {@link ChannelMessenger#messengerName} */
    public static final String MESSENGER_NAME = CHANNEL_NAME + " Messenger";
    /** {@link ChannelListener#channelPort} */
    public static final int CHANNEL_PORT = Peer.MDR_PORT;
    /** {@link ChannelListener#channelAddress} */
    public static final String CHANNEL_ADDRESS = Peer.MDR_ADDRESS;
    /** {@link ChannelListener#bufferSize} */
    public static final int BUFFER_SIZE = Peer.BUFFER_SIZE;
    /** A synchronized arraylist holding messages waiting for CHUNK reply */
    public static ArrayList<ChunkMessage> waitingConfirmation = new ArrayList<ChunkMessage>(Collections.synchronizedList(new ArrayList<ChunkMessage>()));

    /**
    * Constructor
    */
    public RestoreChannelListener() {
        super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
    }


    /**
    * Searches {@link #waitingConfirmation} to see if the received CHUNK message is a match
    *
    * @param received Message to search
    *
    * @return Index of found match. -1 otherwise
    */
    private int searchWaitingConfirmation(Message received) {

      for (int i = 0; i < waitingConfirmation.size(); i++) {

        ChunkMessage msg = waitingConfirmation.get(i);

        if (msg.getFileId().equals(received.getFileId()) && msg.getChunkNo().equals(received.getChunkNo())) {
          return i;
        }
      }

      return -1;
    }


    @Override //TODO aqui
    protected void handler(Message received) {

        int delay = 0;

        // figure out what to do based on message type
        switch (received.getType()) {
            case "CHUNK": //outros peers veem se tem um chunk e mandam para MDR
            {

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
