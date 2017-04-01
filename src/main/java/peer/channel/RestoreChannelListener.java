package peer.channel;

import peer.*;
import peer.message.*;

import java.io.*;
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
    public static final int CHANNEL_PORT = 8083;
    /** {@link ChannelListener#channelAddress} */
    public static final String CHANNEL_ADDRESS = "230.0.0.3";
    /** {@link ChannelListener#bufferSize} */
    public static final int BUFFER_SIZE = Peer.BUFFER_SIZE;

    /**
    * Constructor
    */
    public RestoreChannelListener() {
        super(CHANNEL_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE);
    }

    @Override
    protected void handler(Message received) {

        int delay = 0;

        // figure out what to do based on message type
        switch (received.getType()) {
            case "GETCHUNK": //iniciator peer manda msg getchunk para MC
            {
                GetChunkMessage outmsg = new GetChunkMessage(received.getVersion(), received.getSenderId(), received.getFileId(), String.valueOf(received.getChunkNo()));

                // ask a messenger to deliver the message
                ControlChannelListener.sendMessage(outmsg.toString(), delay);

                break;
            }
            case "CHUNK": //outros peers veem se tem um chunk e mandam para MDR
            {
                String fileId = received.getFileId();
                String chunkNo = received.getChunkNo();
                String fileName = fileId + "-" + chunkNo;

                File chunk = new File(fileName);

                if(chunk.exists()){
                    ChunkMessage outmsg = new ChunkMessage(received.getVersion(), received.getSenderId(), received.getFileId(), received.getChunkNo());

                    // generate a random delay [1-400]ms
                    delay = ThreadLocalRandom.current().nextInt(1, 401);
                    // ask a messenger to deliver the message
                    ControlChannelListener.sendMessage(outmsg.toString(), delay);
                }
                else break; //nesta seccao do enunciado nao diz o que fazer, tenho que procurar melhor


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
    public static void sendMessage(String msg) {
        new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, msg, 0)).start();
    }

}
