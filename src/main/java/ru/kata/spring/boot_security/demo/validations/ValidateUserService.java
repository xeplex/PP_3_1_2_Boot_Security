package ru.kata.spring.boot_security.demo.validations;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.model.User;

public interface ValidateUserService {
    void validateByUsernameAndEmail(User user, BindingResult bindingResult, Long id);

    Boolean validateByRoles(User user, BindingResult bindingResult, Model model);
}
