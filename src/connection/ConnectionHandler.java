package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket;
    private PrintWriter writer;

    public ConnectionHandler() {
        try {
            socket = new Socket("192.168.1.5", 3456);
            new ResponseHandler(socket).start();
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("networking is working");
            sendMes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMes() {
        Request request = new Request("get", ">>", "name");
        System.out.println(request.toString());
        writer.write(request.toString());
        writer.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        writer.close();
        socket.close();
    }
}
