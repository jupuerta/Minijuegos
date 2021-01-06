package Pruebas;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class C {

	public static void main(String[] args) {
		try(Socket s = new Socket("localhost",55558);
				Socket s2 = new Socket("localhost",55558);
			DataInputStream bis = new DataInputStream(s.getInputStream());
				BufferedWriter w2=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));){
				
			w2.write("");
			w2.flush();
				
			
			}
			catch (UnknownHostException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
