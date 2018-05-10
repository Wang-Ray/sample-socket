package wang.angi.sample;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket();
//		socket.setSoTimeout(5*1000);
//		socket.setTcpNoDelay(true);
		socket.setSendBufferSize(81920);
		System.out.println(socket.getSendBufferSize());
		System.out.println(socket.getReceiveBufferSize());
//		socket.connect(new InetSocketAddress("192.168.104.211", 6666));
		socket.connect(new InetSocketAddress("127.0.0.1", 1234));
		OutputStream out = socket.getOutputStream();
		OutputStreamWriter outWriter = new OutputStreamWriter(out);
		outWriter.write(2);
		outWriter.write(2);
		outWriter.flush();
		out.flush();
//		out.write(2);
//		out.flush();
		InputStream in = socket.getInputStream();
		System.out.println(in.read());
		Thread.sleep(10*1000);
		
		socket.close();
	}
}
