package com.arabicpt.common.exception;

import com.arabicpt.common.response.ApiResponseDTO;
import com.arabicpt.common.response.ErrorResponseDTO;
import com.arabicpt.common.response.ResultCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleBusinessException(BusinessException ex) {
        ResultCode code = ex.getResultCode();
        Map<String, Object> errorData = Map.of("reason", ex.getMessage());
        ApiResponseDTO<Object> body = ApiResponseDTO.fail(code, errorData);
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ErrorResponseDTO> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            // 어떤 필드가 왜 실패했는지 그대로 내려줍니다.
            errors.add(new ErrorResponseDTO(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        Map<String, Object> errorData = new HashMap<>();
        errorData.put("errors", errors);

        ResultCode code = ResultCode.INVALID_REQUEST;
        ApiResponseDTO<Object> body = ApiResponseDTO.fail(code, errorData);
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorResponseDTO> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String field = violation.getPropertyPath() == null ? "unknown" : violation.getPropertyPath().toString();
            errors.add(new ErrorResponseDTO(field, violation.getMessage()));
        }

        Map<String, Object> errorData = new HashMap<>();
        errorData.put("errors", errors);

        ResultCode code = ResultCode.INVALID_REQUEST;
        ApiResponseDTO<Object> body = ApiResponseDTO.fail(code, errorData);
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleException(Exception ex) {
        Map<String, Object> errorData = Map.of("reason", ex.getMessage());
        ResultCode code = ResultCode.INTERNAL_ERROR;
        ApiResponseDTO<Object> body = ApiResponseDTO.fail(code, errorData);
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }
}
