package peer.message;

import java.io.*;


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
  public ChunkMessage(String version, String senderId, String fileId, String chunkNo, byte[] body) {

      super(TYPE, version, senderId, fileId, chunkNo, "", "");

      this.setBody(body);




  }
}
