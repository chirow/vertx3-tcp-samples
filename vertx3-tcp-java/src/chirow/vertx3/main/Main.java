package chirow.vertx3.main;

import io.vertx.core.Vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirow.vertx3.main.Main;
import chirow.vertx3.tcpclient.TcpClientController;
import chirow.vertx3.tcpserver.TcpServerController;

public class Main {

	//logback + slf4j
	static final Logger logger = LoggerFactory.getLogger(Main.class);
		
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		logger.info("[Main()] start");
		

		// Create Vertx Instance
		Vertx vertx = Vertx.factory.vertx();

	
		// TCP Server
		vertx.deployVerticle(new TcpServerController());
		Thread.sleep(3000);		// temporarily Sleep
		
		// TCP Client
		vertx.deployVerticle(new TcpClientController());
		Thread.sleep(3000);		// temporarily Sleep
	}

}
