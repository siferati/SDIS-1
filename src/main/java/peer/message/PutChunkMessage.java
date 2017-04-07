package peer.message;

import peer.channel.*;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

/**
* A message to request the backup of a chunk
*/
public class PutChunkMessage extends Message {

  /** {@link MessageHeader#type} */
  public static final String TYPE = "PUTCHUNK";

  /** Ammount of time (in milliseconds) to wait for STORED confirmations */
  public static int WAITING_WINDOW = 1000;

  /** TRUE if still waiting for STORED confirmations */
  public boolean waiting = true;

  /** The actual replication degree of the chunk */
  private int actualRepDeg = 0;

  /** Number of times message was resent */
  private int nresends = 0;

  /** Max number of times a message can be resent */
  public static final int MAX_NRESENDS = 4;

  /** Reference for Time object. Needed to call cancel() */
  private Timer timer;

  /** List of server ids that have stored this message (chunk) */
  private ArrayList<String> savers = new ArrayList<String>();

  /**
  * Constructor
  *
  * @see Message#Message
  */
  public PutChunkMessage(String version, String senderId, String fileId, String chunkNo, String repDeg, byte[] body) {
    super(TYPE, version, senderId, fileId, chunkNo, repDeg, body);
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
      WAITING_WINDOW = WAITING_WINDOW * 2;

      nresends++;

      send();

      return true;
    }
    else {
      return false;
    }
  }

  /**
  * Sends this message to the MDB channel
  */
  public void send() {

    // save a reference for timer to cancel() later
    timer = new Timer();
    
    // only allow STORED confirmations for a set time window
    timer.schedule(
      new TimerTask() {
        @Override
        public void run() {
          closeWaitingWindow();
        }
      }, WAITING_WINDOW
    );

    // send message
    BackupChannelListener.sendMessage(this);
  }

  /**
  * Stops checking for STORED messages and
  * cancels the timer called in {@link #send}
  */
  public void closeWaitingWindow() {
    setWaiting(false);
    timer.cancel();
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
