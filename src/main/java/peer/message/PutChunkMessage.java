package peer.message;

import peer.channel.*;
import peer.file.*;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

/**
* A message to request the backup of a chunk
*/
public class PutChunkMessage extends Message {

  /** {@link MessageHeader#type} */
  public static final String TYPE = "PUTCHUNK";

  /** Initial ammount of time (in milliseconds) to wait for STORED confirmations */
  public static final int WAITING_WINDOW = 1000;

  /** The actual {@link #WAITING_WINDOW}, taking into account resends, etc */
  private int actualWaitingWindow = WAITING_WINDOW;

  /** TRUE if still waiting for STORED confirmations */
  public boolean waiting = true;

  /** The actual replication degree of the chunk */
  private int actualRepDeg = 0;

  /** Number of times message was resent */
  private int nresends = 0;

  /** Max number of times a message can be resent */
  public static final int MAX_NRESENDS = 4;

  /** List of server ids that have stored this message (chunk) */
  private ArrayList<String> savers = new ArrayList<String>();

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public PutChunkMessage(String fileId, String chunkNo, String repDeg, byte[] body) {
    super(TYPE, fileId, chunkNo, repDeg, body);
  }

  /**
  * Getter
  *
  * @return {@link #actualRepDeg}
  */
  public int getActualRepDeg() {
    return actualRepDeg;
  }

  /**
  * Adds one to the actual replication degree
  */
  public void addActualRepDeg() {
    actualRepDeg++;
  }

  /**
  * Adds a saver to history
  *
  * @param id Saver to add
  *
  * TRUE if saver is new, FALSE if was already listed
  */
  public boolean addSaver(String id) {
    if (!savers.contains(id)) {
      savers.add(id);
      return true;
    }
    return false;
  }

  /**
  * Resend this message to the MDB channel
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
  * Called when time window for waiting is over.
  * Checks if there's a need to resend the message
  * based on te actualRepDeg
  */
  public void checkRepDeg() {

    // only act if waiting window is over
    // this shouldn't be needed, but just in case
    if (!waiting) {

      synchronized (ControlChannelListener.waitingConfirmation) {

        // if rep deg was achieved
        if (getActualRepDeg() >= Integer.parseInt(getRepDeg())) {

          System.out.println(header.chunkNo + ": RepDeg was achieved, removing message from waiting queue!");

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

        if (ControlChannelListener.waitingConfirmation.size() == 0) {
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
    synchronized (ControlChannelListener.waitingConfirmation) {

      // remove this message from the "queue"
      ControlChannelListener.waitingConfirmation.remove(this);

      // add this chunk to log
      new FileManager().addChunkInfoToFile(header.senderId, header.fileId, header.chunkNo, header.repDeg, Integer.toString(actualRepDeg));

    }
  }

  /**
  * Sends this message to the MDB channel
  */
  public void send() {

    // save a reference for timer to cancel() it
    Timer timer = new Timer();

    // only allow STORED confirmations for a set time window
    timer.schedule(
      new TimerTask() {
        @Override
        public void run() {
          setWaiting(false);
          checkRepDeg();
          timer.cancel();
        }
      }, actualWaitingWindow
    );

    // send message
    BackupChannelListener.sendMessage(this);
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
}
