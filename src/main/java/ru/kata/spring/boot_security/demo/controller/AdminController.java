package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getUsers(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("users", userService.getAll());
        List<User> users = userService.getAll();
        for (User user : users) {
            user.setRoles(roleService.findRolesByUserId(user.getId()));
        }
        model.addAttribute("users", users);
        model.addAttribute("username", username);
        return "users";
    }

    @GetMapping("/showInfo")
    public String info(@RequestParam Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", userService.getById(id));
        List<Role> userRoles = (List<Role>) user.getRoles();
        model.addAttribute("userRoles", userRoles);
        return "userForAdmins";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        List<Role> roles = roleService.getAll();
        model.addAttribute("allRoles", roles);
        return "user-info";
    }

    @GetMapping("/editUser")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        List<Role> roles = roleService.getAll();
        model.addAttribute("allRoles", roles);
        return "update";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "redirect:/admin";
    }

    @GetMapping("/back")
    public String back() {
        return "redirect:/admin";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = roleService.getAll();
            model.addAttribute("allRoles", roles);
            return "user-info";
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            bindingResult.rejectValue("roles", "error.user",
                    "Please select at least one role");
            List<Role> roles = roleService.getAll();
            model.addAttribute("allRoles", roles);
            return "user-info";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/updateUser")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                             @RequestParam("id") Long id,
                             Model model) {
        if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("email")) {
            List<Role> roles = roleService.getAll();
            model.addAttribute("allRoles", roles);
            return "update";
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            bindingResult.rejectValue("roles", "error.user",
                    "Please select at least one role");
            List<Role> roles = roleService.getAll();
            model.addAttribute("allRoles", roles);
            return "update";
        }
        userService.update(user, id);
        return "redirect:/admin";
    }


    @PostMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/logout";
    }
}
