package net.miscfolder.protopack.handlers.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Base64;

public class DataURLConnection extends URLConnection{
	private byte[] content;
	private String charset;
	private String mimeType;

	DataURLConnection(URL url){
		super(url);
	}

	@Override
	public synchronized void connect(){
		if(connected) return;
		int comma = url.getPath().indexOf(',');
		String[] pieces = url.getPath().substring(0,comma).split(";", -1);
		boolean base64 = false;
		for(String piece : pieces){
			if(piece.equalsIgnoreCase("base64")){
				base64 = true;
			}else if(piece.startsWith("charset=")){
				charset = piece.substring(piece.indexOf('=') + 1);
			}else{
				mimeType = piece;
			}
		}

		if(mimeType == null || mimeType.isEmpty())
			mimeType = "text/plain";
		if(charset == null || charset.isEmpty())
			charset = "US-ASCII";

		content = url.getPath().substring(comma + 1).getBytes(Charset.forName(charset));

		if(base64){
			content = Base64.getMimeDecoder().decode(content);
		}

		connected = true;
	}

	@Override
	public InputStream getInputStream() throws IOException{
		if(!connected) connect();
		return new ByteArrayInputStream(content);
	}

	@Override
	public String getContentType(){
		if(!connected) connect();
		return mimeType;
	}

	@Override
	public String getContentEncoding(){
		if(!connected) connect();
		return charset;
	}

	@Override
	public long getContentLengthLong(){
		if(!connected) connect();
		return content.length;
	}

	@Override
	public int getContentLength(){
		if(!connected) connect();
		return content.length;
	}
}
