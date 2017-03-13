import java.io.*;

public class Peer {
    public static void main(String[] args) throws IOException {
        new Thread(new ControlChannelListener()).start();
        // new BackupChannelThread().start();
        // new RestoreChannelThread().start();
    }
}
