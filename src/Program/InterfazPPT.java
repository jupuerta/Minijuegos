package Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InterfazPPT extends Thread {
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel clockLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private String eleccion;

	public InterfazPPT() {
		mainFrame = new JFrame("Piedra-Papel-Tijera");
		mainFrame.setSize(400, 400);
		mainFrame.setLayout(new GridLayout(4, 1));

		headerLabel = new JLabel("", JLabel.CENTER);
		headerLabel.setText("Elige una de las opciones");

		clockLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);
		clockLabel.setFont(new Font(clockLabel.getFont().getName(), Font.BOLD, 16));
		statusLabel.setSize(350, 100);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		JButton piedraButton = new JButton("Piedra");
		JButton papelButton = new JButton("Papel");
		JButton tijeraButton = new JButton("Tijera");

		piedraButton.setActionCommand("Piedra");
		papelButton.setActionCommand("Papel");
		tijeraButton.setActionCommand("Tijera");

		piedraButton.addActionListener(new ButtonClickListener());
		papelButton.addActionListener(new ButtonClickListener());
		tijeraButton.addActionListener(new ButtonClickListener());

		controlPanel.add(piedraButton);
		controlPanel.add(papelButton);
		controlPanel.add(tijeraButton);

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(clockLabel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);

		crearHilo();
	}

	/*MOSTRAR SOLUCIÓN DE FORMA INTERMITENTE
	 * Pre: Recibe un String el cual es la solución
	 * Post:  
	 */
	public void resultado(String result) {
		try {
			for (int tiempo = 100; tiempo > -1; tiempo--) {
				Thread.sleep(500);
				clockLabel.setText(result);

				Thread.sleep(500);
				clockLabel.setText("");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*ELECCIÓN DE LA OPCIÓN
	 * Pre: 
	 * Post: Devuelve la eleccion del cliente ya sea "Piedra", "Papel" o "Tijera" 
	 */
	public String getEleccion() {
		return this.eleccion;
	}

	/*
	 * Pre: El método es referenciado cuando se pulsa un botón
	 * Post: Define la elección respecto del botón pulsado
	 */
	public class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("Piedra")) {
				statusLabel.setText("Has elegido piedra.");
				eleccion = "Piedra";
			} else if (command.equals("Papel")) {
				statusLabel.setText("Has elegido papel.");
				eleccion = "Papel";
			} else {
				statusLabel.setText("Has elegido tijera.");
				eleccion = "Tijera";
			}
		}
	}

	public void run() {
		try {
			for (int tiempo = 10; tiempo > -1; tiempo--) {
				Thread.sleep(1000);
				clockLabel.setText(String.valueOf(tiempo));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clockLabel.setText("--");
	}

	/*CREAR CUENTA ATRÁS PARA QUE EL JUGADOR ELIJA LA OPCIÓN
	 * Pre: 
	 * Post: Crea un hilo que crea la cuenta atrás
	 */
	public void crearHilo() {
		Thread hilo = new Thread(this);
		hilo.start();

		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}