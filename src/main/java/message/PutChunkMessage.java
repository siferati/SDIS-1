package message;

/**
* A message to request the backup of a chunk
*/
public class PutChunkMessage extends Message {

  /** @see MessageHeader#type */
  private static final String TYPE = "PUTCHUNK";

  /**
  * @see Message#constructor
  */
  public PutChunkMessage(String version, int senderId, String fileId, int chunkNo, int repDeg, String body) {
    super(TYPE, version, senderId, fileId, chunkNo, repDeg, body);
  }
}
