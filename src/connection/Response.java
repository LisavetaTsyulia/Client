package connection;

public class Response {
    private String[] arrFromServer ;
    private String respFromServer;

    public Response(String line) {
        lineToArray(line);
        respFromServer = line;
    }

    @Override
    public String toString() {
        return respFromServer;
    }

    private void lineToArray(String line) {
        String[] parts = line.split(";");
        arrFromServer = parts;
    }

    public String[] getArray() {
        return arrFromServer;
    }
}
