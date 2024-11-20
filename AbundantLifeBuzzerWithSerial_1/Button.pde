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

  void display() {
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

  void update(int x, int y) {
    if ( overImage(imageX, imageY) ) ;
  }
}
