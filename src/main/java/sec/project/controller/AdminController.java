package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sec.project.repository.SignupRepository;
import sec.project.service.SignupService;

@Controller
public class AdminController {

    @Autowired
    SignupService signupService;

    @Autowired
    SignupRepository signupRepository;

    @RequestMapping("/admin")
    public String loadAdmin(Model model) {
        model.addAttribute("signups", signupService.list());
        return "admin";
    }

    @RequestMapping(value = "/admin/signups/{id}", method = RequestMethod.DELETE)
    public String removeSignup(@PathVariable Long id) {
        signupService.removeSignup(signupRepository.findOne(id));
        return "redirect:/admin";
    }
}
