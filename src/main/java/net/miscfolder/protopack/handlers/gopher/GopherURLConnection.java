package net.miscfolder.protopack.handlers.gopher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.*;

import net.miscfolder.protopack.support.SocketProxy;

public class GopherURLConnection extends URLConnection{
	private final Proxy proxy;
	private InputStream stream;
	private char contentType;
	private String mimeType;

	GopherURLConnection(URL url, Proxy proxy){
		super(url);
		this.proxy = proxy;
		String path = this.url.getPath();
		if(path.length() < 2){
			// Root dir is menu type
			contentType = '1';
		}else if(path.length() == 2 || path.charAt(2) == '/'){
			// Type specified: using that
			contentType = path.charAt(1);
		}else{
			// FIXED: support typeless URLs
			contentType = 0;
		}
		mimeType = Handler.resolveMIME(contentType, url.toExternalForm());
	}

	@Override
	public String getContentType(){
		return mimeType;
	}

	@Override
	public void connect() throws IOException{
		InetSocketAddress address =
				new InetSocketAddress(url.getHost(), url.getPort() == -1 ? 70 : url.getPort());
		Socket socket = SocketProxy.proxy(address, proxy, getConnectTimeout());

		// Request page
		// FIXED: support unofficial "query string" URLs
		OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
		if(url.getPath().length() > 1){
			String path;
			if(url.getPath().length() == 2){
				path = "";
			}else if(url.getPath().charAt(2) == '/'){
				path = url.getPath().substring(2);
			}else{
				// FIXED: support unofficial typeless URLs
				path = url.getPath();
			}
			writer.write(path.replace("%09", "\t")
					.replace('?', '\t') + "\r\n");
		}else{
			writer.write("\r\n");
		}
		writer.flush();

		socket.setSoTimeout(getReadTimeout());
		stream = socket.getInputStream();

		// If we gave up earlier, try using the stream method.
		// Current impl (JDK9) only works on small subset, but
		// maybe it's what we're looking for.
		if(mimeType.equalsIgnoreCase("application/octet-stream")){
			String tempMime = URLConnection.guessContentTypeFromStream(stream);
			if(tempMime != null) mimeType = tempMime;
		}

		connected = true;
	}

	@Override
	public InputStream getInputStream() throws IOException{
		if(!connected) connect();
		return new GopherInputStream(stream);
	}

}
