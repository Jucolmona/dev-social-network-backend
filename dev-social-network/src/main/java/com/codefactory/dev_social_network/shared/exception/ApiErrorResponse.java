package com.codefactory.dev_social_network.shared.exception;

import java.util.List;

public class ApiErrorResponse {

    private String errorCode;
    private String message;
    private List<ErrorDetail> details;
    private String traceId;

    public ApiErrorResponse(
            String errorCode,
            String message,
            List<ErrorDetail> details,
            String traceId
    ) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
        this.traceId = traceId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public List<ErrorDetail> getDetails() {
        return details;
    }

    public String getTraceId() {
        return traceId;
    }
}