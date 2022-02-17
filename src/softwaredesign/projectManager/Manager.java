package softwaredesign.projectManager;

import java.util.List;

public class Manager extends Employee{
    private final List<Employee> subordinates;

    public Manager(String name, double maxWorkHours, List<Skill> skills, List<Employee> subordinates) {
        super(name, maxWorkHours, skills);
        this.subordinates = subordinates;
    }

    public List<Employee> getSubordinates() {
        return this.subordinates;
    }

    public Manager setSubordinates(List<Employee> subordinates) {

        return new Manager(super.getName(), super.getHours(), super.getSkills(), subordinates);
    }

    @Override
    public String toString() {
        return "Manager [subordinates=" + subordinates + ", details=" + super.toString() + "]";
    }
}
