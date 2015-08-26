package chirow.vertx3.tcpserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.streams.Pump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirow.vertx3.tcpserver.PacketDividerController;
import chirow.vertx3.tcpserver.SocketCommandController;
import chirow.vertx3.tcpserver.TcpServerController;

public class TcpServerController extends AbstractVerticle {
	
	//logback + slf4j
	static final Logger logger = LoggerFactory.getLogger(TcpServerController.class);
		
	@Override
	public void start() throws Exception {
		
		logger.info("[TcpServerController-start()] START!");
		
		
		// Create Vertx Instance
		Vertx vertx = Vertx.factory.vertx();
		
		EventBus eb = vertx.eventBus();
		
			
		// Creating a TCP Server
		NetServer server = vertx.createNetServer();
	        
		int nServerPort = 7575;
        
		
		// Setting a Connect Handler on the TCP Server
        server.connectHandler(new Handler<NetSocket>() {

        	@Override
            public void handle(final NetSocket netSocket) {
            	
            	Pump.pump(netSocket, netSocket).start();
            	
            	logger.info("[" + netSocket.remoteAddress() + "-S] ============================================================================");
            	
            	// Deploy Verticles for Each DCU of Server Connection
            	String S_SC_IP_Port = netSocket.remoteAddress().toString() + "-S_SC";
            	String S_PD_IP_Port = netSocket.remoteAddress().toString() + "-S_PA";
        		vertx.deployVerticle(new SocketCommandController(S_SC_IP_Port, netSocket));
        		vertx.deployVerticle(new PacketDividerController(S_PD_IP_Port, netSocket));
            	
        		//logger.debug("[" + netSocket.remoteAddress() + "-S] Incoming connection! server.hashCode() - " + server.hashCode());
            	//logger.debug("[" + netSocket.remoteAddress() + "-S] Incoming connection! netSocket.localAddress() - " + netSocket.localAddress());
            	//logger.debug("[" + netSocket.remoteAddress() + "-S] Incoming connection! netSocket.hashCode() - " + netSocket.hashCode());
        		logger.info("[" + netSocket.remoteAddress() + "-S] SERVER-CONN - " + netSocket.localAddress());
           	
            	logger.info("[" + netSocket.remoteAddress() + "-S] ============================================================================");
            	                
            }
        });
        
        // Starting the TCP Server with TCP port
        server.listen(nServerPort);
        logger.info("[S] SERVER-LISTEN: " + nServerPort);
        
        
        
        // Closing The TCP Server
//	        server.close(new Handler<AsyncResult<Void>>() {
//	            @Override
//	            public void handle(AsyncResult result) {
//	                if(result.succeeded()){
//	                    //TCP server fully closed
//	                	logger.info("[" + netSocket.remoteAddress() + "-S] server.close()");
//	                }
//	            }
//	        });
	}
}