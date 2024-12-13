package ru.kata.spring.boot_security.demo.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Service
@Transactional
public class ValidateUserServiceImpl implements ValidateUserService {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ValidateUserServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void validateByUsernameAndEmail(User user, BindingResult bindingResult, Long id) {
        User existingUserByUsername = userService.findByUsername(user.getUsername());
        if (existingUserByUsername != null && (!existingUserByUsername.getId().equals(id))) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
        }
        User existingUserByEmail = userService.findByEmail(user.getEmail());
        if (existingUserByEmail != null && (!existingUserByEmail.getId().equals(id))) {
            bindingResult.rejectValue("email", "email.error", "Email already exists");
        }
    }

    @Override
    public Boolean validateByRoles(User user, BindingResult bindingResult, Model model) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            bindingResult.rejectValue("roles", "error.user",
                    "Please select at least one role");
            List<Role> roles = roleService.getAll();
            model.addAttribute("allRoles", roles);
            return true;
        }
        return false;
    }

}
