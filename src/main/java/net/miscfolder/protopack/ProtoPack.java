package net.miscfolder.protopack;

import net.miscfolder.protopack.handlers.Handlers;

public final class ProtoPack{
	private static final String KEY = "java.protocol.handler.pkgs";
	private static final String BASE_PKG = Handlers.class.getPackage().getName();
	private static volatile boolean installed = false;

	public static void install(){
		if(installed) return;
		synchronized(System.getProperties()){
			if(installed) return;
			if(System.getProperties().contains(KEY)){
				String current = System.getProperty(KEY);
				if(!current.contains(BASE_PKG))
					System.setProperty(KEY, current + '|' + BASE_PKG);
			}else{
				System.setProperty(KEY, BASE_PKG);
			}
			installed = true;
		}
	}
}
