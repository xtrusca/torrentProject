import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.lang.Object;

import edu.rutgers.cs.cs352.bt.exceptions.BencodingException;
import edu.rutgers.cs.cs352.bt.util.*;



public class RUBTClient {

	public static void main(String[] args) throws IOException{
		
		String torrentFile = "cs352.png.torrent";
		String fileName = "finalName";
		byte[] getMetaInfo = getBencodedBytes(torrentFile);
		TorrentInfo torrentInfo = null;
		
		try {
			torrentInfo = new TorrentInfo(getMetaInfo);
		} catch (BencodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] b = new byte[torrentInfo.info_hash.remaining()];
		torrentInfo.info_hash.get(b);

		for(int i = 0; i<b.length; i++){
			System.out.print((char)b[i]);
		}
		System.out.println();
		sendGetRequest(torrentInfo);
		
		
		
	
		
	}
	
	private static void sendGetRequest(TorrentInfo decodedMetainfo) throws IOException{
		
		Socket trackerurl = null;
		PrintWriter out = null;
		BufferedReader fromTracker = null;
	    OutputStreamWriter output = null;
	    StringBuilder string = null;
	    String line = null;

		
			try {
				URL trackerURL = decodedMetainfo.announce_url;
				HttpURLConnection con = (HttpURLConnection) trackerURL.openConnection();
				con.setRequestMethod("GET");
				con.setDoOutput(true);
				con.setReadTimeout(10000);
				con.connect();
				
				
				
				output = new OutputStreamWriter(con.getOutputStream());
				output.write("");
				output.flush();
				
				fromTracker = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				string = new StringBuilder();
				
				while((line = fromTracker.readLine()) !=null){
					string.append(line + '\n');
				}
				
				System.out.println(string.toString());
			} catch (UnknownHostException e) {
				System.err.println("Host is not found");
				System.exit(1);
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Cannot get I/O from " + decodedMetainfo.announce_url.toString());
				e.printStackTrace();
			}
	
	}
	/**
	 * Opens the file and parses the metainfo into a byte array
	 * @params String file_name is the name of the file being parsed
	 * @return a byte array with all the encoded bytes
	 */
	private static byte[] getBencodedBytes(String file_name){
		
		File file = new File(file_name);
		long fileSize = -1;
		byte[] fileBytes = null;
		InputStream file_stream;

		fileSize = file.length();
		fileBytes = new byte[(int)fileSize];
				
		try {				
			file_stream = new FileInputStream(file);
			
			if(!file.exists()){
				System.err.println("Error:The File" + file_name + " does not exist. Please reload the program with the correct filename");
			}
			file_stream.read(fileBytes);
			
			for(int i = 0; i<fileBytes.length; i++){
				System.out.print((char)fileBytes[i]);
			}
			System.out.println();
			
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
			e.printStackTrace();
		} catch (IOException e1){
			System.out.println("Error, cannot read the file.");
			e1.printStackTrace();
		}
		return fileBytes;
	}
	
}
