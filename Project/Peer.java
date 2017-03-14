import java.io.*;

public class Peer {
    public static void main(String[] args) throws IOException {
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
