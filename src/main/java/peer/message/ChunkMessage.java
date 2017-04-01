package peer.message;

/**
* A message to get a chunk
*/
public class ChunkMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "CHUNK";

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public ChunkMessage(String version, String senderId, String fileId, String chunkNo) {
    super(TYPE, version, senderId, fileId, chunkNo, "", new byte[0]);
  }
}
