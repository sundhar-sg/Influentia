package com.cognizant.influentia.exception;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, String> handleInvalidArgumentsException(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			log.error("The provided values are not valid for the arguments: {}", error.getDefaultMessage());
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleNoSuchElementException(NoSuchElementException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleNullPointerException(Exception ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(ResourceQuotaExceededException.class)
	@ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
	@ResponseBody
	public String handleResourceQuotaExceededException(ResourceQuotaExceededException ex) {
		return ex.getMessage();
	}
}