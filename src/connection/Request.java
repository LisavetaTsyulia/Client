package connection;

public class Request {
    private String code;
    private String separator;
    private String field;
    private String body = "";

    public Request(String code, String separator, String field, String body) {
        this.code = code;
        this.separator = separator;
        this.field = field;
        this.body = body;
    }


    public String toString() {
        String result = code + separator + field + separator + body + "\r\n";
        return result;
    }
}
