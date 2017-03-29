package peer.message;

/**
* A message to request the backup of a chunk
*/
public class PutChunkMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "PUTCHUNK";

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public PutChunkMessage(String version, String senderId, String fileId, int chunkNo, String repDeg, String body) {
    super(TYPE, version, senderId, fileId, chunkNo, repDeg, body);
  }
}
