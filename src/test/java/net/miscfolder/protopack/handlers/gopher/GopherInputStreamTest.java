package net.miscfolder.protopack.handlers.gopher;

import java.io.IOException;
import java.net.URL;

import net.miscfolder.protopack.ProtoPack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GopherInputStreamTest{
	@BeforeAll
	static void setup(){
		ProtoPack.install();
	}

	@Test
	void readMenuData() throws IOException{
		new URL("gopher://gopher.quux.org:70/1/Archives")
				.openStream()
				.transferTo(System.out);
	}

	@Test
	void readTextData() throws IOException{
		new URL("gopher://gopher.quux.org/0/About This Server.txt")
				.openStream()
				.transferTo(System.out);
	}
}