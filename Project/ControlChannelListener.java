import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to the multicast control channel (MC)
*/
public class ControlChannelListener extends ChannelListener {

  // #define
  private static final String CHANNEL_NAME = "(MC) Control Channel";
  private static final int PORT = 8081;
  private static final String GROUP_ADDRESS = "230.0.0.1";
  private static final int BUFFER_SIZE = 256;

  public ControlChannelListener() {
    super(CHANNEL_NAME, PORT, GROUP_ADDRESS, BUFFER_SIZE);
  }

  @Override
  protected void handler(String received) {
    try {
      Thread.sleep(1000);

      // send a message to the channel
      new Thread(new ControlChannelMessenger(received)).start();
    }
    catch (Exception e){
      // ...
    }
  }

}
