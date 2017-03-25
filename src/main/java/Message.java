import java.io.*;

/**
* Class for message
*/
public class Message {

    /**
    * Constructor
    */
    public Message(char[] msgType, char[] version, char[] senderID, char[] fileID, char[] chunkNo, char[] repDeg) {
        String msg;

        if(fileID.length != 64 ){
            System.out.println("Error: fileid isn't 64 char long");
            msg = "Error";
            return msg.toCharArray();
        }

        if(chunkNo.length > 6 ){
            System.out.println("Error: chunk number is longer than 6 char");
            msg = "Error";
            return msg.toCharArray();
        }

        if(repDeg.length > 1 ){
            System.out.println("Error: replication degree is longer than 1 char");
            msg = "Error";
            return msg.toCharArray();
        }

        if( repDeg[0] > '9' || repDeg[0] < '0'){
            System.out.println("Error: invalid replication degree");
            msg = "Error";
            return msg.toCharArray();
        }

        String type= new String(msgType);
        String sender= new String(senderID);
        String file= new String(fileID);
        String chunk= new String(chunkNo);
        String rep = new String(repDeg);
        String vers= new String(version);

        msg = type + " " + vers +  " " + sender +  " " + file +  " " + chunk +  " " + rep;

        //System.out.println(msg);

        return msg.toCharArray();
    }

    //Exemplo para buildMessage
    /*
    char[] type= "GETCHUNK".toCharArray();
    char[] version= "1.0".toCharArray();
    char[] sender= "42".toCharArray();
    char[] file= "4ynh64c8w17moarku41edscyixug21cgxx214eieai35koumfoy56ju2vj7edw6s".toCharArray();
    char[] chunk= "5".toCharArray();
    char[] rep= "8".toCharArray();

    buildMessage(type, version, sender, file, chunk, rep);
    */

}
