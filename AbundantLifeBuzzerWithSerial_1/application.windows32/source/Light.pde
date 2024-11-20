import ddf.minim.*;

//look for files in this directory
public Minim minim = new Minim(this);
AudioPlayer player;
public color yellowOff = color(224,216,43);
public color yellowOn = color(255,243,0);
public color redOff = color(203,66,70);
public color redOn = color(255,8,16);

class Light
{
  int xPos;
  int yPos;
  int diameter = 200;
  //String theColour;
  color printColour;
  String audioFile;
  
  Light(int takeXPosition,int takeYPosition, color printColour, String audioFile){
    this.xPos = takeXPosition;
    this.yPos = takeYPosition;
    this.printColour = printColour;
    this.audioFile = audioFile;
  }

  void display(){
    stroke(255);
    fill(printColour);
    ellipse(xPos, yPos, diameter, diameter);
  }
  
  void buzzed(){
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
  
  void turnOn(){
    if(printColour == redOff){
      printColour = redOn;
    }
    else if(printColour == yellowOff){
      printColour = yellowOn;
    }
    display();
  }
  
  void turnOff(){
     if(printColour == redOn){
      printColour = redOff;
    }
    else if(printColour == yellowOn){
      printColour = yellowOff;
    }
    display();
  }
}
