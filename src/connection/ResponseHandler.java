package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ResponseHandler extends Thread{
    private Socket socket;
    boolean isAlive = true;

    public ResponseHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (isAlive) {
                try {
                    socket.setSoTimeout(2000);
                    String line = br.readLine();
                    if (line != null) {
                        Response response = new Response(line);
                        System.out.println(response);
                    }
                } catch (Exception e) {
                    isAlive = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
