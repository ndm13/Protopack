package net.miscfolder.protopack.handlers.javascript;

import java.net.URL;
import java.net.URLConnection;

import net.miscfolder.protopack.support.SelfContainedURLHandler;

public class Handler extends SelfContainedURLHandler{
	@Override
	protected URLConnection openConnection(URL u){
		return new JavaScriptURLConnection(u);
	}
}
