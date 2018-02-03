package net.miscfolder.protopack.handlers.gopher;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import net.miscfolder.protopack.ProtoPack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandlerTest{

	@BeforeAll
	static void setUp(){
		ProtoPack.install();
	}

	@Test
	void resolveMIME(){
		assertEquals("text/x-gopher-menu", Handler.resolveMIME('1', null),
				"Memu MIME type incorrect");
		assertEquals("text/plain", Handler.resolveMIME('0', null),
				"Text MIME type incorrect");
		// TODO stop being lazy
		assertEquals("image/jpeg", Handler.resolveMIME('I', "gopher://dummy/I/file.jpg"),
				"JPEG incorrectly resolved");
		assertEquals("application/zip", Handler.resolveMIME('9', "gopher://dummy/I/file.zip"),
				"ZIP incorrectly resolved");
	}

	@Test
	void connect() throws IOException{
		String[] tests = {
				"gopher://gopher.quux.org/",
				"gopher://gopher.quux.org:70/1/Archives",
				"gopher://gopher.quux.org/0/About This Server.txt",
				"gopher://gopher.quux.org/9/Software/Emulators/CoCo/DOS/cc-utils.zip",
				"gopher://gopher.quux.org/h/Software/Emulators/CoCo/emulators.html",
				"gopher://gopher.quux.org/4/Software/Web/Mosaic/Mac/NCSAMosaic30b4.hqx",
				"gopher://sdf.org/I/users/atorrejo/Infrared/Badge.jpg"
		};
		for(String test : tests){
			URLConnection connection;
			assertNotNull(connection = new URL(test).openConnection(),
					"Connection could not be opened to " + test);
			assertTrue(connection.getInputStream().read(new byte[512]) > 0,
					"No bytes read from " + test);
			assertNotNull(connection.getContentType(),
					"Content type not set for " + test);
		}
	}

}