//package learn.field_agent.validation;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//
//public class CheckDateValidator implements ConstraintValidator <CheckDate, String> {
//
//    @Override
//    public void initialize(CheckDate constraintAnnotation) {
//        this.pattern = constraintAnnotation.pattern();
//    }
//    @Override
//    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
//        if ( object == null ) {
//            return true;
//        }
//
//        try {
//            LocalDate date = new SimpleDateFormat(pattern).parse(object);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
