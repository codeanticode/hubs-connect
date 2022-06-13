import codeanticode.hubs.*;

HubsConnect hubs;

boolean connected = false;

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
      // In order to connect to an existing Hubs room, you need the room ID, which you can get from the URL:
      // https://hubs.mozilla.com/<ROOM_ID>
      // and the authenticationt token, which is included in the "Your Hubs Sign-In Link" email from Hubs. This email contains a line like this:
      // https://hubs.mozilla.com/?auth_origin=spoke&auth_payload=<...>&auth_token=<AUTHENTICATION_TOKEN>&auth_topic=<...>
      connected = hubs.open("processing-coder", "<ROOM_ID>", "<AUTHENTICATION_TOKEN>");
      hubs.enter();
    } else {
      hubs.close();
      connected = false;
    }
  }
}

void mousePressed() {
  if (connected) {
    // Use the URL of a 3-D model from sketchfab, for example:
    hubs.placeObject("https://sketchfab.com/models/6511da7be4714b7a896f25ee51bf54e8");
  }
}