package net.miscfolder.protopack.handlers.javascript;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class JavaScriptURLConnection extends URLConnection{
	private static final Charset TRANSCODER_CHARSET = Charset.forName("UTF-8");
	private byte[] content;

	JavaScriptURLConnection(URL url){super(url);}

	@Override
	public synchronized void connect(){
		if(connected) return;
		content = url.getPath().getBytes(TRANSCODER_CHARSET);
	}

	@Override
	public InputStream getInputStream() throws IOException{
		if(!connected) connect();
		return new ByteArrayInputStream(content);
	}

	@Override
	public String getContentType(){
		if(!connected) connect();
		return "application/javascript";
	}

	@Override
	public String getContentEncoding(){
		if(!connected) connect();
		return TRANSCODER_CHARSET.name();
	}

	@Override
	public int getContentLength(){
		if(!connected) connect();
		return content.length;
	}

	@Override
	public long getContentLengthLong(){
		if(!connected) connect();
		return content.length;
	}
}
