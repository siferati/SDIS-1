package peer.file;

import peer.channel.*;
import peer.message.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import java.util.*;

/**
* A file manager to handle file transfers
*/
public class FileManager {

  /**
  * Constructor
  */
  public FileManager() {

  }


  /**
  * Returns the ID of a given file
  *
  * @param filepath Path to the file
  *
  * @return File ID
  */
  public String getFileId(String filepath) {

    Path path = Paths.get(filepath);

    try {

      // get file attrs
      BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

      // generate a string containing attrs
      String rawId = filepath + attrs.size() + attrs.creationTime() + attrs.lastModifiedTime();

      // convert string to bitstring
      String bitId = stringToBitstring(rawId);

      // hash bitstring
      String hashedId = hashString(bitId);

      return hashedId;
    }
    catch (Exception e) {
      System.out.println("FileManager: Error creating file id: " + e);
      return null;
    }

  }


  /**
  * Converts a given string into a bitstring
  *
  * @param str String to convert
  *
  * @return Bitstring
  */
  public String stringToBitstring(String str) {

    // get bytes from string
    byte[] bytes = str.getBytes();

    // init output
    String bitstring = "";

    // loop each byte and turn them into bits
    for (byte b: bytes) {
      bitstring += Integer.toBinaryString(b);
    }

    return bitstring;
  }


  /**
  * Hashes a string by applying SHA256
  *
  * @param str String to hash
  *
  * @return Hashed string
  */
  public String hashString(String str) {

    try {

      // create message digest sha256
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      // get hashed byte array
      byte[] hashed = md.digest(str.getBytes());

      // return hashed string
      return DatatypeConverter.printHexBinary(hashed);
    }
    catch (Exception e) {

      System.out.println("FileManager: Error hashing string: " + e);
      return null;
    }
  }


  /**
  * Backs up the given file
  * by splitting it into 64Kb chunks
  * and asking other peers to store them
  *
  * @param filepath Path to the file to backup
  */
  public void backup(String filepath) {

    File file = new File(filepath);

    long filesize = file.length();

    // init array
    byte[] chunk = new byte[Message.CHUNK_SIZE];

    try {

      FileInputStream fis = new FileInputStream(file);

      String fileId = getFileId(filepath);

      int chunkNo = 0;
      int nread = 0;

      // read file into chunks
      while ((nread = fis.read(chunk)) > 0) {

        // ignore trailing garbage left by read
        byte[] body = new byte[nread];

        // TODO make sure this is correct. read was leaving last bytes with garbage?
        System.arraycopy(chunk, 0, body, 0, nread);

        PutChunkMessage msg = new PutChunkMessage("1.0", "1", fileId, Integer.toString(chunkNo), "1", body);

        // add this message to waiting "queue"
        synchronized (ControlChannelListener.waitingConfirmation) {
          ControlChannelListener.waitingConfirmation.add(msg);
        }

        // send message to MDB channel
        msg.send();

        // prepare next ite
        chunkNo++;
      }

      // If the file size is a multiple of the chunk size,
      // the last chunk has size 0
      if (filesize % Message.CHUNK_SIZE == 0) {

        // get message to send to multicast channel
        PutChunkMessage lastmsg = new PutChunkMessage("1.0", "1", fileId, Integer.toString(chunkNo), "1", new byte[0]);

        // send message
        lastmsg.send();
      }

      fis.close();

    }
    catch (Exception e) {
      System.out.println("FileManager: Error opening/reading file " + filepath + ": " + e);
    }

  }

  /** TODO create subfolder with server id as its name and store chunks in it
  * Stores the chunk cointained in the msg
  *
  * @param msg Message containing the chunk to store
  */
  public void store(Message msg) {

    String filepath = msg.getChunkPath();

    // create a byte[] to store only the actual content of the chunk
    byte[] content = new byte[msg.getBodyLength()];

    System.arraycopy(msg.getBody(), 0, content, 0, content.length);

    try {
      FileOutputStream out = new FileOutputStream(filepath);
      out.write(content);
      out.close();
    }
    catch (Exception e) {
      System.out.println("FileManager: Error storing chunk " + filepath);
    }
  }

  /**
  * Adds an entry to the log file
  *
  * @param sender {@link message.MessageHeader#senderId}
  * @param file {@link message.MessageHeader#fileId}
  * @param chunk {@link message.MessageHeader#chunkNo}
  * @param desiredRep {@link message.MessageHeader#repDeg}
  * @param actualRep {@link message.PutChunkMessage#actualRepDeg}
  */
  public void addChunkInfoToFile(String sender, String file, String chunk, String desiredRep, String actualRep) {

    try {

      //ler info que ja la esta
      ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("testing/currentInfo.txt"));
      String[] currentInfo = (String[])inputStream.readObject();

      //System.out.println("de currentInfo.txt: "+Arrays.toString(currentInfo));

      //adicionar info
      ArrayList<String> currentInfoList = new ArrayList<String>(Arrays.asList(currentInfo));
      String newInfo = sender+"-"+file+"-"+chunk+"-"+desiredRep+"-"+actualRep;
      currentInfoList.add(newInfo);

      //voltar a guardar no ficheiro
      currentInfo = currentInfoList.toArray(new String[currentInfoList.size()]);
      ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("testing/currentInfo.txt"));
      outputStream.writeObject(currentInfo);

      //System.out.println("para currentInfo.txt: "+Arrays.toString(currentInfo));

      inputStream.close();
    }
    catch(Exception e) {
        System.out.println("Message > addChunkInfoToFile: " + e);
    }
  }
}
