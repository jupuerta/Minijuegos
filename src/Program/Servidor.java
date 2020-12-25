package Program;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	public static void main(String[] args) {				
		Socket client=null;
		String mens;
		DataOutputStream imagen=null;
		try(ServerSocket serverSocket = new ServerSocket(55558);){			
			while (true) {
				client= serverSocket.accept();
				try(DataInputStream in = new DataInputStream(client.getInputStream());){										
					mens=in.readUTF();
					System.out.println(mens);
					
					
				}catch (IOException e) {
					System.out.println(e);
				}
			}
		}catch (IOException e) {
			System.out.println(e);
		}
	}
}
