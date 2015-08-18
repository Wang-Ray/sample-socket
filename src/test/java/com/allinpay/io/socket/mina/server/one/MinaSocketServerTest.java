package com.allinpay.io.socket.mina.server.one;

import org.junit.Test;

public class MinaSocketServerTest {

	@Test
	public void TestMinaSocketServer() throws Throwable {
		MinaSocketServer minaSocketServer = new MinaSocketServer();
		minaSocketServer.init();
		System.in.read();
	}

}
