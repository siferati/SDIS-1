package peer.message;

/**
* A message to get a chunk
*/
public class GetChunkMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "GETCHUNK";

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public GetChunkMessage(String fileId, String chunkNo) {
    super(TYPE, fileId, chunkNo, "", new byte[0]);
  }
}
