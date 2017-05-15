package connection;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler {
    private Socket socket;
    private OutputStream writer;
    private static RequestHandler ourInstance = new RequestHandler();

    public static RequestHandler getInstance() {
        return ourInstance;
    }

    private RequestHandler() {
    }

    public void sendMes(Request request) {
        System.out.println(request.toString());
        try {
            writer.write((request.toString()).getBytes());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNetThings(Socket socket) {
        try {
            this.socket = socket;
            writer = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
//        writer.close();
    }
}
