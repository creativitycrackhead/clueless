import javax.swing.JFrame;

public class ClientTest {
	public static void main(String[] args) {
		Client inGameMsgWindow; //instantiate the messenger system
		inGameMsgWindow = new Client("127.0.0.1"); //sets the port for the messenger system
		inGameMsgWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //this will close the window upon finishing
		inGameMsgWindow.runConnections(); // actually runs the code to open it
		
		
	}
}
