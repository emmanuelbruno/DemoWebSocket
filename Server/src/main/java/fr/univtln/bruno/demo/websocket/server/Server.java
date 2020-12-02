package fr.univtln.bruno.demo.websocket.server;

import fr.univtln.bruno.demo.websocket.model.message.Message;
import lombok.extern.java.Log;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class provides a simple echo server (simple chat) using websockets
 * mvn package && java -jar Server/target/Server-2.0-jar-with-dependencies.jar
 */
@Log
// The path where this server listen and the classes to encode/decode the messages between Java and JSON using Jackson
@ServerEndpoint(value = "/echo",
        encoders = {Message.EncoderDecoder.class},
        decoders = {Message.EncoderDecoder.class})
public class Server {
    //The server listing IP address
    public static final String SERVER_IP;
    //The server listing TCP PORT
    public static final int SERVER_PORT;
    //The list of opened session
    private static final List<Session> sessions = new ArrayList<>();

    static {
        SERVER_IP = Optional.ofNullable(System.getProperty("fr.univtln.bruno.demo.websocket.server.ip")).orElse("localhost");
        int port = 8025;
        try {
            port = Integer.parseInt(Optional.ofNullable(System.getProperty("fr.univtln.bruno.demo.websocket.server.port")).orElse("8025"));
        } catch (NumberFormatException e) {
            log.severe("Server port is not a number, using default value");
            System.exit(0);
        }
        SERVER_PORT = port;
        log.info("Server IP:" + SERVER_IP + " Port: " + SERVER_PORT);
    }

    public static void main(String[] args) {
        log.info("Server starting...");
        org.glassfish.tyrus.server.Server server =
                new org.glassfish.tyrus.server.Server(SERVER_IP, SERVER_PORT, "/", null, Server.class);

        try {
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            log.info("Please press a key to stop the server.");
            reader.readLine();
        } catch (DeploymentException e) {
            log.severe("Server start error " + e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }

    /**
     * Reaction method when a client connect. We add thi session to the list of known sessions.
     *
     * @param session        the new session
     * @param endpointConfig the parameters of this session
     */
    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        log.info("new Client connected in session " + session.getId() + " parameters: " + endpointConfig.getUserProperties());
        sessions.add(session);
    }

    /**
     * Reaction method when a message is received. We simply send it to all the known sessions.
     *
     * @param message The message as a java instance decoded from JSON
     * @param peer    The session origin of the message
     * @throws IOException     Network exception
     * @throws EncodeException Decoding exception
     */
    @OnMessage
    public void echo(Message message, Session peer) throws IOException, EncodeException {
        log.info("From client: " + peer.getId() + " Received: " + message);
        for (Session session : sessions)
            session.getBasicRemote().sendObject(message);
    }

    /**
     * Reaction method when a client leave.
     * Remove the session from the known sessions.
     *
     * @param session        the ended session
     * @param endpointConfig the parameters of this session
     */
    @OnClose
    public void onClose(final Session session, EndpointConfig endpointConfig) {
        log.info(session.getId() + " left with parameters: " + endpointConfig.getUserProperties());
        sessions.remove(session);
    }
}