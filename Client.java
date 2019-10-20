import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private String message ="";
	private String serverIP;
	private Socket connection;
	
	
	//constructor
	public Client(String host)
	{
		super("Clueless Instant Messenger  - Player Two");
		serverIP=host; 
		userText = new JTextField();
		userText.setEditable(false);;
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMsg(event.getActionCommand());
						userText.setText("");
					}
				}
				
				);
		add(userText,BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(500,300);
		setVisible(true);
		
		
	}
	

	//connect to server
	
	public void runConnections() {
		try {
			
			connectToServer();
			setupStreams();
			whileChatting();
			
		}catch(EOFException eofException) {
			displayedMsg("\nPlayer terminated connection");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally {
			closeCrap();
		}
	}
	
	//connect to server
	private void connectToServer() throws IOException{
		displayedMsg("Attempting connection...\n");
		connection = new Socket(InetAddress.getByName(serverIP),6789);
		displayedMsg("Connected to: "+connection.getInetAddress().getHostName());
	}
	
	//set up streams to send and receive messages
	private void setupStreams() throws IOException{
		output= new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		displayedMsg("\nYour streams are good to go\n");
	}
	//while chatting with server
	private void whileChatting() throws IOException{
		allowTypingArea(true);
		do {
			try {
				message = (String)input.readObject();
				displayedMsg("\n"+message);
			}catch(ClassNotFoundException classnotfoundexception) {
				displayedMsg("\n I don't know that object type");
			}
			
		}while(!message.contentEquals("Player One -END"));
	}
	
	//close the streams and sockets
	private void closeCrap() {
		displayedMsg ("\n closing connections...");
		allowTypingArea(false);
		try {
			output.close();input.close();connection.close();
			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
		//send messages to the server
		public void sendMsg(String message) {
			try {
				output.writeObject("Player Two - "+message);
				output.flush();
				displayedMsg("\nPlayer Two - "+message);
				
				
				
			}catch(IOException ioException)
			{
				chatWindow.append("\nSomething went wrong with message sending");
			}
			
		}

		
		//change/update chatWindow
		private void displayedMsg(final String m) {
			SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							chatWindow.append(m);
						}
					}
					);
		}
		
		//gives user permission to type crap into the text box
		private void allowTypingArea(final boolean tof)
		{
			SwingUtilities.invokeLater(
					new Runnable() {
						public void run()
						{
							userText.setEditable(tof);
						}
					}
					);
		
		}
}