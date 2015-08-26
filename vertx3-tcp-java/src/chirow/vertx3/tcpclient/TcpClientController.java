package chirow.vertx3.tcpclient;

import io.vertx.core.AbstractVerticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirow.vertx3.tcpclient.TcpClientController;


public class TcpClientController extends AbstractVerticle {
	
	//logback + slf4j
	static final Logger logger = LoggerFactory.getLogger(TcpClientController.class);
	

    public void start() {
    	
    	logger.info("[TcpClientController-start()] START!");
    	logger.info("[TcpClientController-start()] >>>>>>>>>> Actually not use now.");
    	
    	// 20150826
    	// if you hope to use tcp client, unlock '/* */' tag.
    	// before use tcp-client, need to tcp-server to be connected from tcp-client!
    	// REMIND, first step: Open TCP-Server.
    	//
    	
    	/*
        NetClient tcpClient = vertx.createNetClient();
        
        // Connecting to a Remote Server
        tcpClient.connect(3110, "192.168.0.75",
                new Handler<AsyncResult<NetSocket>>(){

                @Override
                public void handle(AsyncResult<NetSocket> result) {
                    NetSocket socket = result.result();
                    
                    // Writing Data
                    socket.write("GET / HTTP/1.1\r\nHost: jenkov.com\r\n\r\n");
                    
                    // Reading Data
                    socket.handler(new Handler<Buffer>(){
                        @Override
                        public void handle(Buffer buffer) {
                            System.out.println("Received data: " + buffer.length());

                            System.out.println(buffer.getString(0, buffer.length()));

                        }
                    });
                }
            });
        */
        
        // Closing the TCP Connection
        //tcpClient.close();
    }
}