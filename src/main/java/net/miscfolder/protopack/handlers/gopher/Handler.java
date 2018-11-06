package net.miscfolder.protopack.handlers.gopher;

import java.net.*;
import java.util.*;

public class Handler extends URLStreamHandler{
	private static final Map<Character,String> TYPE_MAP = Collections.unmodifiableMap(
			new HashMap<Character,String>(){{
				/* Official Formats */
				put('0',"text/plain");
				put('1',"text/x-gopher-menu");
				// 2 - CCSO Nameserver
				// 3 - Gopher error message
				put('4',"application/mac-binhex40");
				// 5 - DOS binary/archive (really vague - process)
				put('6',"application/x-uuencode");
				put('7',"text/x-gopher-menu");  // Search server
				// 8 - Telnet
				// 9 - Binary file (really generic - process)
				put('+',"text/x-gopher-menu");  // Mirror
				put('g',"image/gif");
				// I - Image (requires more processing)
				// T - Telnet 3270

				/* Unofficial Formats */
				put('h',"text/html");
				// i - Gopher info message
				// s - Sound (requires more processing)
				// d - Document (requires more processing)
				// ; - Video (requires more processing)
				put('c',"text/calendar");
				put('M',"message/rfc822");  // MIME-encoded
			}});
	// FIXED: support unknown types
	private static final List<Character> UNKNOWN_SUFFIX_TYPES =
			List.of((char)0, '5', '9', 'I', 's', 'd', ';');

	/**
	 * Attempts to assign a MIME type to a Gopher item based on its item type
	 * and file name.
	 *
	 * The following assumptions are made based on the type data commonly
	 * implemented by other gopher clients:
	 *  - 0 - text/plain
	 *  - 1/7/+ - text/x-gopher-menu
	 *    NOTE: this is a custom MIME type for gopher menus, covering standard,
	 *    search-server, and mirror item types (although in practice, + should
	 *    not be sent)
	 *  - 4 - application/mac-binhex40
	 *  - 6 - application/x-uuencode
	 *  - g - image/gif
	 *  - h - text/html
	 *  - c - text/calendar
	 *  - M - message/rfc882
	 *
	 * The following types are too generic to be mapped directly, so are passed
	 * to {@link URLConnection#guessContentTypeFromName(String)} to generate a
	 * specific type:
	 *  - 5 - DOS Binary/Archive
	 *    Never very well defined in the original spec.  Not really observed in
	 *    the wild.
	 *  - 9 - Binary file
	 *    Most clients return application/octet-stream, but since it's used for
	 *    a wide range of application/* types, we try to resolve it.
	 *  - I - Image file
	 *    Java 7 client resolved to image/gif, although it's used in the wild
	 *    for image/bmp and image/png, among others.
	 *  - s/d/; - Sound, Document, Video
	 *    All implemented outside of spec with no regard to data type.
	 *
	 * If this method cannot resolve a type, it returns the generic MIME
	 * application/octet-stream.
	 * @param c     the Gopher item type
	 * @param name  the name of the file
	 * @throws NullPointerException
	 *      if the name is null and the type requires a name to resolve
	 * @return a valid MIME type
	 */
	public static String resolveMIME(char c, String name){
		// FIXED: support unknown types
		String type = c == 0 ? null : TYPE_MAP.get(c);
		if(type != null) return type;
		if(UNKNOWN_SUFFIX_TYPES.contains(c)){
			return URLConnection.guessContentTypeFromName(Objects.requireNonNull(name));
		}
		return "application/octet-stream";
	}

	@Override
	protected int getDefaultPort(){
		return 70;
	}

	@Override
	protected URLConnection openConnection(URL u){
		return new GopherURLConnection(u, Proxy.NO_PROXY);
	}

	// gopher://host:port/[type-char][selector](%09|?)[query]%09[gopher+]
	@Override
	protected URLConnection openConnection(URL u, Proxy p){
		return new GopherURLConnection(u, p);
	}

}
