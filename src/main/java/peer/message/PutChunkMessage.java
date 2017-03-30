package peer.message;

/**
* A message to request the backup of a chunk
*/
public class PutChunkMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "PUTCHUNK";

  /** Number of milliseconds left for waiting confirmation */
  private int timeleft = 1000;

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public PutChunkMessage(String version, String senderId, String fileId, String chunkNo, String repDeg, String body) {
    super(TYPE, version, senderId, fileId, chunkNo, repDeg, body);
  }

  /**
  * Getter
  *
  * @return {@link #timeleft}
  */
  public int getTimeleft() {
    return timeleft;
  }
}
