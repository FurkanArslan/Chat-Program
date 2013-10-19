import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientThread extends Thread {
	private String user_name;
	private InputProcess input_reader;
	private OutputProcess output_writer;
	private Server server;
	private Channel user_channel;

	public ClientThread(Socket  client, Server server) throws Exception {
		setServer(server);
		input_reader  = new InputProcess( new BufferedReader( new InputStreamReader( client.getInputStream())) ) ;
		output_writer = new OutputProcess( new PrintWriter ( client.getOutputStream(),true) );
	 	user_name  = input_reader.getLine();
     }

    public void sendMessage(String sender_name, String  message) throws UnsupportedEncodingException  {
	    output_writer.writeLine( sender_name + ": " + message);
     }
		
    public String getUserName() {  
        return user_name; 
    }
    
    public void run()  {
		 try    {
			output_writer.writeLine("CONNECTED");
			 
	        while(true)   {
	        	String input = input_reader.getLine();
	        	  
	        	if(input.startsWith("/listchannels")){
	        		listChannels();
	        	}
	        	else if(input.startsWith("/join")){
	        		joinChannel(input);
	        	}
	        	else if(input.startsWith("/listusers")){
	        		listUsers();
	        	}
	        	else if( input.startsWith("/leave") ){
	    			leaveChannel();
	    		}
	        	else if(input.startsWith("/disconnect")){
	        		if(user_channel != null){
	        			leave();
	        		}
	        		closeStreams();
	        		break;
	        	}
	        	else
	        	{
	        		if(user_channel != null)
	        			user_channel.sendMessage(getUserName(), input);
	        	}
	        } 
		 }catch(Exception ex) {
			 ex.printStackTrace();//System.out.println(ex.);
		 }
    }

	private void closeStreams() throws IOException {
		input_reader.closeFile();
		output_writer.closeFile();
	}

	private void leave() {
		user_channel.removeUser( getUserName());
		user_channel = null;
	}
	
	private void leaveChannel() throws UnsupportedEncodingException {
		if(user_channel != null){
			leave();
		}
		else
			output_writer.writeLine("THERE IS NOT A JOINED CHANNEL");
	}
	
	private void listUsers() throws UnsupportedEncodingException {
		if( user_channel != null ){
			for (ClientThread user : user_channel.getUsers()) {
				output_writer.writeLine(user.getUserName());
			}
		}
		else
			output_writer.writeLine("THERE IS NOT A JOINED CHANNEL");
	}

	private void joinChannel(String join_command) throws UnsupportedEncodingException {
		StringTokenizer parser = new StringTokenizer(join_command);
		parser.nextToken();		//ignore join command
		String channel_name    = parser.nextToken();	//read channel name from input
		
		user_channel = getServer().getChannels().get(channel_name);
		if( user_channel != null ){
			user_channel.addUser( this );
			output_writer.writeLine("JOINED");
		}
		else
			output_writer.writeLine("THERE IS NOT SUCH A CHANNEL");
	}

	private void listChannels() throws UnsupportedEncodingException {
		
		Iterable<String> channel_names = getServer().getChannels().keySet();
		for (String channel : channel_names) {
			output_writer.writeLine(channel);
		}
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	} 
   } 

