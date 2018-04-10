package net.miscfolder.protopack.support;

import java.io.IOException;
import java.net.*;

public abstract class SocketProxyURLConnection extends URLConnection{
	private final Proxy proxy;

	protected SocketProxyURLConnection(URL url, Proxy proxy){
		super(url);
		this.proxy = proxy;
	}

	protected Socket newProxiedSocket() throws IOException{
		InetSocketAddress address =
				new InetSocketAddress(url.getHost(),
						url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
		return SocketProxy.proxy(address, proxy, getConnectTimeout());
	}
}
