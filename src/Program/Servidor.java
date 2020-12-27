package Program;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor implements Runnable{
	
	private int id;
	private static String opcionJugador1=null;
	private static String opcionJugador2=null;
	private Socket cliente;
	private static CyclicBarrier barrera = new CyclicBarrier(2);
	private static int jugadoresPTT=0;
	private static int jugadoresTER=0;
	
	public Servidor(int id,Socket cliente) {
		this.id=id;
		this.cliente=cliente;
	}
	
	public boolean comprobar() {
		if(opcionJugador2!=null) {
			return true;
		}
		return false;
	}
	
	//Lo pongo con synchronized ya que puede dar problemas de indeterminación
	public synchronized void incJugadorPTT() {
		jugadoresPTT++;
	}
	
	public synchronized void incJugadorTER() {
		jugadoresTER++;
	}
	
	public boolean empatePTT(String opcion1, String opcion2) {
		if(opcion1.equals("Piedra") && opcion2.equals("Piedra")) {
			return true;
		}else {
			if(opcion1.equals("Tijera") && opcion2.equals("Tijera")) {
				return true;
			}else {
				if(opcion1.equals("Papel") && opcion2.equals("Papel")) {
					return true;
				}else {
					return false;
				}
			}
		}
	}
	
	public boolean ganadorPTT(String opcion1, String opcion2) {
		if(opcion1.equals("Piedra") && opcion2.equals("Tijera")) {
			return true;
		}else {
			if(opcion1.equals("Tijera") && opcion2.equals("Papel")) {
				return true;
			}else {
				if(opcion1.equals("Papel") && opcion2.equals("Piedra")) {
					return true;
				}else {
					return false;
				}
			}
		}
	}
	
	public void run() {
		String mens;
		try(DataInputStream dis = new DataInputStream(cliente.getInputStream());
			DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());){										
			mens=dis.readUTF();
			
			//Para que un cliente espere hasta que se una alguien con quien jugar
			if(mens.equals("PPT")) {
				
				incJugadorPTT();
				barrera.await();//Asi conseguimos que los dos empiecen a la vez
				
				dos.writeUTF("continua");//Mensaje de que empiecen
				
				String opcion=dis.readUTF();
								
				if(opcionJugador1==null) {
					opcionJugador1=opcion;
					
					Thread.sleep(50);//Para que le de tiempo a que el otro hilo dé valor a opcionJugador2
					
					if(ganadorPTT(opcionJugador1, opcionJugador2)) {
						dos.writeUTF("Ganador");
					}else {
						if(empatePTT(opcionJugador1, opcionJugador2)) {
							dos.writeUTF("Empate");
						}else {
							dos.writeUTF("Perdedor");
						}
					}
				}else {
					opcionJugador2=opcion;
					
					if(ganadorPTT(opcionJugador2, opcionJugador1)) {
						dos.writeUTF("Ganador");
					}else {
						if(empatePTT(opcionJugador2, opcionJugador1)) {
							dos.writeUTF("Empate");
						}else {
							dos.writeUTF("Perdedor");
						}
					}
				}
			}
			
		}catch (IOException | InterruptedException | BrokenBarrierException e) {
			System.out.println(e);
		}
		
	}
	
	public static void main(String[] args) {
		int id=0;
		int hilos= Runtime.getRuntime().availableProcessors();		
		ExecutorService pool= Executors.newFixedThreadPool(hilos);
		Socket client=null;
		
		try(ServerSocket serverSocket = new ServerSocket(55558);){			
			while (true) {
				client= serverSocket.accept();
				Runnable hilo = new Servidor(id, client);
				pool.execute(hilo);
				
				if(Servidor.jugadoresPTT==2) {
					Servidor.barrera = new CyclicBarrier(2);
				}
				id++;
			}
		}catch (IOException e) {
			System.out.println(e);
		}
	}
}
