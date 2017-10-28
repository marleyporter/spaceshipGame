import java.awt.*;
import javax.swing.*;

public class Part2F extends JFrame {
   
   Part2P mainPanel; //declare main panel
   
   Part2F() {
      
      setTitle("Spaceships!");
      mainPanel = new Part2P(); //create new main panel
      setSize(850, 650); //size of main panel
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit the program when closed
      add(mainPanel);
      setVisible(true); //make the main panel visible
      setExtendedState(Frame.MAXIMIZED_BOTH); //Launch maximised screen 
   }
   
   public static void main(String[] args) {
      
      new Part2F();
   }

}