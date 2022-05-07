import template.library.*;

HelloLibrary hello;
String msg;

void setup() {
  size(400,400);
  smooth();
  
  hello = new HelloLibrary(this);
  
  PFont font = createFont("Arial",40);
  textFont(font);
  msg = hello.sayHello();
}

void draw() {
  background(0);
  fill(255);
  text(msg, 40, 200);
}