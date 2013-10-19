import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Channel {
	private String channel_name;
	private HashMap<String, ClientThread> users;
	
	public Channel(String channel_name) {
		setChannel_name(channel_name);
		users = new HashMap<String, ClientThread>();
	}
	
	/**
	 * @return the channel_name
	 */
	public String getChannel_name() {
		return channel_name;
	}
	/**
	 * @param channel_name the channel_name to set
	 */
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	/**
	 * @return the users
	 */
	public Iterable<ClientThread> getUsers() {
		return users.values();
	}
	
	public void sendMessage(String sender_user, String message) throws UnsupportedEncodingException  { //send message to all channel users
		
		for ( ClientThread user : getUsers())
		{
			if(user.getUserName() != sender_user)
				user.sendMessage(sender_user, message);
		}
  }

	public void addUser(ClientThread joined_user) {
		users.put(joined_user.getUserName(), joined_user);
	}

	public void removeUser(String user_name) {
		users.remove(user_name);
	}
}
