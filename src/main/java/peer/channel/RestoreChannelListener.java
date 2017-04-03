package peer.channel;

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
    public static final int CHANNEL_PORT = 8083;
    /** {@link ChannelListener#channelAddress} */
    public static final String CHANNEL_ADDRESS = "230.0.0.3";
    /** {@link ChannelListener#bufferSize} */
    public static final int BUFFER_SIZE = 64 * 1024;

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

                try{
                        int chunkIndex = getChunkIndex(received.getChunkNo());

                        if(chunkIndex > -1){
                            //chunk presente no peer. ir buscar body:

                            String filePath = "chunks/" + received.getFileId() + "-" + received.getChunkNo() + ".chk";
                            File chunk = new File(filePath);

                            if(chunk.exists()){ //so para ter a certeza

                                byte[] body = Files.readAllBytes(Paths.get(filePath));

                                ChunkMessage outmsg = new ChunkMessage(received.getVersion(), received.getSenderId(), received.getFileId(), received.getChunkNo(), body);

                                // generate a random delay [1-400]ms
                                delay = ThreadLocalRandom.current().nextInt(1, 401);
                                // ask a messenger to deliver the message
                                ControlChannelListener.sendMessage(outmsg.toString(), delay);
                            }
                        }
                        else{ //peer nao tem o body
                            break;
                        }

                    }
                    catch(Exception e){
                        System.out.println("RestoreChannelListener for CHUNK: " +e);
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
    public static void sendMessage(String msg) {
        new Thread(new ChannelMessenger(MESSENGER_NAME, CHANNEL_PORT, CHANNEL_ADDRESS, BUFFER_SIZE, msg, 0)).start();
    }

    /**
    * Look for chunkNo in list of chunkNos stored in this peer
    *
    * @param chunkNo chunk number to look for
    *
    * @return index of chunkNo in array of chunkNos or -1 if not found
    */
    public int getChunkIndex(String chunkNo){
        int chunkIndex = -1;

        try{
            //cena falsa para por chunks para poder ler
            Integer[] chunks = {1,43,2,143,23,87,4,8,3};
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("testing/chunkList.txt"));
            outputStream.writeObject(chunks);
            //fim da cena falsa


            //ler chunkIds do peer
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("testing/chunkList.txt"));
            Integer[] chunksNosInThisPeer = (Integer[])inputStream.readObject();

            //procurar chunkNo nos chunkNos deste peer
            List<Integer> chunkList = Arrays.asList(chunksNosInThisPeer);
            chunkIndex = chunkList.indexOf(Integer.valueOf(chunkNo));


        }
        catch(Exception e){
            System.out.println("RestoreChannelListener > getChunkIndex: " +e);
        }

        return chunkIndex;
    }
}
