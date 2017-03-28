package peer.file;

import peer.channel.*;
import peer.message.*;

import java.io.*;

/**
* A file manager to handle file transfers
*/
public class FileManager {

  /** Path to the file */
  private String filepath = "file.txt";

  public static final int CHUNK_SIZE = 10;

  /**
  * Constructor
  */
  public FileManager() {

  }

  /**
  * Transfers the file
  */
  public void transfer() {

    File file = new File(filepath);
    byte[] chunk = new byte[CHUNK_SIZE];

    try {
      FileInputStream fis = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(fis);

      while (bis.read(chunk, 0, CHUNK_SIZE) > 0) {

        String body = new String(chunk);

        String msg = new PutChunkMessage("1.0", "1", "A1B2C3", "0", "1", body).toString();
        BackupChannelListener.sendMessage(msg);
      }

    }
    catch (Exception e) {
      System.out.println("FileManager: Error opening/reading file " + filepath + ": " + e);
    }

  }
}
