package wang.angi.sample.mina.server.one;

import org.junit.Ignore;
import org.junit.Test;

public class MinaSocketServerTest {

	@Test
	@Ignore
	public void TestMinaSocketServer() throws Throwable {
		MinaSocketServer minaSocketServer = new MinaSocketServer();
		minaSocketServer.init();
		System.in.read();
	}

}
