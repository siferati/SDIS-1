package peer.message;

import peer.channel.*;

import java.util.TimerTask;
import java.util.Timer;

/**
* A message to get a chunk
*/
public class GetChunkMessage extends Message {

  /** {@link MessageHeader#type} */
  private static final String TYPE = "GETCHUNK";

  /** Initial ammount of time (in milliseconds) to wait for CHUNK reply */
  public static final int WAITING_WINDOW = 1000;

  /** The actual {@link #WAITING_WINDOW}, taking into account resends, etc */
  private int actualWaitingWindow = WAITING_WINDOW;

  /** TRUE if still waiting for CHUNK reply */
  public boolean waiting = true;

  /** Number of times message was resent */
  private int nresends = 0;

  /** Max number of times a message can be resent */
  public static final int MAX_NRESENDS = 4;

  /** TRUE if this message has been replied to */
  private boolean replied = false;

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public GetChunkMessage(String fileId, String chunkNo) {
    super(TYPE, fileId, chunkNo, "", new byte[0]);
  }


  /**
  * Resend this message to the MC channel
  */
  public boolean resend() {

    if (nresends < MAX_NRESENDS) {

      // reset waiting
      waiting = true;

      // double time window
      actualWaitingWindow = actualWaitingWindow * 2;

      nresends++;

      System.out.println(header.chunkNo + ": Resending message! #" + nresends + " t" + actualWaitingWindow);

      send();

      return true;
    }
    else {
      return false;
    }
  }

  /**
  * Sends this message to the MC channel
  */
  public void send() {

    // save a reference for timer to cancel() it
    Timer timer = new Timer();

    // only allow CHUNK reply for a set time window
    timer.schedule(
      new TimerTask() {
        @Override
        public void run() {
          setWaiting(false);
          update();
          timer.cancel();
        }
      }, actualWaitingWindow
    );

    // send message
    ControlChannelListener.sendMessage(this, 0);
  }

  /**
  * Called when time window for waiting is over.
  * Checks if there's a need to resend the message
  */
  public void update() {

    // only act if waiting window is over
    // this shouldn't be needed, but just in case
    if (!waiting) {

      synchronized (RestoreChannelListener.waitingConfirmation) {

        // if rep deg was achieved
        if (replied) {

          System.out.println(header.chunkNo + ": CHUNK received, removing message from waiting queue!");

          // remove this message from the "queue"
          removeFromQueue();
          }
        else {

          // send message again, this time doubling the time window
          if (!resend()) {

            System.out.println(header.chunkNo + ": RepDeg was NOT achieved and max attempts timedout!");

            // if max attempts to resend were achieved
            // remove this message from the "queue"
            removeFromQueue();
          }
        }

        if (RestoreChannelListener.waitingConfirmation.size() == 0) {
          System.out.println("Queue is empty!");
        }
      }
    }
  }

  /**
  * Removes this message from the waiting queue
  * and adds an entry to the log
  */
  private void removeFromQueue() {

    // this shouldn't be needed, but just in case
    synchronized (RestoreChannelListener.waitingConfirmation) {

      // remove this message from the "queue"
      RestoreChannelListener.waitingConfirmation.remove(this);
    }
  }

  /**
  * Getter
  *
  * @return {@link #waiting}
  */
  public boolean getWaiting() {
    return waiting;
  }

  /**
  * Setter
  *
  * @param waiting {@link #waiting}
  */
  public void setWaiting(boolean waiting) {
    this.waiting = waiting;
  }

  /**
  * Setter
  *
  * @param replied {@link #replied}
  */
  public void setReplied(boolean replied) {
    this.replied = replied;
  }
}
