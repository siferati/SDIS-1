package peer.message;

import peer.*;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
* A message to be sent. Made of an header and a body.
*/
public class Message {

    /** Regex used to split a message into its separate fields */
    private static final String SPLIT_REGEX = "(?:\\s|" + MessageHeader.CRLF + ")+";

    /** Max size of each file chunk */
    public static final int CHUNK_SIZE = 64000;

    /** Header of this message */
    protected MessageHeader header;

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

    /**
    * Parses a message (byte[]) and returns the correspondent Message object
    *
    * @param msg Message to parse
    */
    public static Message parser(byte[] msg) {

        // <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><body>

        // init
        byte[] header = new byte[Peer.BUFFER_SIZE - CHUNK_SIZE];

        // byte[] -> string -> length + 2* CRLF.length
        int header_length = new String(msg).split(MessageHeader.CRLF)[0].length() + 2 * MessageHeader.CRLF.length();

        // init
        byte[] body = new byte[CHUNK_SIZE];

        // split array into header and body
        System.arraycopy(msg, 0, header, 0, header_length);
        System.arraycopy(msg, header_length, body, 0, body.length);

        // split the header to get each individual field
        // .trim() removes null bytes from empty space in byte[]
        String contents[] = new String(header).trim().split(SPLIT_REGEX);

        // return object
        Message message;

        // figure out what message type to return
        switch (contents[0]) {

            case "PUTCHUNK":
            {
                message = new PutChunkMessage(contents[1], contents[2], contents[3], contents[4], contents[5], body);

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
            case "CHUNK":
            {
                message = new ChunkMessage("1.0", "1", "2", "3","bodybody".getBytes());
                break;
            }
            default:
            {
                message = null;
            }
            break;
        }

        return message;
    }

    @Override
    public String toString() {
        return header.toString() + "" + DatatypeConverter.printHexBinary(body);
    }

    /**
    * Turns this message into a byte array
    *
    * @return Message turned into byte aray
    */
    public byte[] toBytes() {

      // get header bytes
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

    /*
    * Getter
    *
    * @return {@link MessageHeader#repDeg}
    */
    public String getRepDeg() {
        return header.repDeg;
    }

    /**
    * Getter
    *
    * @return {@link #header}
    */
    public MessageHeader getHeader() {
        return header;
    }

    /**
    * Getter
    *
    * @return {@link #body}
    */
    public byte[] getBody() {
        return body;
    }

    /**
    * Returns the filepath to store/load this chunk
    *
    * @return Filepath to store/load this chunk
    */
    public String getChunkPath() {

        return "chunks/" + header.fileId + "-" + header.chunkNo + ".chk";
    }

    /**
    * Getter
    *
    * @return Length of body (byte[])
    */
    public int getBodyLength() {

      int i;
      for (i = body.length - 1; i > 0; i--) {
        if (body[i] != 0 || body[i - 1] != 0) {
          break;
        }
      }
      if (i < 0 || i == body.length - 1) {
        i++;
      }
      return i;
    }
}
