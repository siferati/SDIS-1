package peer;

import peer.channel.*;
import peer.message.*;
import peer.file.*;

import java.io.*;

/**
* A server in the backup service
*/
public class Peer {

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
      sendMessage(args[0], args[1]);
    }
  }

  /**
  * Closes all listeners
  */
  public static void closeAll() {
    String msg = "exit";

    ControlChannelListener.sendMessage(msg, 0);

    BackupChannelListener.sendMessage(msg);

    RestoreChannelListener.sendMessage(msg);
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
        ControlChannelListener.sendMessage(msg, 0);
        break;
      case "MDB":
        if (msg.equals("PUTCHUNK")) {
          new FileManager().transfer();
        }
        break;
      case "MDR":
        RestoreChannelListener.sendMessage(msg);
        break;
      default:
        break;
    }
  }
}
