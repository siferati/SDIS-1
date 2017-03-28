package peer.message;

import java.util.Arrays;

/**
* A message to be sent. Made of an header and a body.
*/
public class Message {

  /** Regex used to split a message into its separate fields */
  private static final String SPLIT_REGEX = "(?:\\s|" + MessageHeader.CRLF + ")+";

  /** Header of this message */
  private MessageHeader header;

  /** Body of this message (file chunk as bytes) */
  private String body;

  /**
  * Constructor
  *
  * @see MessageHeader#MessageHeader
  *
  * @param body Body of this message (file chunk as bytes)
  */
  public Message(String type, String version, String senderId, String fileId, String chunkNo, String repDeg, String body) {

    this.header = new MessageHeader(type, version, senderId, fileId, chunkNo, repDeg);
    this.body = body;
  }

  /**
  * Parses a message (string) and returns the correspondent Message object
  *
  * @param msg Message to parse
  */
  public static Message parser(String msg) {

    // <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><body>
    // split the message to get each individual field
    String contents[] = msg.split(SPLIT_REGEX);

    // return object
    Message message;

    // figure out what message type to return
    switch (contents[0]) {
      case "PUTCHUNK":
        message = new PutChunkMessage(contents[1], contents[2], contents[3], contents[4], contents[5], contents[6]);
        break;
      case "STORED":
        message = new StoredMessage(contents[1], contents[2], contents[3], contents[4]);
        break;
      default:
        message = null;
        break;
    }

    return message;
  }

  @Override
  public String toString() {
    return header.toString() + "" + body;
  }

  /**
  * Getter
  *
  * @return {@link MessageHeader#type}
  */
  public String getType() {
    return header.type;
  }
}
