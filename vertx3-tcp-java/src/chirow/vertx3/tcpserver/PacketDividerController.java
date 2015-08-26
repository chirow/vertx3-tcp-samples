package chirow.vertx3.tcpserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirow.vertx3.tcpserver.PacketAnalyzer;
import chirow.vertx3.tcpserver.PacketDividerController;

public class PacketDividerController extends AbstractVerticle {

	//logback + slf4j
	static final Logger logger = LoggerFactory.getLogger(PacketDividerController.class);
		
    private String name = null;
    private String name_log = null;
    private NetSocket netSocket = null;
    private String remoteAddr = null;
    

    public PacketDividerController(String name, NetSocket netSocket) {
        this.name = name;
        this.name_log = "[" + name + "] ";
        this.netSocket = netSocket;
        this.remoteAddr = netSocket.remoteAddress().toString();
    }

    public void start(Future<Void> startFuture) {
    	
    	logger.info("[PacketDividerController-start()] START! > " + this.name);
    	
    	EventBus eb = vertx.eventBus();
    	PacketAnalyzer pa = new PacketAnalyzer();
    	
    	eb.consumer("PD_Trap" + remoteAddr, message -> {
			
    		JsonObject jo = (JsonObject)message.body();
    		String recvMsg = jo.getString("recvPacket");
    		
            logger.debug(name_log + "PD_Trap, RECV[" + recvMsg.length() + "]> \n" + recvMsg);
            
            JsonObject paJo = new JsonObject();
            paJo = pa.reqAnalyzePacket(recvMsg);
            
            logger.debug("paJo.size(): " + paJo.size());
            
             
            
            // RESPONSE, to Target
            if (!paJo.isEmpty()) {
            	logger.debug("nextCmd: " + paJo.getString("nextCmd"));
            	
            	String sendPacket = paJo.getString("sendPacket");
            	logger.debug("[sendData()] sendPacket: " + sendPacket);
            	logger.debug(name_log + "PD_Trap, SEND> " + sendPacket);
            	eb.send(remoteAddr + "Trap_ACK", sendPacket);
            } else {
            	logger.debug(name_log + "PD_Trap, SEND> " + "NACK");
            	String sendPacket = "NACK";
            	eb.send(remoteAddr + "Trap_NACK", sendPacket);
            }
			
        });
    	
    	
        
        
        
    }
    
    
}