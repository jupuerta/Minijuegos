package Program;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor implements Runnable {
	private Socket cliente;
	private static int jugadoresPTT = 0;
	private static String opcionJugador1 = null;
	private static String opcionJugador2 = null;
	private static CyclicBarrier barrera = new CyclicBarrier(2);//El objetivo de esta barrera es esperar a que se unan dos clientes a la partida de piedra papel o tijera
	private static CyclicBarrier opcionJugador = new CyclicBarrier(2);//El objetivo de esta barrera es que al cliente que plasme primero la elección del cliente (piedra, papel o tijera) tenga que esperar a que el otro cliente plasme su elección

	public Servidor(Socket cliente) {
		this.cliente = cliente;
	}

	/*
	 * Pre:
	 * Post: Incrementa la variable jugadoresPTT
	 */
	public synchronized void incJugadorPTT() {
		// Lo pongo con synchronized ya que puede dar problemas de indeterminación
		//aunque la probabilidad de que ocurra es casi nula
		jugadoresPTT++;
	}

	/*GENERADOR DE CARA O CRUZ ALEATORIO
	 * Pre: 
	 * Post: Devuelve un String indicando si el número generado aleatoriamente es mayor o menor que 0,5.
	 * En caso de que sea mayor el String que devuelve es "Cruz", en caso contrario "Cara".
	 */
	public String caraCruz() {
		double random = Math.random();
		if (random < 0.5) {
			return "Cara";
		} else {
			return "Cruz";
		}
	}

	/*CALCULAR EL GANADOR DE LA PARTIDA
	 * Pre: Recibe la opción de los dos clientes
	 * Post: Devuelve true si la opcion1 es la ganadora y false en caso contrario
	 */
	public boolean ganadorPTT(String opcion1, String opcion2) {
		if (opcion1.equals("Piedra") && opcion2.equals("Tijera")) {
			return true;
		} else {
			if (opcion1.equals("Tijera") && opcion2.equals("Papel")) {
				return true;
			} else {
				if (opcion1.equals("Papel") && opcion2.equals("Piedra")) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	
	/*COMPROBAR SI HAY EMPATE ENTRE LOS DOS JUGADORES
	 * Pre: Recibe la opción de los dos clientes
	 * Post: Devuelve true si la opcion1 y la opcion2 son iguales
	 */
	public boolean empatePTT(String opcion1, String opcion2) {
		if (opcion1.equals(opcion2)) {
			return true;
		} else {
			return false;
		}
	}

	public void run() {
		String mens;
		try (DataInputStream dis = new DataInputStream(cliente.getInputStream());
				DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());) {
			mens = dis.readUTF();

			if (mens.equals("PPT")) {

				incJugadorPTT();
				barrera.await();//// Para que un cliente espere hasta que se una alguien con quien jugar

				dos.writeUTF("continua");// Mensaje de que empiecen

				String opcion = dis.readUTF();

				if (opcionJugador1 == null) {
					opcionJugador1 = opcion;

					opcionJugador.await();// Así espera a que el otro cliente de valor a opcionJugador2 para poder continuar y no calcular el resultado con opcionJugador2 en null

					if (ganadorPTT(opcionJugador1, opcionJugador2)) {
						dos.writeUTF("Ganador");
					} else {
						if (empatePTT(opcionJugador1, opcionJugador2)) {
							dos.writeUTF("Empate");
						} else {
							dos.writeUTF("Perdedor");
						}
					}
				} else {
					opcionJugador2 = opcion;

					opcionJugador.await();// Para que el cliente que le dio valor a opcionJugador1 pueda calcular el resultado
					if (ganadorPTT(opcionJugador2, opcionJugador1)) {
						dos.writeUTF("Ganador");
					} else {
						if (empatePTT(opcionJugador2, opcionJugador1)) {
							dos.writeUTF("Empate");
						} else {
							dos.writeUTF("Perdedor");
						}
					}
				}
			} else {
				dos.writeUTF(caraCruz());
			}

		} catch (IOException | InterruptedException | BrokenBarrierException e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		int hilos = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(hilos);
		Socket client = null;

		try (ServerSocket serverSocket = new ServerSocket(55558);) {
			while (true) {
				client = serverSocket.accept();
				
				if (Servidor.jugadoresPTT == 2) {//Para que cuando se haya creado una partida de piedra papel o tijera las barreras se reseteen para que puedan jugar otros clientes
					Servidor.barrera = new CyclicBarrier(2);
					Servidor.opcionJugador = new CyclicBarrier(2);
					Servidor.jugadoresPTT = 0;
				}		
				
				Runnable hilo = new Servidor(client);
				pool.execute(hilo);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
