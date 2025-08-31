package miu.edu.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miu.edu.system.Result;
import miu.edu.system.StatusCode;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler({ObjectNotFoundException.class})
  public Result handleObjectNotFoundException(ObjectNotFoundException exception) {
    return new Result(false, StatusCode.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result handleValidationException(MethodArgumentNotValidException exception) {
    List<ObjectError> errors = exception.getBindingResult().getAllErrors();
    Map<String, String> errorMap = new HashMap<>(errors.size());
    for (ObjectError error : errors) {
      String key = ((FieldError) error).getField();
      String val = ((FieldError) error).getDefaultMessage();
      errorMap.put(key, val);
    }
    return new Result(false, StatusCode.INVALID_ARGUMENT, "Provided  arguments are invalid, see data for details.", errorMap);
  }

}
