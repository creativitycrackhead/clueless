package server;
import javax.swing.JFrame;

public class ServerTest {
	public static void main(String args[]) {
		Server inGameServerMsgWindow = new Server(); //this instantiates a new server window
		inGameServerMsgWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		inGameServerMsgWindow.runConnections(); //actually runs the proper code on getting the service running
	}
}
