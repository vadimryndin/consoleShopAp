package pl.coherentsolutions.core.clientserver;

import java.io.Serializable;

public class Response implements Serializable {
    private final Object responseBody;

    public Response(Object responseBody) {
        this.responseBody = responseBody;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseBody=" + responseBody +
                '}';
    }
}
