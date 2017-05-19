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

    public String[] getCurResponseArray() {
        String[] arr = null;
        boolean isTrue = true;
        while (isTrue) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            if (CurrentResponse.getInstance().getCurrentResponse() != null) {
                arr = CurrentResponse.getInstance().getCurrentResponse().getArray();
                isTrue = false;
            }
        }
        CurrentResponse.getInstance().setCurrentResponse(null);
        return arr;
    }
}
