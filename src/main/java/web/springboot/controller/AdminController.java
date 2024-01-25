package web.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.springboot.model.User;
import web.springboot.service.RoleService;
import web.springboot.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminController(UserService userService1, RoleService roleService, PasswordEncoder encoder) {
        this.userService = userService1;
        this.roleService = roleService;
        this.encoder = encoder;
    }


    @GetMapping("")
    public String showAllUsers(Model model, Principal principal, @ModelAttribute("newUser") User user) {
        model.addAttribute("localUser", userService.findByEmail(principal.getName()).get());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.findAllRole());
        return "all-users5";
    }


    @PostMapping("/new")
    public String createUser(@ModelAttribute("newUser")User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }


    @PatchMapping("/update")
    public String updateUser(@ModelAttribute("oneUser") User user) {
        User oldUser = userService.findById(user.getId());
        if (!oldUser.getPassword().equals(user.getPassword())) {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
