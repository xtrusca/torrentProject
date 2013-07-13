package edu.rutgers.cs.cs352.bt.util;
import java.net.*;
import java.io.*;

public class Contact {
	
	public static String trackerRequest;
	
	public static void sendGetRequest(){
		 Socket s = null;
	     PrintWriter out = null;
	     BufferedReader in = null;        
	     BufferedReader stdIn = null;

		try{
			s = new Socket(trackerRequest, Data.portNumber);
			out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
			trackerRequest = stdIn.readLine();
        	out.println(trackerRequest);
        	System.out.println("echo: " + in.readLine());
        	
		}catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + trackerRequest);
            System.exit(1);
        }catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + trackerRequest);
            System.exit(1);
        }
        
        
        	
    }

	
	public static void formGetRequest(int i){
		
		String event = determineEvent(i);
		trackerRequest = Data.announce_url + "?port=" + Data.portNumber + "&info_hash=" + Data.info_hash_string + "&uploaded="
							+ Data.uploaded + "&downloaded=" + Data.downloaded + "&left=" + Data.dataLeft;
		
		if(event.length() > 0){
			trackerRequest += "&event=" + event;
		}
		
		System.out.println(trackerRequest);
	}
	
	public static String determineEvent(int i){
		switch(i){
			case 1: return "STARTED";
			case 2: return "COMPLETED";
			case 3: return "STOPPED";
			default: return "";
		}
	}
	
}
