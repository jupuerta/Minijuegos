package Pruebas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Prueba {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;

   public Prueba(){
      prepareGUI();
   }
   public static void main(String[] args){
	   Prueba swingControlDemo = new Prueba();  
      swingControlDemo.showEventDemo();       
   }
   private void prepareGUI(){
      mainFrame = new JFrame("Java SWING Examples");
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(3, 1));

      headerLabel = new JLabel("",JLabel.CENTER );
      statusLabel = new JLabel("",JLabel.CENTER);        
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
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }
   private void showEventDemo(){
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
   private class ButtonClickListener implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         String command = e.getActionCommand();           
         if( command.equals( "Piedra" ))  {
            statusLabel.setText("Has elegido piedra.");
         } else if( command.equals( "Papel" ) )  {
            statusLabel.setText("Has elegido papel."); 
         } else {
            statusLabel.setText("Has elegido tijera.");
         }  	
      }		
   }
}