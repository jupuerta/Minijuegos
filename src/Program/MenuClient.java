package Program;

import java.awt.FlowLayout;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Parte de la interfaz esta sacada de Internet
public class MenuClient {
	private JFrame mainFrame;
	private JLabel esperaLabel;
	private JLabel headerLabel;
	private JPanel controlPanel;
	private String eleccion;
	private static CyclicBarrier barrera = new CyclicBarrier(2);//El objetivo de esta barrera es que el programa principal no continue hasta que el cliente pulse un botón

	public MenuClient() {
		mainFrame = new JFrame("Menu");
		mainFrame.setSize(400, 200);
		mainFrame.setLayout(new GridLayout(4, 1));
		esperaLabel = new JLabel("", JLabel.CENTER);
		headerLabel = new JLabel("", JLabel.CENTER);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(esperaLabel);
		mainFrame.setVisible(true);

		showEventDemo();
		try {
			barrera.await();//El programa se para hasta que el cliente elija a lo que quiere jugar
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		esperaLabel.setText("");
	}
	
	/*
	 * Pre: 
	 * Post: crea los botones con y da texto a los label
	 */
	private void showEventDemo() {
		headerLabel.setText("Elige una de las opciones");

		JButton piedraButton = new JButton("Piedra, papel o tijera");
		JButton papelButton = new JButton("Cara o Cruz");

		piedraButton.setActionCommand("PPT");
		papelButton.setActionCommand("COZ");

		piedraButton.addActionListener(new ButtonClickListener());
		papelButton.addActionListener(new ButtonClickListener());

		controlPanel.add(piedraButton);
		controlPanel.add(papelButton);

		mainFrame.setVisible(true);
	}

	/*ELECCIÓN DE LA OPCIÓN
	 * Pre: 
	 * Post: Devuelve la eleccion del cliente ya sea "Cara o Cruz" o "Piedra, Papel o Tijera" 
	 */
	public String getEleccion() {
		return this.eleccion;
	}

	/*MOSTRAR UN TEXTO MIENTRAS SE ESPERA A QUE OTRO JUGADOR SE UNA
	 * Pre: Recibe un String
	 * Post: Muestra el texto el cual se le ha pasado
	 */
	public void setEsperaLabel(String texto) {
		esperaLabel.setText(texto);
	}

	/*OCULTA EL FRAME PRINCIPAL CUANDO SE HA ELEGIDO A QUE JUEGO JUGAR
	 * Pre: 
	 * Post: oculta el Frame principal
	 */
	public void hide() {
		mainFrame.setVisible(false);
	}

	/*
	 * Pre: El método es referenciado cuando se pulsa un botón
	 * Post: Define la elección respecto del botón pulsado
	 */
	public class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("PPT")) {
				eleccion = "PPT";
			} else {
				eleccion = "COZ";
			}

			try {
				barrera.await();//Una vez que el cliente elige, el programa continua
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BrokenBarrierException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try (Socket s = new Socket("localhost", 55558);
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {
			MenuClient swingControlDemo = new MenuClient();
			String eleccion = swingControlDemo.getEleccion();
			dos.writeUTF(eleccion);

			if (eleccion.equals("PPT")) {
				swingControlDemo.setEsperaLabel("Espera a que se conecte otro cliente");

				while (dis.readUTF() == null);// Espero a que el servidor me de el aviso de empezar
				swingControlDemo.setEsperaLabel("");
				swingControlDemo.hide();

				InterfazPPT PPT = new InterfazPPT();
				dos.writeUTF(PPT.getEleccion());
				PPT.resultado(dis.readUTF());
			} else {
				InterfazCaraCruz CC = new InterfazCaraCruz();
				CC.result(dis.readUTF());
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
