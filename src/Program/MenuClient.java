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
	private JLabel headerLabel;
	private JPanel controlPanel;
	private String eleccion;
	private static CyclicBarrier barrera = new CyclicBarrier(2);

	public MenuClient() {
		mainFrame = new JFrame("Menu");
		mainFrame.setSize(400, 200);
		mainFrame.setLayout(new GridLayout(4, 1));

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
		mainFrame.setVisible(true);
		
		showEventDemo();
		try {
			barrera.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getEleccion() {
		return this.eleccion;
	}

	public void setEleccion(String eleccion) {
		this.eleccion = eleccion;
	}

	private void showEventDemo() {
		headerLabel.setText("Elige una de las opciones");

		JButton piedraButton = new JButton("Piedra, papel o tijera");
		JButton papelButton = new JButton("Tres en raya");

		piedraButton.setActionCommand("PPT");
		papelButton.setActionCommand("TER");

		piedraButton.addActionListener(new ButtonClickListener());
		papelButton.addActionListener(new ButtonClickListener());

		controlPanel.add(piedraButton);
		controlPanel.add(papelButton);

		mainFrame.setVisible(true);
	}

	public class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("PPT")) {
				eleccion = "PPT";
			} else {
				eleccion = "TER";
			}
			
			try {
				barrera.await();
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
			String eleccion=swingControlDemo.getEleccion();
			dos.writeUTF(eleccion);

			/*
			 * try { //Thread.sleep(10000000); } catch (InterruptedException e1) { // TODO
			 * Auto-generated catch block e1.printStackTrace(); }
			 */

			if (eleccion.equals("PPT")) {
				while (dis.readUTF() == null)
					;// Espero a que el servidor me de el aviso de empezar
				
				InterfazPPT PPT = new InterfazPPT();
				PPT.crearHilo();
				dos.writeUTF(PPT.getEleccion());
				// resultado

				PPT.resultado(dis.readUTF());
			} else {
				InterfazCaraCruz CC=new InterfazCaraCruz();				
							
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
