package server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	//constructor
	public Server(){
		super("ClueLess Instant Messenger - Player One"); //This is the title of the program
		userText = new JTextField(); //this creates the space where the users can text
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sendMsg(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(500, 300); //Sets the window size
		setVisible(true);
	}
	
	public void runConnections(){
		try{
			server = new ServerSocket(6789, 100); //6789 is a dummy port for testing, this can be changed. The 100 is the maximum people waiting to connect.
			while(true){
				try{
					//Trying to connect and have conversation
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					displayedMsg("\n Server ended the connection! ");
				} finally{
					closeConnection(); //Changed the name to something more appropriate
				}
			}
		} catch (IOException ioException){
			ioException.printStackTrace();
		}
	}
	//wait for connection, then display connection information
	private void waitForConnection() throws IOException{
		displayedMsg(" Waiting for someone to connect... \n");
		connection = server.accept();
		displayedMsg(" Now connected to " + connection.getInetAddress().getHostName());
	}
	
	//get stream to send and receive data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		
		displayedMsg("\n Streams are now setup \n");
	}
	

	
	public void closeConnection(){
		displayedMsg("\n Closing Connections... \n");
		allowTypingArea(false);
		try{
			output.close(); //Closes the output path to the client
			input.close(); //Closes the input path to the server, from the client.
			connection.close(); //Closes the connection between you can the client
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//Send a mesage to the client
	private void sendMsg(String message){
		try{
			output.writeObject("Player One - " + message);
			output.flush();
			displayedMsg("\nPlayer One -" + message);
		}catch(IOException ioException){
			chatWindow.append("\n ERROR: CANNOT SEND MESSAGE, PLEASE RETRY");
		}
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException{
		String message = " Player Two - You are now connected! ";
		sendMsg(message);
		allowTypingArea(true);
		do{
			try{
				message = (String) input.readObject();
				displayedMsg("\n" + message);
			}catch(ClassNotFoundException classNotFoundException){
				displayedMsg("The user has sent an unknown object!");
			}
		}while(!message.equals("Player Two - END"));
	}
	
	
	private void allowTypingArea(final boolean tof){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(tof);
				}
			}
		);
	}
	
	//update chatWindow
	private void displayedMsg(final String text){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(text);
				}
			}
		);
	}
}