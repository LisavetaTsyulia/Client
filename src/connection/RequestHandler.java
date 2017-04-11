package connection;

import java.net.Socket;

public class RequestHandler {
    private Socket socket;
    private Request request;
    private Response response;

    public RequestHandler(Socket socket) {
        this.socket = socket;
        response = new Response();
    }
}
