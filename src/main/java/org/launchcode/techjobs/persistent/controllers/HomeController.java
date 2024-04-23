package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.*;
import org.launchcode.techjobs.persistent.models.data.*;
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
    @Autowired
    private LocationRepository locationRepository;

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
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute(new Job());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, HttpSession session,
                                    @RequestParam List<Integer> skills,
                                    @RequestParam int location) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            model.addAttribute("skills", skillRepository.findAll());
            model.addAttribute("locations", locationRepository.findAll());
            model.addAttribute(new Job());
            return "add";
        }

        Optional<User> employerResult =
                userRepository.findById((Integer) session.getAttribute(UserController.userSessionKey));
        Optional<Location> locationResult = locationRepository.findById(location);
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);
        newJob.setEmployer(employerResult.get());
        newJob.setLocation(locationResult.get());
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
