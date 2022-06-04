import template.library.*;

HelloLibrary hello;
String msg;

boolean connected = false;

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
  text("Connected: " + connected, 40, 200);
}

void keyPressed() {
  if (!connected) {
    connected = hello.open("hubs-connect", "V32UWm4", "AUTH_NOT_REQUIRED_FOR_TEST");
  } else {
    hello.close();
  }
}