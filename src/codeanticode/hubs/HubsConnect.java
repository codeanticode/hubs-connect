package codeanticode.hubs;

import java.util.Map;
import static java.util.Map.entry;
import java.net.URL;

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

            if (room.isOpen()) {
                System.out.println("SUCCESS to open room");
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
            room.close();
            System.out.println("SUCCESS to close room");			
        } catch (Exception ex) {
            System.err.println("Hubs connection failed");
            ex.printStackTrace();
        }
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
        try {
            avn.portal.HubsApi.sendMessage(HUBS_SERVER, authToken, roomId, "update_scene", Map.ofEntries(entry("url", sceneUrl)), userName);
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