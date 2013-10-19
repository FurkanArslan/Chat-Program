import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class  Server {
  private HashMap<String, Channel> channels;
  private ServerSocket server;

  public Server(String[] argumans) {
	try {
		
		create_channels(argumans[1]);
		int port_number = Integer.valueOf(argumans[0]).intValue();
		server = new ServerSocket( port_number );
		
	} catch (IOException e) {
		e.printStackTrace();
	}
  }

  private void create_channels(String file_path) { //creating channels
	channels = new HashMap<String, Channel>();
	InputProcess input = new InputProcess(file_path);
	ArrayList<String> channels_names = input.readFile(); //read channels from input file
	
	for (String channel_name : channels_names) {		//create all readed channels
		Channel channel = new Channel(channel_name);
		channels.put(channel_name, channel);
	}
  }

  public void start() throws IOException  {
	  try {
		  listen_request();
	  } catch (Exception e) {
		  e.printStackTrace();
	  } finally{
		  server.close();
	  }
  }
  
  private void listen_request() throws Exception {
	  while( true ) {
	 		 Socket client_socket = server.accept();
	 		 new ClientThread(client_socket, this).start();
	     } 
  }

  public static void main(String[] args) throws Exception {
     new Server(args).start();
  }

/**
 * @return the channels
 */
  public HashMap<String,Channel> getChannels() {
	return channels;
  }
}