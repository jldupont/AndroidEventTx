/**
 * 
 * @author jldupont
 */
package com.systemical.eventtx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Service;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Message;
import android.util.Log;

import com.systemical.netlib.dns.DNSMessage;
import com.systemical.netlib.NetUtil;
import com.systemical.netlib.Packet;
import com.systemical.system.BaseThread;
import com.systemical.system.MsgTypesList;

public class MDNSThread extends BaseThread {

	final String thisName="MDNS";
	
    // the standard mDNS multicast address and port number
    private static final byte[] MDNS_ADDR =
        new byte[] {(byte) 224,(byte) 0,(byte) 0,(byte) 251};
    private static final int MDNS_PORT = 5353;

    private static final int BUFFER_SIZE = 4096;

    private NetworkInterface networkInterface;
    private InetAddress groupAddress;
    private MulticastSocket multicastSocket;
    private NetUtil netUtil;
	
    Service service=null;

	public MDNSThread() {
		super();
		service=(Service) Factory.get(Factory.K.SERVICE);
		
		netUtil = new NetUtil(service);
		
		mtInterests=new MsgTypesList(
			MsgMap.T.TICK.ordinal()
		);
	}//

	public String getAgentName() {
		return thisName;
	}

	/**
	 * Only messages of interest to this Agent
	 *  end-up here.
	 */
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}//
	
    /**
     * Open a multicast socket on the mDNS address and port.
     * @throws IOException
     */
    private void openSocket() throws IOException {
        multicastSocket = new MulticastSocket(MDNS_PORT);
        multicastSocket.setTimeToLive(2);
        multicastSocket.setReuseAddress(true);
        multicastSocket.setNetworkInterface(networkInterface);
        multicastSocket.joinGroup(groupAddress);
    }
	
    // =======================================================================
    
    @Override
    public void run() {
        Log.v(thisName, "starting network thread");

        Set<InetAddress> localAddresses = NetUtil.getLocalAddresses();
        MulticastLock multicastLock = null;
        
        // initialize the network
        try {
            networkInterface = netUtil.getFirstWifiOrEthernetInterface();
            if (networkInterface == null) {
                throw new IOException("Your WiFi is not enabled.");
            }
            groupAddress = InetAddress.getByAddress(MDNS_ADDR); 

            multicastLock = netUtil.getWifiManager().createMulticastLock("unmote");
            multicastLock.acquire();
            //Log.v(TAG, "acquired multicast lock: "+multicastLock);

            openSocket();
        } catch (IOException e1) {

        	send(MsgMap.T.NETWORK_INIT_ERROR.ordinal(), e1);
            return;
        }

        // set up the buffer for incoming packets
        byte[] responseBuffer = new byte[BUFFER_SIZE];
        DatagramPacket response = new DatagramPacket(responseBuffer, BUFFER_SIZE);

        // loop!
        while (true) {
            // zero the incoming buffer for good measure.
            java.util.Arrays.fill(responseBuffer, (byte) 0); // clear buffer
            
            // receive a packet (or process an incoming command)
            try {
                multicastSocket.receive(response);
            } catch (IOException e) {
                // check for commands to be run
                Command cmd = commandQueue.poll();
                if (cmd == null) {
                    //activity.ipc.error(e);
                    return;
                }

                // reopen the socket
                try {
                    openSocket();
                } catch (IOException e1) {
                	send(MsgMap.T.NETWORK_SOCKET_OPEN_ERROR.ordinal(), e1);
                    //activity.ipc.error(new RuntimeException("socket reopen: "+e1.getMessage()));
                    return;
                }

                // process commands
                if (cmd instanceof QueryCommand) {
                    try {
                        query(((QueryCommand)cmd).host);
                    } catch (IOException e1) {
                        //activity.ipc.error(e1);
                    }
                } else if (cmd instanceof QuitCommand) {
                    break;
                }
                
                continue;
            }

            /*
            Log.v(TAG, String.format("received: offset=0x%04X (%d) length=0x%04X (%d)", response.getOffset(), response.getOffset(), response.getLength(), response.getLength()));
            Log.v(TAG, Util.hexDump(response.getData(), response.getOffset(), response.getLength()));
            */
            
            // ignore our own packet transmissions.
            if (localAddresses.contains(response.getAddress())) {
                continue;
            }
            
            // parse the DNS packet
            DNSMessage message;
            try {
                message = new DNSMessage(response.getData(), response.getOffset(), response.getLength());
            } catch (Exception e) {
            	send(MsgMap.T.DNS_PARSE_ERROR.ordinal(), e);
                //activity.ipc.error(e);
                continue;
            }

            // send the packet to the UI
            Packet packet = new Packet(response, multicastSocket);
            packet.description = message.toString().trim();
            send(MsgMap.T.PACKET.ordinal(), packet);
            //activity.ipc.addPacket(packet);
        }
        
        // release the multicast lock
        multicastLock.release();
        multicastLock = null;

        Log.v(thisName, "stopping network thread");
    }
    
    /**
     * Transmit an mDNS query on the local network.
     * @param host
     * @throws IOException
     */
    private void query(String host) throws IOException {
        byte[] requestData = (new DNSMessage(host)).serialize();
        DatagramPacket request =
            new DatagramPacket(requestData, requestData.length, InetAddress.getByAddress(MDNS_ADDR), MDNS_PORT);
        multicastSocket.send(request);
    }

    // inter-process communication
    // poor man's message queue

    private Queue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();
    private static abstract class Command {
    }
    private static class QuitCommand extends Command {}
    private static class QueryCommand extends Command {
        public QueryCommand(String host) { this.host = host; }
        public String host;
    }
    public void submitQuery(String host) {
        commandQueue.offer(new QueryCommand(host));
        multicastSocket.close();
    }
    public void submitQuit() {
        commandQueue.offer(new QuitCommand());
        if (multicastSocket != null) {
            multicastSocket.close();
        }
    }
    
}///
