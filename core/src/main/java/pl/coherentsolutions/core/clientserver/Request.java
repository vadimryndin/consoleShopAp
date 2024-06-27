package pl.coherentsolutions.core.clientserver;

import java.io.Serializable;

public class Request implements Serializable {
    private final RequestType requestType;
    private final Object requestBody;

    public Request(RequestType requestType, Object requestBody) {
        this.requestType = requestType;
        this.requestBody = requestBody;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestType=" + requestType +
                ", requestBody=" + requestBody +
                '}';
    }
}
