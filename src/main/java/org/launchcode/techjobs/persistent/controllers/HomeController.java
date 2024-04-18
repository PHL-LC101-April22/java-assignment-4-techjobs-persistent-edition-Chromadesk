package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.User;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.launchcode.techjobs.persistent.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillRepository skillRepository;

    @RequestMapping("")
    public String index(Model model, HttpSession session) {

        model.addAttribute("title", "My Jobs");
        model.addAttribute("isLoggedOut",
                session.getAttribute(UserController.userSessionKey) == null);

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model, HttpSession session) {
        model.addAttribute("title", "Post New Job");
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute(new Job());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, HttpSession session,
                                    @RequestParam List<Integer> skills) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            model.addAttribute("skills", skillRepository.findAll());
            model.addAttribute(new Job());
            return "add";
        }

        Optional<User> employerResult =
                userRepository.findById((Integer) session.getAttribute(UserController.userSessionKey));
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);
        newJob.setEmployer(employerResult.get());
        jobRepository.save(newJob);

        return "list";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

        Optional<Job> result = jobRepository.findById(jobId);
        if (result.isEmpty()) {
            return "redirect:";
        }
        model.addAttribute("job", result.get());
        return "view";
    }


}
