## Hubs Connect

This is a library for the [Processing programming environment](https://processing.org/) that allows accessing a [Mozilla Hubs](https://hubs.mozilla.com/) room programatically. It is based on the [HubsCloudUtil](https://github.com/rawnsley/HubsCloudUtil) library by [Rupert Rawnsley](https://github.com/rawnsley).

# Installing and using

You have to install the library manually by downloading the latest release and upacking the zip file in Processing's contributed libraries folder. The library comes with a few examples, the basic use is with the example below:

```
import codeanticode.hubs.*;

HubsConnect hubs;

boolean connected = false;
String objectId = "";

void setup() {
  size(400, 400);
  
  hubs = new HubsConnect(this);
  
  PFont font = createFont("Arial", 40);
  textFont(font);
}

void draw() {
  background(0);
  fill(255);
  text("Connected: " + connected + "\nmouseX: " + mouseX + "\nmouseY: " + mouseY, 40, 100);

  if (connected && objectId != "") {
    hubs.moveObject(objectId, map(mouseX, 0, width, -2, +2), 0, map(mouseY, 0, height, -2, +2));
  }
}

void keyPressed() {
  if (key == ' ') {
    if (!connected) {
      // In order to connect to an existing Hubs room, you need the room ID, which you can get from the URL:
      // https://hubs.mozilla.com/<ROOM_ID>
      // and the authenticationt token, which is included in the "Your Hubs Sign-In Link" email from Hubs. This email contains a line like this:
      // https://hubs.mozilla.com/?auth_origin=spoke&auth_payload=<...>&auth_token=<AUTHENTICATION_TOKEN>&auth_topic=<...>
      connected = hubs.open("processing-coder", "<ROOM_ID>", "<AUTHENTICATION_TOKEN>");
      hubs.enter(0, 0, -2);
    } else {
      hubs.close();
      connected = false;
      objectId = "";
    }
  }
}

void mousePressed() {
  if (connected && objectId == "") {
    // Use the URL of a 3-D model from sketchfab, for example:
    objectId = hubs.createObject("https://sketchfab.com/models/6511da7be4714b7a896f25ee51bf54e8", map(mouseX, 0, width, -2, +2), 0, map(mouseY, 0, height, -2, +2));
  }
}
```

You have to provide the ID of a room to open with the library, and then you can enter it with a "puppet" avatar that's controlled from the code, as well as creating and moving objects around. 

**Please note** that this library is currently at the alpha/prototype stage. It should be considered a proof of concept at this point.