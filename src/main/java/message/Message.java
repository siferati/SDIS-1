package message;

/**
* A message to be sent. Made of an header and a body.
*/
public abstract class Message {

  /** Header of this message */
  private MessageHeader header;

  /** Body of this message (file chunk as bytes) */
  private String body;

  /**
  * Constructor
  *
  * @see MessageHeader#constructor
  * @param body Body of this message (file chunk as bytes)
  */
  public Message(String type, String version, int senderId, String fileId, int chunkNo, int repDeg, String body) {

    this.header = new MessageHeader(type, version, senderId, fileId, chunkNo, repDeg);
    this.body = body;
  }

  @Override
  public String toString() {
    return header.toString() + "" + body;
  }
}
