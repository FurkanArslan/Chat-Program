import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client implements ActionListener {
	private String user_name;
	private InputProcess input_reader;
	private OutputProcess output_writer;
	private Boolean connected;
	private JTextField text_field;
	private JTextArea text_area;
	private Socket client_socket;
	
    public Client(String user_name) {
    	connected = false;
        this.setUser_name(user_name);
        buildInterface();
    }
    
    private void buildInterface() {
    	 JFrame frame = new JFrame();
    	
    	 text_area = new JTextArea();
    	 text_area.setRows(10);
         text_area.setColumns(50);
         text_area.setEditable(false);
    	 
    	 JScrollPane scroll = new JScrollPane(text_area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	 
    	 frame.add(scroll,"Center");
    	 text_field  = new JTextField(50);
    	 text_field.setText("");
    	 text_field.addActionListener(this);
    	 
    	 JPanel panel = new JPanel(new FlowLayout());
    	 panel.add(text_field);
    	 frame.add(panel,"South");
    	 frame.setSize(500,300);
    	 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	 
    	 frame.setVisible(true); 
    	 frame.pack();
	}
    
	@Override
	public synchronized void actionPerformed(ActionEvent arg0) {
		String command = text_field.getText();
		
			try {
					if(command.startsWith("/")){
						Boolean isContinues = determine_process(command);
						
						if(!isContinues){
							connected = false;
							close_program();
						}
					}
					else{
						if(connected){
							output_writer.writeLine(command);
							command = getUser_name() +": " +command;
						}
						else
							command = "THERE IS NOT ANY CONNECTION";
					}
						
		
				} catch (IOException e) {
					e.printStackTrace();
				}
		
		text_area.append("< " + command + "\n");
		text_field.setText("");
	}
	
	private void close_program() throws IOException {	//close programs
		//input_reader.closeFile();	//close input reader
		output_writer.closeFile();	//close output writer
		//client_socket.close();		//close socket connection	
	}
	
	private boolean determine_process(String line) throws IOException {
		
		if(line.startsWith("/connect")){
			connect_server(line);
		}
		else if( line.startsWith("/listchannels") ){
			output_writer.writeLine(line);
		}
		else if( line.startsWith("/join") ){
			output_writer.writeLine(line);
		}
		else if( line.startsWith("/listusers") ){
			output_writer.writeLine(line);
		}
		else if( line.startsWith("/leave") ){
			output_writer.writeLine(line);
		}
		else if (line.startsWith("/disconnect")){
			output_writer.writeLine(line);
			return false;
		}
			
		return true;
	}
	
	private void connect_server(String server_information) throws IOException { //this function connect entered server and make input and output connections
		StringTokenizer parser = new StringTokenizer(server_information," :");
		parser.nextToken();		//ignore connect command
		String server_name = parser.nextToken();	//read server name from input
		String port_number = parser.nextToken();	//read port number from input
		
		client_socket = new Socket(server_name, Integer.valueOf(port_number)); //open socket connection
		connected = true;
		open_streams( client_socket );  //open input and output streams
		output_writer.writeLine(user_name); //send user name to server
	}
	
	private void open_streams(Socket client_socket) throws IOException {
		input_reader  = new InputProcess( new BufferedReader( new InputStreamReader( client_socket.getInputStream() ) ) );
        output_writer = new OutputProcess(new PrintWriter( client_socket.getOutputStream(),true) );
        new MessagesThread().start();			//start listing from server
	}

	public static void main(final String[] args) {
		new Client(args[0]);
    } 
    
    /**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

    class  MessagesThread extends Thread {
        public void run() {
           while( connected ) {
                try {
                	System.out.println(connected);
                	String input = input_reader.getLine();
                	text_area.append("> " + input + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }
} 