package com.guilherme.stock.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.guilherme.stock.dto.responses.SimpleResponse;
import com.guilherme.stock.httpExceptions.BadRequestException;
import com.guilherme.stock.httpExceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<SimpleResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {

        Throwable cause = exception.getCause();

        if (cause instanceof InvalidFormatException invalidFormat) {
            if (invalidFormat.getPath() != null && !invalidFormat.getPath().isEmpty()) {
                String fieldName = invalidFormat.getPath().getFirst().getFieldName();
                String value = invalidFormat.getValue().toString();
                String targetType = invalidFormat.getTargetType().getSimpleName();
                String message = String.format("O valor '%s' não é válido para o campo '%s'. O valor deve ser do tipo %s", value, fieldName, targetType);
                return ResponseEntity.badRequest().body(new SimpleResponse(message));
            }
        } else if (cause instanceof JsonMappingException jsonMapping) {
            if (jsonMapping.getPath() != null && !jsonMapping.getPath().isEmpty()) {
                String fieldName = jsonMapping.getPath().getFirst().getFieldName();
                String message = String.format("O campo '%s' é inválido", fieldName);
                return ResponseEntity.badRequest().body(new SimpleResponse(message));
            }
        }
        System.out.println("Erro ao ler a requisição: " + exception.getMessage());
        return ResponseEntity.badRequest().body(new SimpleResponse("Requisição inválida"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        System.out.println("Erro de validação: " + exception.getMessage());

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String message = fieldErrors.getFirst().getDefaultMessage();

        return ResponseEntity.badRequest().body(new SimpleResponse(message));

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<SimpleResponse> handleBadCredentialsException(BadCredentialsException exception) {
        System.out.println("Credenciais inválidas: " + exception.getMessage());
        return ResponseEntity.status(401).body(new SimpleResponse("Credenciais inválidas ou usuário não encontrado."));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<SimpleResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        System.out.println("Método não suportado: " + exception.getMessage());
        return ResponseEntity.status(405).body(new SimpleResponse("Método não suportado"));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SimpleResponse> handleNotFoundException(NotFoundException exception) {
        System.out.println("Recurso não encontrado: " + exception.getMessage());
        return ResponseEntity.status(404).body(new SimpleResponse(exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<SimpleResponse> handleBadRequestException(BadRequestException exception) {
        System.out.println("Requisição inválida: " + exception.getMessage());
        return ResponseEntity.badRequest().body(new SimpleResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleResponse> handleException(Exception exception) {
        System.out.println("Erro interno: " + exception.getMessage());
        return ResponseEntity.internalServerError().body(new SimpleResponse("Erro interno"));
    }
}
