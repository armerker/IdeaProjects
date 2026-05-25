package edu.phystech.hw5.service;

import edu.phystech.hw5.annotation.validation.NotBlank;
import edu.phystech.hw5.annotation.validation.Size;
import edu.phystech.hw5.exception.ValidationException;

import java.lang.reflect.Field;

/**
 * @author kzlv4natoly
 */
@FunctionalInterface
public interface Validator {
    void validate(Object object);

    static void performValidation(Object object) {
        if (object == null) {
            return;
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getType().equals(String.class)) {
                continue;
            }

            try {
                field.setAccessible(true);
                String value = (String) field.get(object);

                if (field.isAnnotationPresent(NotBlank.class)) {
                    NotBlank notBlank = field.getAnnotation(NotBlank.class);
                    if (value == null || value.trim().isEmpty()) {
                        final String msg = notBlank.message();
                        throw new ValidationException() {
                            @Override
                            public String getMessage() {
                                return msg;
                            }
                        };
                    }
                }
                if (field.isAnnotationPresent(Size.class)) {
                    Size size = field.getAnnotation(Size.class);
                    int length = (value == null) ? 0 : value.length();
                    
                    if (length < size.min() || length > size.max()) {
                        final String msg = size.message();
                        throw new ValidationException() {
                            @Override
                            public String getMessage() {
                                return msg;
                            }
                        };
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Ошибка рефлексии: " + field.getName(), e);
            }
        }
    }
}
