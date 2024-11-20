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

  void display() {
    textFont(createFont("Georgia",40));
    textAlign( CENTER );
    if (startTimer == true) {
      displayInt = (offset + countFrom - int(millis()/1000));
    } 
    if (displayInt < 0) {
      endTimer();
    }
    text = displayInt + "";
    fill(0);
    text(text, xPos, yPos);
  }

  void countFrom(int countFrom) {
    this.countFrom = countFrom;
  }

  void startTimer() {
    offset = int(millis()/1000);
    startTimer = true;
  }

  void endTimer() {
    player = minim.loadFile("time.mp3");
    player.play();
    startTimer = false;
    displayInt = 0;
  }
  
    void endTimer(boolean isTimerCleared) {
    startTimer = false;
    displayInt = 0;
  }
  
  boolean isTimerOn(){
    return this.startTimer;
  }
  
}
