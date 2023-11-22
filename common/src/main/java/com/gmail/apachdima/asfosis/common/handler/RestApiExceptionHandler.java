package com.gmail.apachdima.asfosis.common.handler;

import com.gmail.apachdima.asfosis.common.constant.common.CommonConstant;
import com.gmail.apachdima.asfosis.common.constant.message.Error;
import com.gmail.apachdima.asfosis.common.dto.RestApiErrorResponseDTO;
import com.gmail.apachdima.asfosis.common.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(fieldError -> errors.add(fieldError.getField()
                .concat(CommonConstant.COLON.getValue()).concat(CommonConstant.SPACE.getValue())
                .concat(Objects.nonNull(fieldError.getDefaultMessage()) ? fieldError.getDefaultMessage() : CommonConstant.SPACE.getValue())));
        ex.getBindingResult().getGlobalErrors()
            .forEach(objectError -> errors.add(objectError.getObjectName()
                .concat(CommonConstant.COLON.getValue()).concat(CommonConstant.SPACE.getValue())
                .concat(Objects.nonNull(objectError.getDefaultMessage()) ? objectError.getDefaultMessage() : CommonConstant.SPACE.getValue())));

        Object[] params = {ex.getBindingResult().getObjectName(), errors.size()};
        String message = messageSource.getMessage(Error.VALIDATION_REQUEST.getKey(), params, Locale.ENGLISH);
        RestApiErrorResponseDTO responseDTO = buildRestApiErrorResponse(message, HttpStatus.BAD_REQUEST, errors);
        return createResponseEntity(responseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = messageSource
            .getMessage(Error.MISSING_REQUEST_PARAMETER.getKey(), new Object[]{ex.getParameterName()}, Locale.ENGLISH);
        RestApiErrorResponseDTO responseDTO =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, List.of(error));
        return createResponseEntity(responseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String param = String.join(CommonConstant.SPACE.getValue(), ex.getHttpMethod(), ex.getRequestURL());
        String error = messageSource.getMessage(Error.NO_HANDLER_FOUND.getKey(), new Object[]{param}, Locale.ENGLISH);
        RestApiErrorResponseDTO responseDTO =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, List.of(error));
        return createResponseEntity(responseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String supportedMethods = Objects.nonNull(ex.getSupportedHttpMethods())
            ? ex.getSupportedHttpMethods().stream().map(HttpMethod::name).collect(Collectors.joining(CommonConstant.SPACE.getValue()))
            : CommonConstant.EMPTY.getValue();
        Object[] params = new Object[]{ex.getMethod(), supportedMethods};
        String error = messageSource.getMessage(Error.HTTP_METHOD_NOT_ALLOWED.getKey(), params, Locale.ENGLISH);
        RestApiErrorResponseDTO responseDTO =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, List.of(error));
        return createResponseEntity(responseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String supportedMediaTypes = ex.getSupportedMediaTypes().stream()
            .map(MimeType::toString).collect(Collectors.joining(CommonConstant.SPACE.getValue()));
        Object[] params = new Object[]{ex.getContentType(), supportedMediaTypes};
        String error = messageSource.getMessage(Error.MEDIA_TYPE_NOT_SUPPORTED.getKey(), params, Locale.ENGLISH);
        RestApiErrorResponseDTO responseDTO =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, List.of(error));
        return createResponseEntity(responseDTO);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex) {
        String message = messageSource.getMessage(Error.DATA_ACCESS.getKey(), null, Locale.ENGLISH);
        RestApiErrorResponseDTO responseDTO =
            buildRestApiErrorResponse(message, HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
        return createResponseEntity(responseDTO);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex) {
        String error = messageSource.getMessage(Error.INTERNAL_SERVER_ERROR_OCCURRED.getKey(), null, Locale.ENGLISH);
        RestApiErrorResponseDTO responseDTO =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, List.of(error));
        return createResponseEntity(responseDTO);
    }

    private ResponseEntity<Object> createResponseEntity(RestApiErrorResponseDTO responseDTO) {
        return new ResponseEntity<>(responseDTO, new HttpHeaders(), responseDTO.getStatus());
    }

    private RestApiErrorResponseDTO buildRestApiErrorResponse(String exceptionMessage, HttpStatus httpStatus, List<String> errors) {
        return RestApiErrorResponseDTO.builder()
            .status(httpStatus)
            .message(exceptionMessage)
            .errors(errors)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
