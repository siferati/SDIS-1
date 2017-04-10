package peer.channel;

import peer.*;
import peer.message.*;
import peer.file.*;

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
    public static ArrayList<GetChunkMessage> waitingConfirmation = new ArrayList<GetChunkMessage>(Collections.synchronizedList(new ArrayList<GetChunkMessage>()));

    /** last chunk that was written to the file */
    private int currentChunk = -1;

    /** list of chunks to write to file. */
    private ArrayList<Message> receivedChunks = new ArrayList<Message>();

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

        GetChunkMessage msg = waitingConfirmation.get(i);

        if (msg.getFileId().equals(received.getFileId()) && msg.getChunkNo().equals(received.getChunkNo())) {
          return i;
        }
      }

      return -1;
    }


    /**
    * Looks for the next chunk to append to file
    */
    private Message searchReceivedChunks() {
      for (Message msg : receivedChunks) {
        if (Integer.parseInt(msg.getChunkNo()) == currentChunk - 1) {
          return msg;
        }
      }
      return null;
    }


    @Override //TODO aqui
    protected void handler(Message received) {

        int delay = 0;

        // figure out what to do based on message type
        switch (received.getType()) {
            case "CHUNK": //outros peers veem se tem um chunk e mandam para MDR
            {
              synchronized (waitingConfirmation) {

                int i;

                // check if this peer is interested in this chunk
                if ((i = searchWaitingConfirmation(received)) >= 0) {

                  GetChunkMessage msg = waitingConfirmation.get(i);

                  // replied received
                  msg.setReplied(true);

                  // if time window for CHUNK is over
                  if (!msg.getWaiting()) {
                    // update queue
                    msg.update();
                  }

                  // if this is the next chunk
                  if (Integer.parseInt(received.getChunkNo()) == currentChunk + 1) {
                    // append to file
                    new FileManager().build(received);
                  }
                  else {

                    // check if next chunk is already stored waiting
                    Message nextChunk = searchReceivedChunks();

                    if (nextChunk != null) {

                      // append to file
                      new FileManager().build(nextChunk);
                    }
                    else {

                      // store chunk
                      receivedChunks.add(received);
                    }
                  }
              }
              }
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
