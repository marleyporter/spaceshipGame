import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Rectangle;
import javax.sound.sampled.*;
import java.io.*;



public class Part2P extends JPanel implements ActionListener, KeyListener {

   private int totalImages = 16;
   private int currentImage = 0;
   Image theImage, secImage;
   ImageIcon[] spaceShips = new ImageIcon[totalImages]; //array with spaceships
   ImageIcon[] redSpace = new ImageIcon[totalImages]; //array with second set of spaceships
   
   //background image
   private ImageIcon backG, aster, asteroid, lineF, finishLine;
   
   //initial starting orientation
   private int currentP1 = 4;
   private int currentP2 = 4;
   
   private int spaceSize = 50;
   
   //current speeds
   private int speedP1 = 0;
   private int speedP2 = 0;
   
   //coordinate variables
   private int xP1 = 425;
   private int yP1 = 100;
   private int xP2 = 425;
   private int yP2 = 150;           
   
   //box (rectangle) starting coordinates
   private int xBox = 250;
   private int yBox = 30;
   
   //lap counter
   private int lapP1 = 0;
   private int lapP2 = 0;
   
   //Check points
   private boolean checkpoint = false;
   private boolean checkpoint2 = false;

   
   //Constructor for main panel
   public Part2P() {
      
      Timer paintTimer = new Timer(50, this);
      paintTimer.start();
            
      //background image of Jpanel
      setOpaque(false);
      addKeyListener(this); //listens to keyboard
      setFocusable(true); //set focus on specific keys
      setFocusTraversalKeysEnabled(true); //set focus on virtual keyboard
            
      //set background image
      backG = new ImageIcon ("galaxy.jpg");
      
      //load asteroid image, resize the image and save it as asteroid.
      ImageIcon image = new ImageIcon ("asteroid.png");
      Image aster = image.getImage();
      Image newaster = aster.getScaledInstance(800, 400, java.awt.Image.SCALE_SMOOTH);
      asteroid = new ImageIcon(newaster);
      
      //load finish line image, resize
      ImageIcon imageF = new ImageIcon ("finish.png");
      Image lineF = imageF.getImage();
      Image newlineF = lineF.getScaledInstance(20, 190, java.awt.Image.SCALE_SMOOTH);
      finishLine = new ImageIcon(newlineF);
            
      //import spaceship pictures to arary
      for(int i = 0; i < spaceShips.length; i++) {
         spaceShips[i] = new ImageIcon("starships/starships" + i + ".png");
      }
            
      //import second set of spaceships to array
      for(int i = 0; i < redSpace.length; i++) {
         redSpace[i] = new ImageIcon("redstars/redstarships" + i + ".png");
      }      
   }
   
    public void paint(Graphics g) {
      
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      
      //set colour to white
      Color wh = Color.white;
      g.setColor(wh);
      
      //set line thickness
      Stroke lineTh = g2d.getStroke();
      g2d.setStroke(new BasicStroke(6));
      
      //add font to the screen
      backG.paintIcon(this, g, 0, 0);
      g.setFont(new Font("courier", Font.PLAIN, 26));
      g.drawString("Part 2", 50, 30);
      g.setFont(new Font("courier", Font.PLAIN, 20));
      
      //Add scores to output
      g.drawString("P1 Score: " + lapP1, 50, 60);
      g.drawString("P2 Score: " + lapP2, 50, 90);
      
      //Add the asteroid to the JPanal
      asteroid.paintIcon(this, g, 425, 200);
      g.drawRect(460, 220, 735, 380);
      
      //Add Finish Line to the JPanal
      finishLine.paintIcon(this, g, 500, 30);

      //draw boundary box
      Rectangle box = new Rectangle(xBox, yBox, 1150, 750);
      g.drawRect(box.x, box.y, box.width, box.height);
      
      //assign current spaceship images to variables
      theImage = spaceShips[currentP1].getImage();
      secImage = redSpace[currentP2].getImage();
      
      g2d.drawImage(theImage, xP1, yP1, this);
      g2d.drawImage(secImage, xP2, yP2, this);
      g.dispose();
   }

   //Methods to be performed after key strokes
   public void actionPerformed(ActionEvent e) {
         
      repaint();
      movement();
      collisions();
      finish();
   }
      
   //When P1 collides the following occurrs
   public void collisionP1(){
      
      xP1 = 425;
      yP1 = 100;
      speedP1 = 0;
      currentP1 = 4;
      lapP1 -= 1;
      checkpoint = false; 
      soundTest();
   }
      
   //When P2 collides the following occurrs
   public void collisionP2(){
         
      xP2 = 425;
      yP2 = 150;
      speedP2 = 0;
      currentP2 = 4;
      lapP2 -= 1; 
      checkpoint2 = false;
      soundTest();
   }
      
   public void collisions(){
      
      //Assign collision coordinates using rectangle areas
      Rectangle outerbox = new Rectangle(xBox, yBox, 1150, 750);
      Rectangle asterbox = new Rectangle(460, 220, 735, 380);
      Rectangle P1box = new Rectangle(xP1, yP1, 35, 35);
      Rectangle P2box = new Rectangle(xP2, yP2, 35, 35);
      Rectangle finishbox = new Rectangle(500, 30, 20, 190);
      Rectangle checkTwo = new Rectangle(900, 600, 35, 200);
         
         
      //If statements, conditions for collisions
      if (outerbox.getBounds().contains(P1box.getBounds())){
      
      }else{
         collisionP1();
      }
         
      if (outerbox.getBounds().contains(P2box.getBounds())){
      
      }else{
         collisionP2(); 
      }
         
      if (asterbox.getBounds().intersects(P1box.getBounds())){
         collisionP1();
      }

      if (asterbox.getBounds().intersects(P2box.getBounds())){
         collisionP2();
      }
         
      if (P1box.getBounds().intersects(P2box.getBounds())){
         collisionP1();
         collisionP2();
      }
         
      //Managing crossing the finish line player 1
      if(checkTwo.getBounds().intersects(P1box.getBounds())){
         checkpoint = true;   //set boolean to true when starship passes checkpoint
      } 
         
      //Check current image to verify the direction of the starship
      if(currentP1 == 1 || currentP1 == 2 || currentP1 == 3 || currentP1 == 4 || currentP1 == 5 || currentP1 == 6 || currentP1 == 7){
         //If the ship crosses the finish line having passed through the checkpoint
         if(finishbox.getBounds().intersects(P1box.getBounds()) && checkpoint != false){
            lapP1 +=1; //Add a lap to the counter
            checkpoint = false; //Set the checkpoint to false.
         }
   
      }
      
      //Managing crossing the finish line player 2   
      if(checkTwo.getBounds().intersects(P2box.getBounds())){
         checkpoint2 = true;
      }
         
      if(currentP2 == 1 || currentP2 == 2 || currentP2 == 3 || currentP2 == 4 || currentP2 == 5 || currentP2 == 6 || currentP2 == 7){
         if(finishbox.getBounds().intersects(P2box.getBounds()) && checkpoint2 != false){
            lapP2 +=1;
            checkpoint2 = false;
         }
      }         
   }
   
   //FINISH
   public void finish(){
      //If player 1 reaches 3 laps, display message and reset variables
      if(lapP1 == 3){
         JOptionPane.showMessageDialog(null, "GAME OVER -- Player 1 is the winner!");
         lapP1 = 0;
         lapP2 = 0;
         xP1 = 425;
         yP1 = 100;
         speedP1 = 0;
         currentP1 = 4;
         checkpoint = false;
         
      }
 
      //If player 2 reaches 3 laps, display message and reset variables      
      if(lapP2 == 3){
         JOptionPane.showMessageDialog(null, "GAME OVER -- Player 2 is the winner!");
         lapP1 = 0;
         lapP2 = 0;
         xP2 = 425;
         yP2 = 150;
         speedP2 = 0;
         currentP2 = 4;
         checkpoint2 = false;
      }
   }   

   public void soundTest(){
      
      try{
         File explosion = new File("explosion.wav"); //create instance of audio file
         AudioInputStream stream; //create instance of AIS 'stream'
         AudioFormat format; //create instance of AF 'format'
         DataLine.Info info;
         Clip clip;
            
         stream = AudioSystem.getAudioInputStream(explosion); //load the audio file
         format = stream.getFormat();
         info = new DataLine.Info(Clip.class, format);
         clip = (Clip) AudioSystem.getLine(info);
         clip.open(stream);
         clip.start(); //sound the audio file
      }catch(Exception e){
            
      }
   }
               
      
   public void movement(){
   
      //If statements - controls movement of spaceship1
      if(currentP1 == 0){ //if spaceship is looking up
         yP1 = yP1 - 2 * speedP1; //multiply 2 by speed, subtract from y axis to move up
      }
      else if(currentP1 == 1){
         xP1 = xP1 + 1 * speedP1;
         yP1 = yP1 - 2 * speedP1;
      }
      else if(currentP1 == 2){
         xP1 = xP1 + 2 * speedP1;
         yP1 = yP1 - 2 * speedP1;
      }
      else if(currentP1 == 3){
         xP1 = xP1 + 2 * speedP1;
         yP1 = yP1 - 1 * speedP1;
      }
      else if(currentP1 == 4){
         xP1 = xP1 + 2 * speedP1;
      }
      else if(currentP1 == 5){
         xP1 = xP1 + 2 * speedP1;
         yP1 = yP1 + 1 * speedP1;
      }
      else if(currentP1 == 6){
         xP1 = xP1 + 2 * speedP1;
         yP1 = yP1 + 2 * speedP1;
      }
      else if(currentP1 == 7){
         xP1 = xP1 + 1 * speedP1;
         yP1 = yP1 + 2 * speedP1;
      }
      else if(currentP1 == 8){
         yP1 = yP1 + 2 * speedP1;
      }
      else if(currentP1 == 9){
         xP1 = xP1 - 1 * speedP1;
         yP1 = yP1 + 2 * speedP1;
      }
      else if(currentP1 == 10){
         xP1 = xP1 - 2 * speedP1;
         yP1 = yP1 + 2 * speedP1;
      }
      else if(currentP1 == 11){
         xP1 = xP1 - 2 * speedP1;
         yP1 = yP1 + 1 * speedP1;
      }
      else if(currentP1 == 12){
         xP1 = xP1 - 2 * speedP1;
      }
      else if(currentP1 == 13){
         xP1 = xP1 - 2 * speedP1;
         yP1 = yP1 - 1 * speedP1;
      }
      else if(currentP1 == 14){
         xP1 = xP1 - 2 * speedP1;
         yP1 = yP1 - 2 * speedP1;
      }
      else if(currentP1 == 15){
         xP1 = xP1 - 1 * speedP1;
         yP1 = yP1 - 2 * speedP1;
      }
      
      //If statement - controls movement of spaceship2
      if(currentP2 == 0){
         yP2 = yP2 - 2 * speedP2;
      }
      else if(currentP2 == 1){
         xP2 = xP2 + 1 * speedP2;
         yP2 = yP2 - 2 * speedP2;
      }
      else if(currentP2 == 2){
         xP2 = xP2 + 2 * speedP2;
         yP2 = yP2 - 2 * speedP2;
      }
      else if(currentP2 == 3){
         xP2 = xP2 + 2 * speedP2;
         yP2 = yP2 - 1 * speedP2;
      }
      else if(currentP2 == 4){
         xP2 = xP2 + 2 * speedP2;
      }
      else if(currentP2 == 5){
         xP2 = xP2 + 2 * speedP2;
         yP2 = yP2 + 1 * speedP2;
      }
      else if(currentP2 == 6){
         xP2 = xP2 + 2 * speedP2;
         yP2 = yP2 + 2 * speedP2;
      }
      else if(currentP2 == 7){
         xP2 = xP2 + 1 * speedP2;
         yP2 = yP2 + 2 * speedP2;
      }
      else if(currentP2 == 8){
         yP2 = yP2 + 2 * speedP2;
      }
      else if(currentP2 == 9){
         xP2 = xP2 - 1 * speedP2;
         yP2 = yP2 + 2 * speedP2;
      }
      else if(currentP2 == 10){
         xP2 = xP2 - 2 * speedP2;
         yP2 = yP2 + 2 * speedP2;
      }
      else if(currentP2 == 11){
         xP2 = xP2 - 2 * speedP2;
         yP2 = yP2 + 1 * speedP2;
      }
      else if(currentP2 == 12){
         xP2 = xP2 - 2 * speedP2;
      }
      else if(currentP2 == 13){
         xP2 = xP2 - 2 * speedP2;
         yP2 = yP2 - 1 * speedP2;
      }
      else if(currentP2 == 14){
         xP2 = xP2 - 2 * speedP2;
         yP2 = yP2 - 2 * speedP2;
      }
      if(currentP2 == 15){
         xP2 = xP2 - 1 * speedP2;
         yP2 = yP2 - 2 * speedP2;
      }     
   }

   //control the speed up of the spaceship
   private void speedUpP1(){
      
      //if speed is lower than maximum speed, increase speed by one
      if (speedP1 < 6){
         speedP1 += 1;
      }
      //if speed is at maximum, do not increase
      else{
         speedP1 = 6;
      }
   }
   
   //control the speed up of the second spaceship   
   private void speedUpP2(){
      
      if (speedP2 < 6){
         speedP2 += 1;
      }
      else {
         speedP2 = 6;
      }
   }
   
   //control the speed down of the spaceship   
   private void speedDownP1(){
      
      //if speed is more than 0, lower speed by one
      if (speedP1 > 1){
         speedP1 -= 1;
      }
      //if speed is at 0, do not lower
      else {
         speedP1 = 0;
      }
   }
   
   //control the speed down of the spaceship   
   private void speedDownP2(){
      
      if (speedP2 > 1){
         speedP2 -= 1;
      }
      else {
         speedP2 = 0;
      }
   }
      
   //Action key Pressed   
   public void keyPressed(KeyEvent k){
      
      //Declaration of variables
      int Player1 = k.getKeyCode();
      int Player2 = k.getKeyCode();
         
      //PLAYER ONE KEYBOARD CONTROLS
      if(Player1 == KeyEvent.VK_RIGHT){
         
         //rotation of the images 
         if(currentP1 < 15){
            currentP1 += 1;
         }
         else{
         currentP1 = 0;
         }
      }
         
      if(Player1 == KeyEvent.VK_LEFT){
         
         if(currentP1 > 0){
            currentP1 -= 1;
         }
         else{
         currentP1 = 15;
         }
      }
         
      if(Player1 == KeyEvent.VK_UP){
         speedUpP1();
      }
            
      if(Player1 == KeyEvent.VK_DOWN){
         speedDownP1();
      }

      //PLAYER 2 KEYBOARD CONTROLS
      if(Player2 == KeyEvent.VK_D){
            
         if(currentP2 < 15){
            currentP2 += 1;
         }
         else{
         currentP2 = 0;
         }
      }
         
      if(Player2 == KeyEvent.VK_A){
            
         if(currentP2 > 0){
            currentP2 -= 1;
         }
         else{
         currentP2 = 15;
         }
      }

      if (Player2 == KeyEvent.VK_W) { 
      speedUpP2();
      }

      if (Player2 == KeyEvent.VK_S){ 
      speedDownP2();
      }
   }
      
   public void keyTyped(KeyEvent e){}
   public void keyReleased(KeyEvent e){}
}
