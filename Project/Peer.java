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
        if (args.length != 0) {
          new Thread(new ControlChannelMessenger(args[0])).start();
        }
        else {
          new Thread(new ControlChannelListener()).start();
        }
        // new BackupChannelThread().start();
        // new RestoreChannelThread().start();
    }
}
