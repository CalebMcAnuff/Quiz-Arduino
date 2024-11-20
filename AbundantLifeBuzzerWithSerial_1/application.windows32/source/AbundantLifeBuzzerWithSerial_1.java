import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AbundantLifeBuzzerWithSerial_1 extends PApplet {

/*********************************************************************************
 *                                Buzzer                                         *
 *********************************************************************************
 * - Serial works
 * - Need to add offset for buzzer values red and yellow - fixed
 * Things to change:
 * -Clear button should clear timer - fixed
 * -Timer should say time (not buzz) - fixed
 * -Thirty seconds should be automated (right as you press the buzzer time starts) - fixed
 * -Interruption button should be grayed if a buzzer was not buzzed - fixed
 * -UI to customize timer settings (or other settings)
 * -Reduce spurious input from buzzer - fixed
 * -Track the time between buzzers (who pressed how many seconds after...)
 *********************************************************************************
 *                                Buzzer                                         *
 *********************************************************************************
 */



//the coloumn for the light
int[] column = {
  112, 350, 587
};

//the row for the light (yellow on top red below)
int[] row = {
  140, 380
};

//if isBuzzerPressed is true, no other function can be called until it is cleared by clear()
boolean isBuzzerPressed = false;

Light[] YellowLight = new Light[3];
Light[] RedLight = new Light[3];

int buttonColumn = 750;
Button interruption = new Button(buttonColumn, 100, "Interruption");
Button clear = new Button(buttonColumn, 140, "Clear");
Button five = new Button(buttonColumn, 180, "5");
//Button thirty = new Button(buttonColumn, 220, "30");

Timer timer = new Timer(buttonColumn + 50, row[1]);

Serial serialPort;

public void setup() {
  size(900, 500);
  for (int i=0; i<3; i++) {
    YellowLight[i] = new Light(column[i], row[0], yellowOff, "yellow"+(i+1)+".mp3");
    RedLight[i] = new Light(column[i], row[1], redOff, "red"+(i+1)+".mp3");
  }
  background(175);

  //display the lights
  displayLights();

  //display the buttons
  interruption.isButtonPressable = false; //greyed unless buzzer is pressed
  displayButtons();
  
  //make Serial Port
  serialPort = new Serial(this, Serial.list()[0], 9600);
}

public void draw() {
  background(175);
  update(mouseX, mouseY);
  displayLights();  
  displayButtons();
  timer.display();
  
  //Serial Stuff ****** ===>> NEED TO ADD OFFSETS FOR RED AND YELLOW TEAM!!!!!!!!!!!!!!!
  
    while (serialPort.available() > 0 && isBuzzerPressed == false) {
    int inByte = serialPort.read();
    println(inByte);//This is only for testing
    isBuzzerPressed = true;
    interruption.isButtonPressable = true;
    
    //Arduino sends a value from 0 to 5
    //0-2 is yellow 3-5 is red
    if(inByte >= 0 && inByte <= 2)
      YellowLight[inByte].buzzed();
    if(inByte >= 3 && inByte <= 5)
      RedLight[inByte-3].buzzed();
    
    //serialPort.clear();
    timer.countFrom(30);//Automate timer
    timer.startTimer();
   
  }
  
  //Serial Stuff ends
}

public void update(int x, int y) {
  clear.update(x, y);
  interruption.update(x, y);
  five.update(x, y);
  //thirty.update(x, y);
}

public void displayButtons() {
  clear.display();
  interruption.display();
  five.display();
  //thirty.display();
}

public void displayLights() {
  for (int i = 0; i< 3; i++) {
    YellowLight[i].display();
    RedLight[i].display();
  }
}

public void turnOffLights() {
  for (int i = 0; i< 3; i++) {
    YellowLight[i].turnOff();
    RedLight[i].turnOff();
  }
}

public void mousePressed() {
  if (clear.imageOver) {
    //turn off all buttons
    turnOffLights();
    //clear timer
    timer.endTimer(true);
    isBuzzerPressed = false;
    interruption.isButtonPressable = false;//greyed unless buzzer is pressed
    
    serialPort.clear();
  }
  if (interruption.imageOver && interruption.isButtonPressable) {
    player = minim.loadFile("interruption.mp3");
    player.play();
  }
  if (five.imageOver) {
    timer.countFrom(5);  
    timer.startTimer();
  }/*
  if (thirty.imageOver) {
    timer.countFrom(30);
    timer.startTimer();
  }*/
}
/***           Button Class
  **  ..... ..... ..... ..... .....
  ** Methods: update(), display()
  ** Important variable: imageOver, name
  ** To use: place  if (button_object.imageOver == boolean)
  .. in the mousePressed() function.
*/
class Button
{
  int imageX, imageY;    // Position of image button
  PImage img = loadImage("C:\\Users\\Caleb\\Documents\\Processing\\Psketch\\AbundantLifeBuzzer\\button.png");
  boolean imageOver = false;
  String name;
  boolean isButtonPressable = true;

  Button( int x, int y, String name) {
    imageX = x;
    imageY = y;
    this.name = name;
  }

  public void display() {
    textFont(createFont("Georgia", 14));
    textAlign(CENTER);
    if (isButtonPressable) {
      if (imageOver) {
        tint(150);
      } else {
        noTint();
      }
    } else {
      tint(200);
    }
    image(img, imageX, imageY, img.width/2, img.height/2);

    //This is for the text on the button
    fill(0);
    text(this.name, imageX + img.width/4, imageY+img.height/4+5);
  }

  private boolean overImage(int x, int y) {
    if (mouseX >= x && mouseX <= x+img.width/2 && 
      mouseY >= y && mouseY <= y+img.height/2) {
      imageOver = true;
      return imageOver;
    } 
    else {
      imageOver = false;
      return imageOver;
    }
  }

  public void update(int x, int y) {
    if ( overImage(imageX, imageY) ) ;
  }
}


//look for files in this directory
public Minim minim = new Minim(this);
AudioPlayer player;
public int yellowOff = color(224,216,43);
public int yellowOn = color(255,243,0);
public int redOff = color(203,66,70);
public int redOn = color(255,8,16);

class Light
{
  int xPos;
  int yPos;
  int diameter = 200;
  //String theColour;
  int printColour;
  String audioFile;
  
  Light(int takeXPosition,int takeYPosition, int printColour, String audioFile){
    this.xPos = takeXPosition;
    this.yPos = takeYPosition;
    this.printColour = printColour;
    this.audioFile = audioFile;
  }

  public void display(){
    stroke(255);
    fill(printColour);
    ellipse(xPos, yPos, diameter, diameter);
  }
  
  public void buzzed(){
    //first light the buzzer
    turnOn();
    player = minim.loadFile("buzz.mp3.mp3");
    player.play();
    
    //this prevents sounds from playing on top of each other
    while(player.isPlaying()){
      delay(0);
    }   
    
    //then load the buzzer name
    player = minim.loadFile(this.audioFile);
    player.play();
  }
  
  public void turnOn(){
    if(printColour == redOff){
      printColour = redOn;
    }
    else if(printColour == yellowOff){
      printColour = yellowOn;
    }
    display();
  }
  
  public void turnOff(){
     if(printColour == redOn){
      printColour = redOff;
    }
    else if(printColour == yellowOn){
      printColour = yellowOff;
    }
    display();
  }
}
/**       Timer Class
****** ****** ****** ****** ******
 ** Takes x and y position
 ** Important functions: timer_object.countFrom(int) => tells object where to count from
 ** timer_object.display() => displays object 
 ** timer_object.startTimer() => starts the timer (usually in mousePressed() function)
*/

class Timer {

  private boolean startTimer;
  private int xPos, yPos;
  private int countFrom;
  private int offset;
  private int displayInt;
  private String text;

  Timer( int x, int y) {
    this.xPos = x;
    this.yPos = y;
    startTimer = false;
    countFrom = 0;
    displayInt = 0;
    text = "0";
  }

  public void display() {
    textFont(createFont("Georgia",40));
    textAlign( CENTER );
    if (startTimer == true) {
      displayInt = (offset + countFrom - PApplet.parseInt(millis()/1000));
    } 
    if (displayInt < 0) {
      endTimer();
    }
    text = displayInt + "";
    fill(0);
    text(text, xPos, yPos);
  }

  public void countFrom(int countFrom) {
    this.countFrom = countFrom;
  }

  public void startTimer() {
    offset = PApplet.parseInt(millis()/1000);
    startTimer = true;
  }

  public void endTimer() {
    player = minim.loadFile("time.mp3");
    player.play();
    startTimer = false;
    displayInt = 0;
  }
  
    public void endTimer(boolean isTimerCleared) {
    startTimer = false;
    displayInt = 0;
  }
  
  public boolean isTimerOn(){
    return this.startTimer;
  }
  
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "AbundantLifeBuzzerWithSerial_1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
