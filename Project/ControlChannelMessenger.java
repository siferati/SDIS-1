import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to send messages to the multicast control channel (MC)
*/
public class ControlChannelMessenger extends ChannelMessenger {

  // #define
  private static final String MESSENGER_NAME = "(MCM) Multicast Control Channel Messenger";
  private static final int PORT = 8080;
  private static final int GROUP_PORT = 8081;
  private static final String GROUP_ADDRESS = "230.0.0.1";
  private static final int BUFFER_SIZE = 256;

  public ControlChannelMessenger(String message) {
    super(MESSENGER_NAME, PORT, GROUP_PORT, GROUP_ADDRESS, BUFFER_SIZE, message);
  }

}
