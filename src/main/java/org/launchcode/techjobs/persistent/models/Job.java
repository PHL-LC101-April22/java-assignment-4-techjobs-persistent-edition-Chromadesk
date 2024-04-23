package org.launchcode.techjobs.persistent.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Job extends AbstractEntity {

    @ManyToOne
    private User employer;

    @ManyToOne
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @NotNull
    @Min(value = 1, message = "Salary can't be less than 1.")
    private int salary;

    @ManyToMany
    private List<Skill> skills;

    @NotNull
    @Size(max = 2500, message = "Description must be within 2500 characters.")
    private String description;

    public Job() {
    }

    public Job(User anEmployer, List<Skill> someSkills,
               int aSalary, String aDescription) {
        super();
        this.employer = anEmployer;
        this.skills = someSkills;
        this.salary = aSalary;
        this.description = aDescription;
    }

    // Getters and setters.

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
