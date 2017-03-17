import java.io.*;
import java.net.*;
import java.util.*;

/**
* Peer thread to listen to the multicast control channel (MC)
*/
public class ControlChannelListener extends ChannelListener {

  public ControlChannelListener() {

    super("(MC) Control Channel", 8081, "230.0.0.1", 256);
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
