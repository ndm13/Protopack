package net.miscfolder.protopack.support;

import java.net.URL;
import java.net.URLStreamHandler;

public abstract class SelfContainedURLHandler extends URLStreamHandler{

	@Override
	protected int getDefaultPort(){
		return -1;
	}

	@Override
	protected void parseURL(URL u, String spec, int start, int limit){
		setURL(u, u.getProtocol(), null, -1, null, null,
				spec.substring(start), null, null);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implNote
	 * Because {@link URLStreamHandler#hostsEqual(URL, URL)} resolves the host
	 * names to determine equality, we'll save overhead since the host names
	 * are always null.
	 */
	@Override
	protected boolean hostsEqual(URL u1, URL u2){
		return true;
	}
}
