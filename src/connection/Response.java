package connection;

public class Response {
    private String respFromServer;
    public Response(String line) {
        respFromServer = line;
    }

    @Override
    public String toString() {
        return respFromServer;
    }
}
