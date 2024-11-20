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

import processing.serial.*;

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

void setup() {
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

void draw() {
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

void update(int x, int y) {
  clear.update(x, y);
  interruption.update(x, y);
  five.update(x, y);
  //thirty.update(x, y);
}

void displayButtons() {
  clear.display();
  interruption.display();
  five.display();
  //thirty.display();
}

void displayLights() {
  for (int i = 0; i< 3; i++) {
    YellowLight[i].display();
    RedLight[i].display();
  }
}

void turnOffLights() {
  for (int i = 0; i< 3; i++) {
    YellowLight[i].turnOff();
    RedLight[i].turnOff();
  }
}

void mousePressed() {
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
