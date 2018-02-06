package net.miscfolder.protopack.handlers.gopher;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import net.miscfolder.protopack.support.RingBuffer;

// Maintains a three-character buffer to avoid printing
// full-stop
class GopherInputStream extends InputStream{
	private static final int BUFFER_SIZE = 5;
	private final Pattern TRIM_PATTERN = Pattern.compile(".*\r\n\\.(\r\n)?");

	private final InputStream is;
	private final RingBuffer buffer = new RingBuffer(BUFFER_SIZE);

	private volatile boolean started = false, ended = false;

	GopherInputStream(InputStream is){
		this.is = is;
	}

	@Override
	public int read() throws IOException{
		if(!started){
			preload();
			started = true;
		}
		if(ended) return -1;
		int read = is.read();
		if(read == -1){
			if(TRIM_PATTERN.matcher(buffer.toString()).matches()){
				ended = true;
				return -1;
			}
		}

		int buffered = buffer.feed(read);
		if(buffered == -1)
			ended = true;
		return buffered;
	}

	private void preload() throws IOException{
		int i=0, read;
		while(i++<BUFFER_SIZE && (read = is.read()) != -1)
			buffer.feed(read);
		while(i++<BUFFER_SIZE)
			buffer.feed(-1);
	}
}
