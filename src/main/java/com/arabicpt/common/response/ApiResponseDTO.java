package com.arabicpt.common.response;

public class ApiResponseDTO<T> {
    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    private ApiResponseDTO(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponseDTO<Void> success() {
        return new ApiResponseDTO<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> ApiResponseDTO<T> success(ResultCode resultCode, T data) {
        return new ApiResponseDTO<>(true, resultCode.getCode(), resultCode.getMessage(), data);
    }

    public static ApiResponseDTO<Void> fail(ResultCode resultCode) {
        return new ApiResponseDTO<>(false, resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static ApiResponseDTO<Object> fail(ResultCode resultCode, Object errorData) {
        return new ApiResponseDTO<>(false, resultCode.getCode(), resultCode.getMessage(), errorData);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
