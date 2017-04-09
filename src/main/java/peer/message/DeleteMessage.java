package peer.message;

import java.io.*;

/**
* A message to delete a chunk
*/
public class DeleteMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "DELETE";

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public DeleteMessage(String fileId) {

      super(TYPE, fileId, "", "", "fake".getBytes());

  }
}
