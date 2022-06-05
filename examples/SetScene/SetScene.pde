import codeanticode.hubs.*;

HubsConnect hubs;

boolean connected = false;
String sceneURL = "https://data.avncloud.com/dev/GatheringHall.glb";

void setup() {
  size(400, 400);
  
  hubs = new HubsConnect(this);
  
  PFont font = createFont("Arial", 40);
  textFont(font);
}

void draw() {
  background(0);
  fill(255);
  text("Connected: " + connected, 40, 200);
}

void keyPressed() {
  if (key == ' ') {
    if (!connected) {
      connected = hubs.open("processing-coder", "<ROOM_ID>", "<AUTHENTICATION_TOKEN>");
    } else {
      hubs.close();
      connected = false;
    }
  } else if (connected) {
    hubs.setScene(sceneURL);
  }
}