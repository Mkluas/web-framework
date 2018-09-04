package cn.mklaus.framework.extend;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 限制字符串长度，一个英文字符长度为1，一个中文汉字长度为2。
 *
 * @author Mklaus
 * @date 2018-07-11 下午5:16
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CharLength.CharLengthValidator.class)
public @interface CharLength {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "{org.hibernate.validator.constraints.Length.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    class CharLengthValidator implements ConstraintValidator<CharLength, Object> {

        private int min;

        private int max;

        @Override
        public void initialize(CharLength constraintAnnotation) {
            this.min = constraintAnnotation.min();
            this.max = constraintAnnotation.max();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return false;
            }

            int length = countCharLength(value.toString());

            boolean valid = length >= min && length <= max;
            return valid;
        }

        private int countCharLength(String input) {
            if (input == null) {
                return 0;
            }

            int length = 0;
            for (char c : input.toCharArray()) {
                length += (c <= 0x00FF) ? 1 : 2;
            }

            return length;
        }
    }

}


