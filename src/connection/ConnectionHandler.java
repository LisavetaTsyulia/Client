package connection;

import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket;

    public ConnectionHandler() {
        try {
            socket = new Socket("localhost", 3456);
            new ResponseHandler(socket).start();
            System.out.println("networking is working");
            RequestHandler.getInstance().setNetThings(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
