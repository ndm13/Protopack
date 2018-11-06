package net.miscfolder.protopack.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SocketProxy{
	public static Socket proxy(InetSocketAddress destination, Proxy proxy, int timeout) throws IOException{
		if(proxy == null) proxy = Proxy.NO_PROXY;
		InetSocketAddress proxyAddress = (InetSocketAddress) proxy.address();

		switch(proxy.type()){
			case DIRECT:    // No proxy
				Socket directSocket = new Socket();
				directSocket.connect(destination, timeout);
				return directSocket;
			case SOCKS:     // Simple SOCKS tunnel
				Socket socksProxySocket = new Socket(proxy);
				socksProxySocket.connect(destination, timeout);
				return socksProxySocket;
			case HTTP:      // HTTP Direct
				StringBuilder headers = new StringBuilder("CONNECT ")
						.append(destination.getHostName()).append(':').append(destination.getPort());

				PasswordAuthentication auth = Authenticator.requestPasswordAuthentication(proxyAddress.getHostName(),
						proxyAddress.getAddress(), proxyAddress.getPort(), "http",
						proxyAddress.getHostString(), "http");
				if(auth != null){
					headers.append(" HTTP/1.0\nProxy-Authorization:Basic ");
					byte[] userBytes = (auth.getUserName() + ":").getBytes(StandardCharsets.UTF_8);
					ByteBuffer passBytes = StandardCharsets.UTF_8.encode(CharBuffer.wrap(auth.getPassword()));
					ByteBuffer authBytes = ByteBuffer.allocate(userBytes.length + passBytes.capacity());
					authBytes.put(userBytes);
					authBytes.put(passBytes);
					headers.append(Base64.getEncoder().encode(authBytes));
				}
				headers.append("\n\n");

				Socket httpProxySocket = new Socket();
				httpProxySocket.connect(proxyAddress, timeout);
				httpProxySocket.getOutputStream().write(headers.toString().getBytes());

				InputStream proxyInputStream = httpProxySocket.getInputStream();
				String response = streamToString(proxyInputStream);
				if(response.length() == 0)
					throw new SocketException("HTTP CONNECT not supported");
				if(response.contains("200")){
					proxyInputStream.skip(proxyInputStream.available());
					return httpProxySocket;
				}
				throw new SocketException("HTTP CONNECT failed; check authorization for realm " +
						proxyAddress.getHostString());
			default:
				throw new SocketException("Cannot create socket for proxy type: " + proxy.type());
		}
	}

	private static String streamToString(InputStream stream) throws IOException{
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while((length = stream.read(buffer)) != -1){
			result.write(buffer, 0, length);
		}
		return result.toString(StandardCharsets.UTF_8);
	}
}
