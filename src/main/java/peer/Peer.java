package peer;

import peer.channel.*;
import peer.message.*;
import peer.file.*;
import java.util.Arrays;

import java.io.*;

/**
* A server in the backup service
*/
public class Peer {

    /** Size of packet buffer. The practical limit for the data length which is imposed by the underlying IPv4 protocol is 65,507 bytes (65,535 − 8 byte UDP header − 20 byte IP header) */
    public static final int BUFFER_SIZE = 65507;

    /** {@link channel.ChannelListener#channelAddress} */
    public static String MC_ADDRESS;
    /** {@link channel.ChannelListener#channelPort} */
    public static int MC_PORT;
    /** {@link channel.ChannelListener#channelAddress} */
    public static String MDB_ADDRESS;
    /** {@link channel.ChannelListener#channelPort} */
    public static int MDB_PORT;
    /** {@link channel.ChannelListener#channelAddress} */
    public static String MDR_ADDRESS;
    /** {@link channel.ChannelListener#channelPort} */
    public static int MDR_PORT;

    /** Protocol version */
    public static String VERSION;

    /** Server id */
    public static String ID;

    /** Access point */
    public static String ACCESS_POINT;

    /** Path where chunks are stored */
    public static String CHUNKS_PATH;

    /**
    * Entry point of the program
    *
    * @param args Arguments passed in the terminal
    */
    public static void main(String[] args) {

        if (args.length < 6) {
          System.out.println("syntax: <MC_addr>:<MC_port> <MDB_addr>:<MDB_port> <MDR_addr>:<MDR_port> <PROTO_VERSION> <SERVER_ID> <ACCESS_POINT>");
          return;
        }

        System.out.println("Peer started!");

        // control channel
        String[] mc = args[0].split(":");
        MC_ADDRESS = mc[0];
        MC_PORT = Integer.parseInt(mc[1]);

        // data backup channel
        String[] mdb = args[1].split(":");
        MDB_ADDRESS = mdb[0];
        MDB_PORT = Integer.parseInt(mdb[1]);

        // data restore channel
        String[] mdr = args[2].split(":");
        MDR_ADDRESS = mdr[0];
        MDR_PORT = Integer.parseInt(mdr[1]);

        // protocol version
        VERSION = args[3];

        // sender id
        ID = args[4];

        // access point
        ACCESS_POINT = args[5];

        // chunk path
        CHUNKS_PATH = "chunks/peer" + ID + "/";

        // create needed folders
        File dir = new File(Peer.CHUNKS_PATH);
        dir.mkdirs();

        if (args.length == 6) {
            new Thread(new ControlChannelListener()).start();
            new Thread(new BackupChannelListener()).start();
            new Thread(new RestoreChannelListener()).start();
        }
        else if (args.length == 8) {

            // needed to test repdeg
            new Thread(new ControlChannelListener()).start();
            new Thread(new BackupChannelListener()).start();
            new Thread(new RestoreChannelListener()).start();

            sendMessage(args[6], args[7]);
        }
    }

    /**
    * Send a message to the given channel
    *
    * @param ch Channel to send the message to
    * @param msg Message to send
    */
    public static void sendMessage(String ch, String msg) {

        switch (ch) {
            case "MC":
              // ControlChannelListener.sendMessage(msg, 0);
              if (msg.equals("DELETE")) {
                  DeleteMessage message = new DeleteMessage("A1B2C3");
                  ControlChannelListener.sendMessage(message, 0);
              }
              if (msg.equals("REMOVED")) {
                  RemovedMessage message = new RemovedMessage("A1B2C3", "24");
                  ControlChannelListener.sendMessage(message, 0);
              }
              if (msg.equals("GETCHUNK")){
                  GetChunkMessage message = new GetChunkMessage("A1B2C3", "2");
                  ControlChannelListener.sendMessage(message, 0);
              }
              if (msg.equals("TEST_INFO_UPDATE")) {
                  new FileManager().addChunkInfoToFile(ID, "Affw2C3", "24", "5", "1");
                 // ControlChannelListener.sendMessage(message, 0);
              }
            break;
            case "MDB":
            if (msg.equals("PUTCHUNK")) {
                backup("testing/longfile.txt", "" + 1);
            } else if (msg.equals("RESTORE")) {
              restore("testing/longfile.txt");
            }
            break;
            case "MDR":
            if (msg.equals("CHUNK")){
              ChunkMessage message = new ChunkMessage("A1B2C3", "0", "faky".getBytes());
              RestoreChannelListener.sendMessage(message);
            }

            break;
            default:
            break;
        }
    }

    /**
    * @see file.FileManager#backup
    */
    public static void backup(String filepath, String repDeg) {
        new FileManager().backup(filepath, repDeg);
    }


    public static void deleteFile(String filepath) {

        String fileID = new FileManager().getFileId(filepath);

        DeleteMessage message = new DeleteMessage(fileID);

        ControlChannelListener.sendMessage(message, 0);

        //nao e preciso mandar uma mensagem para cada chunk, porque ao receber delete ja elimina todos os chunks do ficheiro
        // mas em caso de o delete nao chegar la podemos mandar varias vezes?
/*
        ControlChannelListener.sendMessage(message, 0);
        ControlChannelListener.sendMessage(message, 0);
        ControlChannelListener.sendMessage(message, 0);
        ControlChannelListener.sendMessage(message, 0);
*/
    }

  /**
  * @see {@link file.FileManager#restore}
  */
  public static void restore(String filepath) {
    new FileManager().restore(filepath);
  }
}
