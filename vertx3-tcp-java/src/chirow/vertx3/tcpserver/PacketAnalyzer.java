package chirow.vertx3.tcpserver;

import io.vertx.core.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirow.vertx3.tcpserver.PacketAnalyzer;

public class PacketAnalyzer {

	//logback + slf4j
	static final Logger logger = LoggerFactory.getLogger(PacketAnalyzer.class);
		
	public JsonObject reqAnalyzePacket(String param) {
		
		// you can fix parameter type 'String' to 'Byte Array' and whatever.
		
		logger.debug("[reqAnalyzePacket()] param: " + param);
		
		JsonObject jo = new JsonObject();
		
		
		
		// if you want, add user-funcitons.(chekc length, version, and so on.)
		
		
		// RESPONSE
		jo.put("sendPacket", "ACK: " + param);
		

        return jo;
	}
	
}
