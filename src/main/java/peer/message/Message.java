package peer.message;

import peer.*;

/**
* A message to be sent. Made of an header and a body.
*/
public class Message {

    /** Regex used to split a message into its separate fields */
    private static final String SPLIT_REGEX = "(?:\\s|" + MessageHeader.CRLF + ")+";

    /** Header of this message */
    private MessageHeader header;

    /** Body of this message (file chunk as bytes) */
    private byte[] body;

    /**
    * Constructor
    *
    * @see MessageHeader#MessageHeader
    *
    * @param body Body of this message (file chunk as bytes)
    */
    public Message(String type, String version, String senderId, String fileId, String chunkNo, String repDeg, byte[] body) {

        this.header = new MessageHeader(type, version, senderId, fileId, chunkNo, repDeg);
        this.body = body;
    }

    /** TODO fix parser
    * Parses a message (string) and returns the correspondent Message object
    *
    * @param msg Message to parse
    */
    public static Message parser(String msg) {
      /*
        // <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><body>

        // split the message to get each individual field
        String contents[] = msg.split(SPLIT_REGEX);

        // return object
        Message message;

        // figure out what message type to return
        switch (contents[0]) {

            case "PUTCHUNK":
            {
                String chunk;

                if (contents.length == 7) {
                    chunk = contents[6];
                } else {
                    chunk = "";
                }

                message = new PutChunkMessage(contents[1], contents[2], contents[3], contents[4], contents[5], chunk);

                break;
            }
            case "STORED":
            {
                message = new StoredMessage(contents[1], contents[2], contents[3], contents[4]);
                break;
            }
            case "GETCHUNK":
            {
                message = new GetChunkMessage(contents[1], contents[2], contents[3], contents[4]);
                break;
            }
            default:
            {
                message = null;
            }
            break;
        }

        return message;*/
        return null;
    }

    @Override
    public String toString() {
        return header.toString() + "" + body;
    }

    /**
    * Turns this message into a byte array
    *
    * @return Message turned into byte aray
    */
    public byte[] toBytes() {

      // turn header into byte[]
      byte[] b_header = header.toString().getBytes();

      // init output
      byte[] output = new byte[Peer.BUFFER_SIZE];

      // copy b_header into output
      System.arraycopy(b_header, 0, output, 0, b_header.length);

      // copy body into output
      System.arraycopy(body, 0, output, b_header.length, body.length);

      return output;
    }

    /**
    * Getter
    *
    * @return {@link MessageHeader#type}
    */
    public String getType() {
        return header.type;
    }

    /**
    * Getter
    *
    * @return {@link MessageHeader#version}
    */
    public String getVersion() {
        return header.version;
    }

    /**
    * Getter
    *
    * @return {@link MessageHeader#senderId}
    */
    public String getSenderId() {
        return header.senderId;
    }

    /**
    * Getter
    *
    * @return {@link MessageHeader#fileId}
    */
    public String getFileId() {
        return header.fileId;
    }

    /**
    * Getter
    *
    * @return {@link MessageHeader#chunkNo}
    */
    public String getChunkNo() {
        return header.chunkNo;
    }

    /**
    * Getter
    *
    * @return {@link MessageHeader#repDeg}
    */
    public String getRepDeg() {
        return header.repDeg;
    }

}
