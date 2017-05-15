package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ResponseHandler extends Thread{
    private Socket socket;
    boolean isAlive = true;

    public ResponseHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            socket.setSoTimeout(100);
            while (isAlive) {
                try {
                    String line = br.readLine();
                    if (line != null) {
                        Response response = new Response(line);
                        CurrentResponse.getInstance().setCurrentResponse(response);
                        System.out.println(response.toString());
                    }
                } catch (SocketTimeoutException ignored) {
                } catch (Exception e) {
                    System.out.println("Something is wrong(");
                    isAlive = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
