package com.arabicpt.common.exception;

import com.arabicpt.common.response.ResultCode;

public class BusinessException extends RuntimeException {
    private final ResultCode resultCode;
    private final String overrideMessage;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.overrideMessage = null;
    }

    public BusinessException(ResultCode resultCode, String overrideMessage) {
        super(overrideMessage);
        this.resultCode = resultCode;
        this.overrideMessage = overrideMessage;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    @Override
    public String getMessage() {
        return overrideMessage != null ? overrideMessage : resultCode.getMessage();
    }
}
