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
        if (args.length == 2) {
          if (args[0].equals("MC")) {
            new Thread(new ChannelMessenger(ControlChannelListener.MESSENGER_NAME, ControlChannelListener.CHANNEL_PORT, ControlChannelListener.CHANNEL_ADDRESS, ControlChannelListener.BUFFER_SIZE, args[1])).start();
          }
          else if (args[0].equals("MDB")) {
            new Thread(new ChannelMessenger(BackupChannelListener.MESSENGER_NAME, BackupChannelListener.CHANNEL_PORT, BackupChannelListener.CHANNEL_ADDRESS, BackupChannelListener.BUFFER_SIZE, args[1])).start();
          }
          else if (args[0].equals("MDR")) {
            new Thread(new ChannelMessenger(RestoreChannelListener.MESSENGER_NAME, RestoreChannelListener.CHANNEL_PORT, RestoreChannelListener.CHANNEL_ADDRESS, RestoreChannelListener.BUFFER_SIZE, args[1])).start();
          }
        }
        else {
          new Thread(new ControlChannelListener()).start();
          new Thread(new BackupChannelListener()).start();
          new Thread(new RestoreChannelListener()).start();
        }
    }
}
