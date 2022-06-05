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
    // In order to connect to an existing Hubs room, you need the room ID, which you can get from the URL:
    // https://hubs.mozilla.com/<ROOM_ID>
    // and the authenticationt token, which is included in the "Your Hubs Sign-In Link" email from Hubs. This email contains a line like this:
    // https://hubs.mozilla.com/?auth_origin=spoke&auth_payload=<...>&auth_token=<AUTHENTICATION_TOKEN>&auth_topic=<...>
    connected = hello.open("processing-coder", "<ROOM_ID>", "<AUTHENTICATION_TOKEN>");
  } else {
    hello.close();
    connected = false;
  }
}