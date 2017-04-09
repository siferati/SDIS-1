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
  public RemovedMessage(String fileId, String chunkNo) {
    super(TYPE, fileId, chunkNo, "", new byte[0]);
  }
}
