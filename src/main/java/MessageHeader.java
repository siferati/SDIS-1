/**
* Header for a Message
*
* <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF>
*/
public class MessageHeader {

  /** Type of the message */
  private String type;
  /** Version of the protocol */
  private String version;
  /** Id of the Peer who sent the message */
  private int senderId;
  /** Id of file for backup service */
  private String fileId;
  /** Number of the chuck of file */
  private int chunkNo;
  /** Desired replication degree of the chunk */
  private int repDeg;
  /** Line terminator */
  public static final String CRLF = "0xD0xA";

  /**
  * Constructor
  *
  * @param type Type of the message
  * @param version Version of the protocol
  * @param senderId Id of the Peer who sent the message
  * @param fileId Id of file for backup service
  * @param chunkNo Number of the chuck of file
  * @param repDeg Desired replication degree of the chunk
  */
  public MessageHeader(String type, String version, int senderId, String fileId, int chunkNo, int repDeg) {

    this.type = type;
    this.version = version;
    this.senderId = senderId;
    this.fileId = fileId;
    this.chunkNo = chunkNo;
    this.repDeg = repDeg;

  }

  @Override
  public String toString() {
    return type + " " + version + " " + senderId + " " + fileId + " " + chunkNo + " " + repDeg + " " + CRLF + "" + CRLF;
  }
}
