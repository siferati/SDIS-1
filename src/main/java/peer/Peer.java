package peer;

import peer.channel.*;
import peer.message.*;
import peer.file.*;

import java.io.*;

/**
* A server in the backup service
*/
public class Peer {

    /** Size of packet buffer. The practical limit for the data length which is imposed by the underlying IPv4 protocol is 65,507 bytes (65,535 − 8 byte UDP header − 20 byte IP header) */
    public static final int BUFFER_SIZE = 65507;

    /**
    * Entry point of the program
    *
    * @param args Arguments passed in the terminal
    */
    public static void main(String[] args) {
        System.out.println("Peer started!");

        if (args.length == 0) {
            new Thread(new ControlChannelListener()).start();
            new Thread(new BackupChannelListener()).start();
            new Thread(new RestoreChannelListener()).start();
        }
        else if (args.length == 1 && args[0].equals("exit")) {
            closeAll();
        }
        else if (args.length == 2) {
            // needed to test repdeg
            new Thread(new ControlChannelListener()).start();

            sendMessage(args[0], args[1]);
        }
    }

    /** TODO fix closeAll
    * Closes all listeners
    */
    public static void closeAll() {
        /*String msg = "exit";

        ControlChannelListener.sendMessage(msg, 0);

        BackupChannelListener.sendMessage(msg);

        RestoreChannelListener.sendMessage(msg);
        */
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
            break;
            case "MDB":
            if (msg.equals("PUTCHUNK")) {
                backup("testing/file.txt");
            }
            break;
            case "MDR":
            if (msg.equals("GETCHUNK")){
                GetChunkMessage message = new GetChunkMessage("1.0", "1", "A1B2C3", "0");
                RestoreChannelListener.sendMessage(message);
            }
            else if (msg.equals("CHUNK")){
              ChunkMessage message = new ChunkMessage("1.0", "1", "A1B2C3", "0", "faky".getBytes());
              RestoreChannelListener.sendMessage(message);
            }

            break;
            default:
            break;
        }
    }

    /**
    * @see FileManager#backup
    */
    public static void backup(String filepath) {
        new FileManager().backup(filepath);
    }
}
