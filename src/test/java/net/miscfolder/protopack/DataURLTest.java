package net.miscfolder.protopack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DataURLTest{
	public static void main(String... args) throws IOException{
		System.out.println("Installing...");
		ProtoPack.install();
		System.out.println("Installed.");

		String[] tests = {
				"data:text/plain;base64,SGVsbG8sIFdvcmxkIQ%3D%3D",
				"data:text/plain;charset=US-ASCII;base64,SGVsbG8sIFdvcmxkIQ%3D%3D",
				"data:,Hello%2C%20World!",
				"data:text/html,%3Ch1%3EHello%2C%20World!%3C%2Fh1%3E",
				"data:text/html,<script>alert('hi');</script>",
				"data:,Hello%2C%20World!",
				"data:text/html,lots of text...<p><a name%3D\"bottom\">bottom</a>?arg=val",
				"data:image/svg+xml;utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 512 512'%3E%3Cpath d='M224%20387.814V512L32 320l192-192v126.912C447.375 260.152 437.794 103.016 380.93 0 521.287 151.707 491.48 394.785 224 387.814z'/%3E%3C/svg%3E"

		};

		for(String test : tests){
			System.out.println(test);
			System.out.println("==========");
			URLConnection connection = new URL(test).openConnection();
			connection.connect();
			System.out.println(connection.getContentType());
			System.out.println(connection.getContentLength());
			System.out.println(connection.getContentEncoding());

			InputStream inputStream = connection.getInputStream();
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while((length = inputStream.read(buffer)) != -1){
				result.write(buffer, 0, length);
			}
			System.out.println(result);
			System.out.println();
		}
	}
}
