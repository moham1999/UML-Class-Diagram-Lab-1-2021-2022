package softwaredesign.projectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task {
    private final Status status;
    private final String name;
    private final double estimatedTime;
    private static final UUID uuid = UUID.randomUUID();
    private final List<Employee> assignedEmployees;
    private final List<Skill> requiredSkills;
    private final double timeSpent;

    //Used mostly to update a variable within the class, or create an instance from scratch.
    public Task (String name, double estimatedTime, List<Employee> assignedEmployees, Status status, List<Skill> requiredSkills, double timeSpent) {
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.assignedEmployees = assignedEmployees;
        this.status = status;
        this.requiredSkills = requiredSkills;
        this.timeSpent = timeSpent;
    }

    //Used mostly to create an instance of this class from object.
    public Task (String name, double estimatedTime, List<Employee> assignedEmployees, List<Skill> requiredSkills) {
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.assignedEmployees = assignedEmployees;
        this.status = new Status(Status.Progress.READY);
        this.requiredSkills = requiredSkills;
        this.timeSpent = 0d;
    }

    public Task (String name, double estimatedTime, List<Skill> requiredSkills) {
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.assignedEmployees = new ArrayList<>();
        this.status = new Status(Status.Progress.CREATED);
        this.requiredSkills = requiredSkills;
        this.timeSpent = 0d;
    }


    public String getTaskName () {
        return this.name;
    }

    public Task setEstimatedTime (double estimatedTime) {
        return new Task(this.name, estimatedTime, this.assignedEmployees, this.status, this.requiredSkills, this.timeSpent);
    }

    public double getEstimateTime () {
        return this.estimatedTime;
    }

    //setAssignedEmployee instead of assign employee
    public Task setAssignedEmployees (List<Employee> setAssignedEmployees) {
        return new Task(this.name , this.estimatedTime, setAssignedEmployees, this.status, this.requiredSkills, this.timeSpent);
    }

    public List<Employee> getAssignedEmployees () {
        return this.assignedEmployees;
    }

    public Task assignEmployeeToTask (Employee employee) {
        if (isAssigned(employee)) {
            System.err.println("Employee already assigned");
            return this;
        }
        else {
            if (isQualified(employee)) {
                List<Employee> copiedAssignedEmployees = new ArrayList<>(assignedEmployees);
                copiedAssignedEmployees.add(employee);
                return new Task(this.name, this.estimatedTime, copiedAssignedEmployees, this.status, this.requiredSkills, this.timeSpent);
            }
            else {
                System.err.println(employee.getName() + " is not qualified to perform this task." + employee.getName() + " not assigned to task");
                return this;
            }
        }
    }

    //method to replace any of the given lists? like to replace the assigned employee list
    public List<Skill> getRequiredSkills () { return requiredSkills;}

    public Task addRequiredSkill (Skill skill) {
        List<Skill> copiedRequiredSkill = new ArrayList<> (requiredSkills);
        copiedRequiredSkill.add(skill);
        return new Task(this.name , this.estimatedTime, this.assignedEmployees, this.status, copiedRequiredSkill, this.timeSpent);
    }

    public Task setStatus (Status newStatus) {
        //Could add switch statement here, but since it's just two cases, this suffices.
        if (newStatus.getProgress() != Status.Progress.CREATED && !taskAssigned()) {
                System.err.println("No employees assigned to task. Status not changed");
                return this;
        }
        else if (newStatus.getProgress() == Status.Progress.EXECUTING && !started()) {
            System.err.println("No amount of hours worked on this task have been logged. Status not changed");
            return this;
        }
        return new Task (this.name, this.estimatedTime, this.assignedEmployees, status, this.requiredSkills, this.timeSpent);
    }

    public Status getStatus () {
        return this.status;
    }

    public void printStatus() {
        this.status.printStatus();
    }

    public double getTimeSpent() {return this.timeSpent;}

    public Task setTimeSpent(double timeSpent) {
        return new Task (this.name, this.estimatedTime, this.assignedEmployees, this.status, this.requiredSkills, timeSpent);

    }

    public UUID getUuid() {
        return this.uuid;
    }

    private boolean taskAssigned () {
        return this.assignedEmployees.size() >= 1;
    }

    private boolean isAssigned (Employee employee) {
        return this.assignedEmployees.contains(employee);
    }

    public boolean started () {
        return this.timeSpent > 0;
    }

    private boolean isQualified (Employee employee) {
        return requiredSkills.containsAll(employee.getSkills());
    }
}
