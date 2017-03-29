package peer.message;

/**
* A message to confirm the backup of a chunk
*/
public class StoredMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "STORED";

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public StoredMessage(String version, String senderId, String fileId, int chunkNo) {
    super(TYPE, version, senderId, fileId, chunkNo, "", "");
  }
}
