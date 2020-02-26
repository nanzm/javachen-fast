package com.javachen.web.validation;

import com.javachen.data.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("blRegistrationValidator")
public class RegistrationValidator implements Validator {

    private static final String DEFAULT_VALID_NAME_REGEX = "[A-Za-z'. -]{1,80}";

    private static final String DEFAULT_VALID_PASSWORD_REGEX = "[^\\s]{6,}";

    public void validate(User user, String password, String passwordConfirm, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "passwordConfirm.required");
        errors.pushNestedPath("user");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");
        errors.popNestedPath();

        if (errors.hasErrors()) {
            if (!passwordConfirm.equals(password)) {
                errors.rejectValue("passwordConfirm", "invalid");
            }

            if (!user.getUsername().matches(getValidNameRegex())) {
                errors.rejectValue("username", "username.invalid", null, null);
            }

            if (!user.getPassword().matches(getValidPasswordRegex())) {
                errors.rejectValue("password", "password.invalid", null, null);
            }

            if (!password.equals(passwordConfirm)) {
                errors.rejectValue("password", "passwordConfirm.invalid", null, null);
            }

//            if (!GenericValidator.isEmail(customer.getEmailAddress())) {
//                errors.rejectValue("emailAddress", "emailAddress.invalid", null, null);
//            }
        }
    }

    public static String getValidNameRegex() {
        return System.getProperty("name.valid.regex", DEFAULT_VALID_NAME_REGEX);
    }

    public static String getValidPasswordRegex() {
        return System.getProperty("password.valid.regex", DEFAULT_VALID_PASSWORD_REGEX);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
    }
}
