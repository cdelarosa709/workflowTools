package com.vmware.http.exception;

import java.net.HttpURLConnection;

public class InternalServerException extends ApiException {

    public InternalServerException(final String errorText) {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR, errorText);
    }

    public InternalServerException(final String errorText, Throwable throwable) {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR, errorText, throwable);
    }
}
