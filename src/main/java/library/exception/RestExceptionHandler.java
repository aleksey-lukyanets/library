package library.exception;

import library.exception.dto.ValidationErrorDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Обработчик исключений.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    //-------------------------------------------------- Обработчики исключений
    
    /**
     * Запрошенный автор не найден.
     * @param ex исключение
     */
    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleAuthorNotFoundException(AuthorNotFoundException ex) {
        //
    }
    
    /**
     * Запрошенная книга не найдена.
     * @param ex исключение
     */
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleBookNotFoundException(BookNotFoundException ex) {
        //
    }
    
    /**
     * Попытка повторной вставки существующего объекта.
     * @param ex исключение
     */
    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleDuplicateException(DuplicateException ex) {
        //
    }
    
    /**
     * Попытка сослаться на несуществующую страну.
     * @param ex исключение
     * @return перечень нарушенных ограничений
     */
    @ExceptionHandler(UnknownCountryException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ValidationErrorDTO handleUnknownCountryException(UnknownCountryException ex) {
        String[] codes = {"NotExist.authorDTO.country"};
        Object[] arguments = {"country"};
        List<FieldError> fieldErrors = Arrays.asList(new FieldError("authorDTO", "country", "", false, codes, arguments, ""));
        return processFieldErrors(fieldErrors);
    }
    
    /**
     * Попытка сослаться на несуществующего автора.
     * @param ex исключение
     * @return перечень нарушенных ограничений
     */
    @ExceptionHandler(UnknownAuthorException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ValidationErrorDTO handleUnknownAuthorException(UnknownAuthorException ex) {
        String[] codes = {"NotExist.bookDTO.author"};
        Object[] arguments = {"author"};
        List<FieldError> fieldErrors = Arrays.asList(new FieldError("bookDTO", "author", "", false, codes, arguments, ""));
        return processFieldErrors(fieldErrors);
    }
    
    /**
     * Ошибки валидации полученного от клиента объекта.
     * @param ex исключение
     * @return перечень нарушенных ограничений
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ValidationErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }
    
    //----------------------------------------- Компоновка сообщений об ошибках
    
    private ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        ValidationErrorDTO dto = new ValidationErrorDTO();

        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = resolveErrorMessage(fieldError);
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return dto;
    }

    private String resolveErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

        // Если подходящего сообщения не найдено - попытаться найти ближайшее по коду ошибки
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }
        return localizedErrorMessage;
    }
}
