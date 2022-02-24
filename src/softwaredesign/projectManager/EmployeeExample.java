package softwaredesign.projectManager;
import java.util.*;
import java.util.UUID;
import java.util.ArrayList; // import the ArrayList class

public class EmployeeExample {

    private String name;
    private UUID uuid;
    private double workedHours;
    private List<Skill> skills;

    //Limit maxWorkHours to 40, add a boolean to ensure it does not top it.

    public EmployeeExample(String name, double maxWorkHours, List<Skill> skills) {
        this.name = name;
        this.workedHours = maxWorkHours;
        this.skills = skills;
        this.uuid = UUID.randomUUID();
    }

    //Extra constructor
    public EmployeeExample(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.workedHours = 0D;
        this.skills = new ArrayList<>();
    }

    public void setName(String newName) {
        this.name = String.copyValueOf(newName.toCharArray());
    }

    public EmployeeExample(Employee employee) {
        this.name = employee.getName();
        this.workedHours = employee.getHours();
        this.skills = employee.getSkills();
        this.uuid = employee.getUuid();
    }

    public String getName() {
        return String.copyValueOf(this.name.toCharArray());
    }

    public void setHours(Double hours) {
        this.workedHours = Double.valueOf(hours);
    }

    public Double getHours() {
        return Double.valueOf(this.workedHours);
    }

    public void setSkills(List<Skill> newSkills) {
        this.skills = new ArrayList<>(newSkills);
    }

    public List<Skill> getSkills() {
        return Collections.unmodifiableList(this.skills);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void removeSkill(Skill skill) {
        skill = new Skill(skill);
        this.skills.remove(skill);
    }

    public void addSkill(Skill skill) {
        skill = new Skill(skill);
        this.skills.add(skill);
    }

    public void print () {
        String messageToBePrinted = "Employee name:" + this.name + "\nHours worked: " + this.workedHours + "\nSkills: ";
        System.out.print(messageToBePrinted);

        for (Skill currentSkill : this.skills) {
            System.out.print(currentSkill.getName() + " ");
        }
        System.out.println("\n");
    }
}