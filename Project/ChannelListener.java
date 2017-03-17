import java.io.*;
import java.net.*;
import java.util.*;

public abstract class ChannelListener implements Runnable {

   protected final int PORT;
   protected final String GROUP_ADDRESS;
   protected final int BUFFER_SIZE;
   protected final String CHANNEL_NAME;

   protected boolean open;
   protected MulticastSocket socket;
   protected InetAddress groupInetAddress;

   public ChannelListener(String channelName, int port, String groupAddress, int bufferSize) {

     this.PORT = port;
     this.GROUP_ADDRESS = groupAddress;
     this.BUFFER_SIZE = bufferSize;
     this.CHANNEL_NAME = channelName;

     // allow communication
     open = true;

     try {
       // get a multicast socket
       socket = new MulticastSocket(PORT);
     }
     catch (IOException e) {
       System.out.println(CHANNEL_NAME + ": Error creating multicast socket!");
     }

     try {
       //get group address
       groupInetAddress = InetAddress.getByName(GROUP_ADDRESS);
     }
     catch (UnknownHostException e) {
       System.out.println(CHANNEL_NAME + ": Error getting Inet Address!");
     }

     try {
      // join multicast group
      socket.joinGroup(groupInetAddress);
     }
     catch (IOException e) {
       System.out.println(CHANNEL_NAME + ": Error joining multicast group!");
     }
   }

   @Override
   public void run() {

     while (open) {

       byte[] buf = new byte[BUFFER_SIZE];
       DatagramPacket packet = new DatagramPacket(buf, buf.length);

       try {
         // listen to the channel
         socket.receive(packet);
       }
       catch (IOException e){
         System.out.println(CHANNEL_NAME + ": Error receiving packet from socket!");
       }

       // get received string
       String received = new String(packet.getData(), 0, packet.getLength());
       System.out.println("Received: " + received);

       handler(received);
     }

     // end communications
     try {
       socket.leaveGroup(groupInetAddress);
     }
     catch (IOException e) {
       System.out.println(CHANNEL_NAME + ": Error leaving multicast group!");
     }

     socket.close();
   }

   protected abstract void handler(String received);
}
