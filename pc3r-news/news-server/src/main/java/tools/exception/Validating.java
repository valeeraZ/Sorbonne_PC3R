package tools.exception;

import com.google.common.collect.ImmutableMap;

import javax.validation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Validating {
    public static Validator createValidator(){
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        Validator validator = factory.getValidator();
        factory.close();
        return validator;
    }

    public static <T> void validate(T bean){
        Validator validator = createValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if(violations.size() > 0){
            Map<String, Object> m = new HashMap<>();
            for(ConstraintViolation<T> constraint : violations){
                m.put(constraint.getPropertyPath().toString(), constraint.getMessage());
            }
            ImmutableMap<String, Object> errors = ImmutableMap.copyOf(m);
            throw new MethodArgumentInvalidException(errors);
        }
    }
}
