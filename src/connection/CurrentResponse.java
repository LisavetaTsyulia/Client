package connection;

public class CurrentResponse {
    private static CurrentResponse ourInstance = new CurrentResponse();
    private Response currentResponse;
    public static CurrentResponse getInstance() {
        return ourInstance;
    }

    private CurrentResponse() {
    }

    public void setCurrentResponse(Response response) {
        currentResponse = response;
    }

    public Response getCurrentResponse() {
        return currentResponse;
    }
}
