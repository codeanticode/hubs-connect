package codeanticode.hubs;

import java.util.Map;
import static java.util.Map.entry;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import processing.core.*;
import avn.portal.*;

/**
 * A Processing library for programmatic access to the assets in a Mozilla Hubs room.
 * It uses the HubsCloudUtil library: 
 * https://github.com/rawnsley/HubsCloudUtil
 * to communicate with the Mozilla Reticulum, the Hubs backend.
 * 
 * @example HelloHubs
 */

public class HubsConnect {	
    PApplet parent;

    public final static String VERSION = "##library.prettyVersion##";

    // The classes from the HubsCloudUtil library, which is written in Kotlin.	
    // Some resources about Kotlin-Java interoperability:
    // https://kotlinlang.org/docs/java-to-kotlin-interop.html
    // https://medium.com/google-developer-experts/from-java-to-kotlin-and-back-i-java-calling-kotlin-9abfc6496b04
    // https://livebook.manning.com/book/the-joy-of-kotlin/a-mixing-kotlin-with-java/v-8/226	
    public avn.portal.ReticulumConnection connection;
    public avn.portal.RoomConnection room;

    private URL HUBS_SERVER;

    private String userName;
    private String roomId;
    private String authToken;

    // private String networkId;
    private String sessionId;

    private String avatarId = "xZYtcDf";   // Default avatar
    private String avatarUrl = "https://hubs.mozilla.com/api/v1/avatars/xZYtcDf/avatar.gltf?v=63736240049";
    private boolean firstAvatarSync;
    private float avatarX;
    private float avatarY; 
    private float avatarZ;

    private Timer timer;

    // private String avatarId = "7qt89yB"; // Foxr avatar
	// private String avatarId = "PGnvD5h"; // Webcam astronaut avatar

    /**
     * The constructor of the library object.
     * 
     * @example HelloHubs
     * @param parent the parent PApplet
     */
    public HubsConnect(PApplet parent) {
        this.parent = parent;

        try {
            HUBS_SERVER = new URL("https://hubs.mozilla.com");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        welcome();
    }

    private void welcome() {
        System.out.println("##library.name## ##library.prettyVersion## by ##author##");
    }

    /**
     * Open a room and return true if succesful.
     * 
     * @example HelloHubs
     * @param userName A user name to use when logging into the room
     * @param roomId The ID of the room
     * @param authToken The authorization token that can be obtained from the sign-in link email from Hubs
     * 
     * @return boolean
     */
    public boolean open(String userName, String roomId, String authToken) {
        this.userName = userName;
        this.roomId = roomId;
        this.authToken = authToken;

        try {
            connection = new avn.portal.ReticulumConnection(HUBS_SERVER, authToken);
            room = new avn.portal.RoomConnection(roomId, connection, userName);

            sessionId = room.getSessionId();

            if (room.isOpen()) {
                System.out.println("SUCCESS to open room with session id " + sessionId);
            }

            return true;
        } catch (Exception ex) {
            System.err.println("Hubs connection failed");
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Close the currently open room
     * 
     */
    public void close() {
        try {
            if (timer != null) {
                timer.cancel();
            }
            room.close();
            System.out.println("SUCCESS to close room");			
        } catch (Exception ex) {
            System.err.println("Hubs connection failed");
            ex.printStackTrace();
        }
    }

    /**
     * Enter the currently open room and return true if succesful.
     * 
     * @return boolean
     */	
    public boolean enter(float x, float y, float z) {
        try {
            room.sendMessage("events:entered", Map.ofEntries(entry("entryDisplayType", "Screen"), entry("userAgent", "Processing 4 (hubs-connect library)")));
            
            avatarId = generateRandomAlphaNumericString(7);

            Map profile = Map.ofEntries(entry("avatarId", avatarId), entry("displayName", userName));
			room.sendMessage("events:profile_updated", Map.ofEntries(entry("profile", profile)));

            firstAvatarSync = true;

            avatarX = x;
            avatarY = y;
            avatarZ = z;

            System.out.println("SUCCESS to join room");


            TimerTask task = new TimerTask() {
                public void run() {
                    updateAvatar();
                }
            };
            timer = new Timer("Timer");
            long delay = 500L;
            long period = 5000L;
            timer.scheduleAtFixedRate(task, delay, period);

			return true;
        } catch (Exception ex) {
            System.err.println("Hubs connection failed");
            ex.printStackTrace();
        }
		return false;
	}

    private String generateRandomAlphaNumericString(int len) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
    
        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(len)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();

        return generatedString;
    }


    private void updateAvatar() {
        try {
			// Initialize/update avatar
			Map components = Map.ofEntries(entry("0", Map.ofEntries(entry("x", avatarX), entry("y", avatarY), entry("z", avatarZ))),
			                               entry("1", Map.ofEntries(entry("x", 0), entry("y", 180), entry("z", 0))),
										   entry("2", Map.ofEntries(entry("x", 1), entry("y", 1), entry("z", 1))),
										   entry("3", Map.ofEntries(entry("avatarSrc", avatarUrl), 
                                                                       entry("avatarType", "skinnable"), 
                                                                       entry("muted", true), 
                                                                       entry(   "isSharingAvatarCamera", false))),
			                               entry("4", Map.ofEntries(entry("left_hand_pose", 0), entry("right_hand_pose", 0))),
										   entry("5", Map.ofEntries(entry("x", 0), entry("y", 1.6), entry("z", 0))),
										   entry("6", Map.ofEntries(entry("x", 0), entry("y", 0), entry("z", 0))),
			                               entry("7", Map.ofEntries(entry("x", 0), entry("y", 0), entry("z", 0))),
										   entry("8", Map.ofEntries(entry("x", 0), entry("y", 0), entry("z", 0))),
										   entry("9", false),
			                               entry("10", Map.ofEntries(entry("x", 0), entry("y", 0), entry("z", 0))),
										   entry("11", Map.ofEntries(entry("x", 0), entry("y", 0), entry("z", 0))),
										   entry("12", false));

            Map data = Map.ofEntries(
                                     entry("networkId", avatarId),
                                     entry("owner", sessionId),
                                     entry("creator", sessionId),
                                     entry("template", "#remote-avatar"),
			                         entry("persistent", false),
			                         entry("isFirstSync", firstAvatarSync),
			                         entry("components", components));

			room.sendMessage("naf", Map.ofEntries(entry("dataType", "u"), 
			                                      entry("data", data)));

            // firstAvatarSync = false;

            System.out.println("SUCCESS to update avatar");
        } catch (Exception ex) {
            System.err.println("Hubs connection failed");
            ex.printStackTrace();
        }
    }

    // https://sketchfab.com/models/6511da7be4714b7a896f25ee51bf54e8
    public String createObject(String assetUrl, float x, float y, float z) {
        try {
            room.sendMessage("events:object_spawned", Map.ofEntries(entry("object_type", 2)));
            String objectId = generateRandomAlphaNumericString(7);

			// Initialize/update asset
			Map components = Map.ofEntries(entry("0", Map.ofEntries(entry("x", x), entry("y", y), entry("z", z))),
			                               entry("1", Map.ofEntries(entry("x", 0), entry("y", 180), entry("z", 0))),
										   entry("2", Map.ofEntries(entry("x", 1), entry("y", 1), entry("z", 1))),
										   entry("3", Map.ofEntries(entry("src", assetUrl), 
                                                                       entry("moveTheParentNotTheMesh", false), 
                                                                       entry("fitToBox", true), 
                                                                       entry("resolve", true),
                                                                       entry("animate", true),
                                                                       entry("version", 1),
                                                                       entry("fileIsOwned", true),
                                                                       entry("playSoundEffect", true),
                                                                       entry("fileId", ""))));

            Map data = Map.ofEntries(
                                     entry("networkId", objectId),
                                     entry("owner", sessionId),
                                     entry("creator", sessionId),
                                     entry("template", "#interactable-media"),
			                         entry("persistent", false),
			                         entry("isFirstSync", true),
			                         entry("components", components));

			room.sendMessage("naf", Map.ofEntries(entry("dataType", "u"), 
			                                      entry("data", data)));

            System.out.println("SUCCESS to create object");

			return objectId;
        } catch (Exception ex) {
            System.err.println("Hubs connection failed");
            ex.printStackTrace();
        }
		return null;
	}
    
    public boolean moveObject(String id, float x, float y, float z) {
        try {
            String nafValue = createNAFString(id, sessionId, sessionId, "#interactable-media", x, y, z, 0, 180, 0);
			room.sendMessage("nafr", Map.ofEntries(entry("naf", nafValue)));

            System.out.println("SUCCESS to move object");

            return true;
        } catch (Exception ex) {
            System.err.println("Hubs connection failed");
            ex.printStackTrace();
        }
        return false;
    }

    private String createNAFString(String networkId, String owner, String creator, String template, float x, float y, float z, float ax, float ay, float az) {
        return "{\"dataType\":\"um\",\"data\":{\"d\":[{\"networkId\":\"" + networkId + "\",\"owner\":\"" + owner + "\",\"creator\":\"" + creator + "\",\"lastOwnerTime\":1655201191717.35,\"template\":\"" + template + "\",\"persistent\":false,\"parent\":null,\"components\":{\"0\":{\"x\":" + x + ",\"y\":" + y + ",\"z\":" + z + "},\"1\":{\"x\":" + ax + ",\"y\":" + ay + ",\"z\":" + az + "}}}]}}";
    }

    /**
     * Set the scene in the currently open room and return true if succesful.
     * 
     * @example SetScene
     * @param sceneUrl The URL of the new scene file
     * 
     * @return boolean
     */
    public boolean setScene(String sceneUrl) {
        URL url;
        try {
           url = new URL(sceneUrl);
        } catch (Exception ex) {
            System.err.println("URL creation failed");
            ex.printStackTrace();
            return false;
        }

        try {
            // avn.portal.HubsApi.sendMessage(HUBS_SERVER, authToken, roomId, "update_scene", Map.ofEntries(entry("url", url)), userName);
			room.sendMessage("update_scene", Map.ofEntries(entry("url", url)));
            return true;
        } catch (Exception ex) {
            System.err.println("Hubs connection failed");
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Return the current Hubs server URL
     * 
     * @return String
     */
    public String getServer() {
        return HUBS_SERVER.toString();
    }

    /**
     * Set the hubs server URL and return true is succesful.
     * 
     * @return boolean
     */		
    public boolean setServer(String url) {
        try {
            HUBS_SERVER = new URL(url);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Return the version of the Library.
     * 
     * @return String
     */
    public static String version() {
        return VERSION;
    }
}