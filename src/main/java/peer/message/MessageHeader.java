package peer.message;

import peer.*;

/**
* Header for a Message.
*
* <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF>
*/
public class MessageHeader {

  // Attrs made public for easy access in Message
  // Message takes care of hiding them from external classes

  /** Type of the message */
  public String type;
  /** Version of the protocol */
  public String version;
  /** Id of the Peer who sent the message */
  public String senderId;
  /** Id of file for backup service */
  public String fileId;
  /** Number of the chuck of file */
  public String chunkNo;
  /** Desired replication degree of the chunk */
  public String repDeg;
  /** Line terminator */
  public static final String CRLF = Character.toString((char)13) + Character.toString((char)10);

  /**
  * Constructor
  *
  * @param type {@link #type}
  * @param fileId {@link #fileId}
  * @param chunkNo {@link #chunkNo}
  * @param repDeg {@link #repDeg}
  */
  public MessageHeader(String type, String fileId, String chunkNo, String repDeg) {

    this.type = type;
    this.version = Peer.VERSION;
    this.senderId = Peer.ID;
    this.fileId = fileId;
    this.chunkNo = chunkNo;
    this.repDeg = repDeg;

  }

  @Override
  public String toString() {
    return type + " " + version + " " + senderId + " " + fileId + " " + chunkNo + " " + repDeg + " " + CRLF + CRLF;
  }

  public String print() {
    return type + " " + version + " " + senderId + " " + fileId + " " + chunkNo + " " + repDeg;
  }
}
