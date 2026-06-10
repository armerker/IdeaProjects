package edu.phystech.hw5;

import java.lang.reflect.Field;
import edu.phystech.hw5.annotation.validation.NotBlank;
import edu.phystech.hw5.annotation.validation.Size;
import edu.phystech.hw5.exception.ValidationException;
import edu.phystech.hw5.service.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidatorTest {

    private Validator validator = Validator::performValidation; {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() != String.class) continue;
            
            String value = (String) field.get(object);
            
            if (field.isAnnotationPresent(NotBlank.class)) {
                NotBlank notBlank = field.getAnnotation(NotBlank.class);
                if (value == null || value.trim().isEmpty()) {
                    String message = notBlank.message();
                    throw new ValidationException(message.isEmpty() ? "Field cannot be blank" : message);
                }
            }
            
            if (field.isAnnotationPresent(Size.class)) {
                Size size = field.getAnnotation(Size.class);
                if (value == null) {
                    throw new ValidationException(size.message().isEmpty() ? "Size validation failed" : size.message());
                }
                int len = value.length();
                if (len < size.min()) {
                    throw new ValidationException(size.message().isEmpty() ? "Length < " + size.min() : size.message());
                }
                if (len > size.max()) {
                    throw new ValidationException(size.message().isEmpty() ? "Length > " + size.max() : size.message());
                }
            }
        }
    };

    @Test
    void notBlankWorks() {
        class Example {
            @NotBlank private final String x;
            @NotBlank(message = "This is a very important field and it can't be empty!") private final String y;
            Example(String x, String y) { this.x = x; this.y = y; }
        }
        Assertions.assertDoesNotThrow(() -> validator.validate(new Example("123", "567")));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> validator.validate(new Example("11", "")));
        Assertions.assertEquals("This is a very important field and it can't be empty!", exception.getMessage());
    }

    @Test
    void sizeWorks() {
        class Example {
            @Size(max = 52, message = "Long live Saint Petersburg!") private final String x;
            @Size(min = 5, max = 11) private final String y;
            Example(String x, String y) { this.x = x; this.y = y; }
        }
        Assertions.assertDoesNotThrow(() -> validator.validate(new Example("123", "567765")));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> validator.validate(new Example("", "")));
        Assertions.assertEquals("Long live Saint Petersburg!", exception.getMessage());
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(new Example("", "0000000000000")));
    }
}
