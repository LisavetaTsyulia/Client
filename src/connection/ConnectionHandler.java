package connection;

import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket;
    public ConnectionHandler() {
        try {
            socket = new Socket("127.0.0.1", 3456);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }
}
