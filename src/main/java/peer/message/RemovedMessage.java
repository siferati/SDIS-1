package peer.message;

/**
* A message sent after a chunk is removed
*/
public class RemovedMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "REMOVED";

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public RemovedMessage(String version, String senderId, String fileId, String chunkNo) {
    super(TYPE, version, senderId, fileId, chunkNo, "", new byte[0]);
  }
}
