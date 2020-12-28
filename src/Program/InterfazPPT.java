package Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InterfazPPT extends Thread{
	   private JFrame mainFrame;
	   private JLabel headerLabel;
	   private JLabel clockLabel;
	   private JLabel statusLabel;
	   private JPanel controlPanel;
	   private String eleccion;
		
		public InterfazPPT(){
			mainFrame = new JFrame("Piedra-Papel-Tijera");
			  mainFrame.setSize(400,400);
			  mainFrame.setLayout(new GridLayout(4, 1));
			
			  headerLabel = new JLabel("",JLabel.CENTER );
			  clockLabel = new JLabel("",JLabel.CENTER);
			  statusLabel = new JLabel("",JLabel.CENTER); 
			  clockLabel.setFont(new Font(clockLabel.getFont().getName(), Font.BOLD, 16));
		      statusLabel.setSize(350,100);
		      
		      mainFrame.addWindowListener(new WindowAdapter() {
		         public void windowClosing(WindowEvent windowEvent){
		            System.exit(0);
		         }        
		      });    
		      controlPanel = new JPanel();
		      controlPanel.setLayout(new FlowLayout());
		
		      mainFrame.add(headerLabel);
		      mainFrame.add(controlPanel);
		      mainFrame.add(clockLabel);
		      mainFrame.add(statusLabel);
		      mainFrame.setVisible(true);
		      showEventDemo();
	   }
		
		public void resultado(String result) {
			try {
				   for(int tiempo=100;tiempo>-1;tiempo--) {
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
		
		public String getEleccion() {
			return this.eleccion;
		}
		
		public void setEleccion(String eleccion) {
			this.eleccion = eleccion;
		}
		
		public void showEventDemo(){
	      headerLabel.setText("Elige una de las opciones"); 
		
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
	
	      mainFrame.setVisible(true);  
	   }
		public class ButtonClickListener implements ActionListener{
	      public void actionPerformed(ActionEvent e) {
	         String command = e.getActionCommand();           
	         if( command.equals( "Piedra" ))  {
	        	 statusLabel.setText("Has elegido piedra.");
	        	 eleccion="Piedra";
			 } else if( command.equals( "Papel" ) )  {
				 	statusLabel.setText("Has elegido papel.");
				 	eleccion="Papel";
			 		} else {
			 			statusLabel.setText("Has elegido tijera.");
			 			eleccion="Tijera";
			 		}  	
	     	}		
	   }
	   
	   
	   public void run() {
		   try {
			   for(int tiempo=10;tiempo>-1;tiempo--) {
				   Thread.sleep(1000);
				   clockLabel.setText(String.valueOf(tiempo));
			   }				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   clockLabel.setText("--");
	   }
	   
	   public void crearHilo() {
		   Thread hilo=new Thread(this);
		   hilo.start();
		   
		   try {
			   hilo.join();
		   } catch (InterruptedException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
	   }
	   /*
	   public static void main(String[] args){
		   InterfazPPT swingControlDemo = new InterfazPPT();  
		   swingControlDemo.showEventDemo();
		   
		   try(Socket s = new Socket("localhost",55558);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream())){
			   
			   swingControlDemo.crearHilo();
			   
			   
			   System.out.println("Entra");
			   System.out.println(swingControlDemo.getEleccion());
			   dos.writeUTF(swingControlDemo.getEleccion());	
				
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
	   */
}