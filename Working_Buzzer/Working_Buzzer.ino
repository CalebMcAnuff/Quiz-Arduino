/*
  State change detection (edge detection)
 	
 Often, you don't need to know the state of a digital input all the time,
 but you just need to know when the input changes from one state to another.
 For example, you want to know when a button goes from OFF to ON.  This is called
 state change detection, or edge detection.
 
 This example shows how to detect when a button or button changes from off to on
 and on to off.
 	
 The circuit:
 * pushbutton attached to pin 2 from +5V
 * 10K resistor attached to pin 2 from ground
 * LED attached from pin 13 to ground (or use the built-in LED on
 most Arduino boards)
 
 Quizzing:
 -Each button object will have its own buttonPushCounter
 -Arduino has isRequestSendable it will Serial.Write only if that value is true
 -once a button has been pressed isRequestSendable is set to false
 Processing
 -When a button is pressed, processing reads the buttonPushCounter and makes sure its one, if not it ignores the packet
 -When clear is pressed buttonPushCounter (for all buttons are set to zero) and isRequestSendable is set to true
 
 
 */

// this constant won't change:
const int  buttonPin[] = {
  2,3,4,8,9,10};    // the pin that the pushbutton is attached to
const int ledPin = 13;       // the pin that the LED is attached to
int buttonPushCounter[6] = {0};   // counter for the number of button presses
int buttonState[6] = {
  0};         // current state of the button
int lastButtonState[6] = {
  0};     // previous state of the button
boolean isSendable = true;

void setup() {
  // initialize the button pin as a input:
  for(int i =0; i<6; i++){
    pinMode(buttonPin[i], INPUT);
  }
  // initialize the LED as an output:
  pinMode(ledPin, OUTPUT);
  // initialize serial communication:
  Serial.begin(9600);
}


void loop() {
  for(int i= 0; i<6; i++){
    // read the pushbutton input pin:
    buttonState[i] = digitalRead(buttonPin[i]);

    // compare the buttonState to its previous state
    if (buttonState[i] != lastButtonState[i]) {
      // if the state has changed, increment the counter
      if (buttonState[i] == HIGH&&isSendable) {
        // if the current state is HIGH then the button
        // wend from off to on:
        buttonPushCounter[i]++;
        Serial.write(i);
        //Serial.println("on");
        //Serial.print("number of button pushes:  ");
        //Serial.println(buttonPushCounter);
      } 
      else {
        // if the current state is LOW then the button
        // wend from on to off:
        //Serial.println("off"); 
      }
    }
    // save the current state as the last state, 
    //for next time through the loop
    lastButtonState[i] = buttonState[i];
  }

}










