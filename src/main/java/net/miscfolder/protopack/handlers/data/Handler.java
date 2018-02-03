package net.miscfolder.protopack.handlers.data;

import java.net.URL;
import java.net.URLConnection;

import net.miscfolder.protopack.support.SelfContainedURLHandler;

public class Handler extends SelfContainedURLHandler{

	protected URLConnection openConnection(URL u){
		return new DataURLConnection(u);
	}

}
