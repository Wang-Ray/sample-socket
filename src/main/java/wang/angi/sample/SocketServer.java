package wang.angi.sample;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	public static void main(String[] args) throws Exception {

		ServerSocket serverSocket = new ServerSocket(1234);
		System.out.println("服务器已经启动，端口号：1234");
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println(socket.getSendBufferSize());
			System.out.println(socket.getReceiveBufferSize());
			InputStream in = socket.getInputStream();
			System.out.println(in.read());
			Thread.sleep(20*1000);
			OutputStreamWriter outWriter = new OutputStreamWriter(socket.getOutputStream());
			outWriter.write(1);
			outWriter.write(1);
			outWriter.flush();
			socket.close();
		}
	}
}
