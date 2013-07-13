package edu.rutgers.cs.cs352.bt.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import edu.rutgers.cs.cs352.bt.exceptions.*;

public class Data {
	
	public static TorrentInfo torrentInfo = null;
	public static byte[] info_hash = new byte[20];
	public static String info_hash_string = "";
	public static int portNumber = 0;
	public static String announce_url;
	public static int downloaded = 0;
	public static int uploaded = 0; 
	public static int dataLeft = 0;
	
	
	public Data(){
	
	}
	
	public Data(String torrentFileName, String saveFileName){
		createInfoFile(torrentFileName, saveFileName);
		createInfoHash();
		info_hash_string = convertToHex(info_hash);
		announce_url = torrentInfo.announce_url.toString();
		portNumber = torrentInfo.announce_url.getPort();
		Contact.formGetRequest(0);
		Contact.sendGetRequest();

	}
	
	public static TorrentInfo createInfoFile(String torrentFileName, String saveFileName){
		
		try {
			RandomAccessFile rFile = new RandomAccessFile(torrentFileName, "rw");
			byte[] fileBytes = new byte[(int) rFile.length()];
			rFile.read(fileBytes);
			torrentInfo = new TorrentInfo(fileBytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch(IOException e1){
			System.out.println("File not found");
		} catch(BencodingException b){
			
		}
		return null;
	}
	
	public static void createInfoHash(){
		torrentInfo.info_hash.get(info_hash, 0, info_hash.length);
		torrentInfo.info_hash.rewind();
	}
	
	public static String bufferToString(ByteBuffer buffer) {
		byte[] bufferBytes = new byte[buffer.capacity()];
		buffer.get(bufferBytes, 0, bufferBytes.length);
		String value = new String(bufferBytes);
		return value;
	}
	
	public static String convertToHex(byte[] bytes){
		
		char[] hexValues = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer convertedValue = new StringBuffer();
		
		int i = 0;
		while(i<bytes.length){
			convertedValue.append('%').append(hexValues[(bytes[i] >> 4 & 0x0F)]).append(hexValues[(bytes[i] & 0x0F)]);
			i++;
		}
		System.out.println(convertedValue.toString());
		return convertedValue.toString();
	}

}

