package peer.message;

/**
* A message to get a chunk
*/
public class GetChunkMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "GET";

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public GetChunkMessage(String version, String senderId, String fileId, String chunkNo) {
    super(TYPE, version, senderId, fileId, chunkNo, "", "");
  }
}
