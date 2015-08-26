package chirow.vertx3.tcpserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirow.vertx3.tcpserver.SocketCommandController;


public class SocketCommandController extends AbstractVerticle {

	//logback + slf4j
	static final Logger logger = LoggerFactory.getLogger(SocketCommandController.class);
		
    private String name = null;
    private String name_log = null;
    private NetSocket netSocket = null;
    private String remoteAddr = null;
    
    
    public SocketCommandController(String name, NetSocket netSocket) {
        this.name = name;
        this.name_log = "[" + name + "] ";
        this.netSocket = netSocket;
        this.remoteAddr = netSocket.remoteAddress().toString();
    }

    public void start(Future<Void> startFuture) {
    	
    	logger.info("[SocketCommandController-start()] START! > " + this.name);
    	
    	EventBus eb = vertx.eventBus();
        
        netSocket.handler(new Handler<Buffer>() {

            @Override
            public void handle(Buffer inBuffer) {
            	
            	// Reading Data From The Socket
            	logger.debug(name_log + "----------------------------------------------------------------------------");
            	//logger.debug(name_log + "incoming data: " + inBuffer.length());
                String recvPacket = inBuffer.getString(0, inBuffer.length());
            	
                //logger.debug(name_log + "read data: " + recvPacket);
                logger.info(name_log + "RECV[" + recvPacket.length() + "]> \n" + recvPacket + "\n");
                
                
                // -- Packet Analyze ----------------------------------------------------------------
                // Writing received message to event bus
				
                JsonObject jo = new JsonObject();
                
                jo.put("recvPacket", recvPacket);
                
                eb.send("PD_Trap" + remoteAddr, jo);

                // -- RESPONSE, Send Packet to Target -----------------------------------------------
                eb.consumer(remoteAddr + "Trap_ACK", message -> {
                	writeToTarget(message.body().toString(), "Trap_ACK");
                });
                
                eb.consumer(remoteAddr + "Trap_NACK", message -> {
                	writeToTarget(message.body().toString(), "Trap_NACK");
                });
                
            }
        });
        
    	
    }
    
    private void writeToTarget(String sendPacket, String cmd)
    {
    	logger.debug(name_log + "writeToDCU(), Command: " + cmd);
    	
        // Writing Data to The Socket
        Buffer outBuffer = Buffer.buffer();
        outBuffer.setString(0, sendPacket);
        logger.info(name_log + "SEND[" + outBuffer.length() + "]> \n" + outBuffer.toString() + "\n");

        netSocket.write(outBuffer);
    
    }

}
