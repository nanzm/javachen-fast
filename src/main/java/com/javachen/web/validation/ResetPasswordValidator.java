package com.javachen.web.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ResetPasswordValidator implements Validator {
    public void validate(String username, String password, String confirmPassword, Errors errors) {
        if (StringUtils.isEmpty(password)) {
            errors.reject("password", "password.required");
        }

        if (StringUtils.isEmpty(username)) {
            errors.reject("username", "username.required");
        }

        if (!errors.hasErrors()) {
            if (!password.matches(RegistrationValidator.getValidPasswordRegex())) {
                errors.rejectValue("password", "password.invalid", null, null);
            } else {
                if (!password.equals(confirmPassword)) {
                    errors.rejectValue("password", "passwordConfirm.invalid", null, null);
                }
            }
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
    }
}
