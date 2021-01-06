package Pruebas;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import Program.Servidor;

public class S {

	public static void main(String[] args) {
		
		try(ServerSocket serverSocket = new ServerSocket(55558);){			
			while (true) {
				Socket client= serverSocket.accept();
				try(BufferedReader dis = new BufferedReader(new InputStreamReader(client.getInputStream()));
					DataOutputStream dos = new DataOutputStream(client.getOutputStream())){
						
						if(dis.readLine()!=null) {
							System.out.println("Esto no es null");
						}else {
							System.out.println("Esto es null");
						}
						
					}catch (IOException e) {
								System.out.println(e);
					}
			}
		}catch (IOException e) {
			System.out.println(e);
		}

	}

}
