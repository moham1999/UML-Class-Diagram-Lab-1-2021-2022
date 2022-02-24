package softwaredesign.projectManager;

import java.util.*;

public class Task {
    private final Status status;
    private final String name;
    private static final UUID uuid = UUID.randomUUID();
    private final Map<Employee, QualStatus> assignedEmployees;
    private final List<Skill> requiredSkills;
    private final TaskList dependentTasks;
    private final double timeSpent;

    //Used mostly to update a variable within the class, or create an instance from scratch.
    public Task (String name, Map<Employee, QualStatus> assignedEmployees, Status status, List<Skill> requiredSkills, double timeSpent, TaskList dependentTasks) {
        this.name = name;
        this.assignedEmployees = assignedEmployees;
        this.status = status;
        this.requiredSkills = requiredSkills;
        this.timeSpent = timeSpent;
        this.dependentTasks = dependentTasks;
    }

    //Used mostly to create an instance of this class from object.
    public Task (String name, double estimatedTime, Map<Employee, QualStatus> assignedEmployees, List<Skill> requiredSkills, TaskList dependentTasks) {
        this.name = name;
        this.assignedEmployees = assignedEmployees;
        this.status = decideStatus();
        this.requiredSkills = requiredSkills;
        this.timeSpent = 0d;
        this.dependentTasks = dependentTasks;
    }

    public Task (String name, double estimatedTime, List<Skill> requiredSkills) {
        this.name = name;
        this.assignedEmployees = new HashMap<>();
        this.status = decideStatus();
        this.requiredSkills = requiredSkills;
        this.timeSpent = 0d;
        this.dependentTasks = new TaskList("Dependent Tasks");
    }


    public String getTaskName () {
        return this.name;
    }

    //setAssignedEmployee instead of assign employee
    public Task setAssignedEmployees (Map<Employee, QualStatus> setAssignedEmployees) {
        return new Task(this.name, setAssignedEmployees, this.status, this.requiredSkills, this.timeSpent, this.dependentTasks);
    }

    public List<Employee> getAssignedEmployees () {
        return new ArrayList<>(this.assignedEmployees.keySet());
    }

    public Task assignEmployeeToTask (Employee employee) {
        if (isAssigned(employee)) {
            System.err.println("Employee already assigned");
            return this;
        }
        else {
            Map<Employee, QualStatus> copiedAssignedEmployees = new HashMap<>(this.assignedEmployees);
            if (isQualified(employee)) {
                copiedAssignedEmployees.put(employee, QualStatus.QUALIFIED);
            }
            else {
                System.err.println(employee.getName() + " is not qualified to perform this task." + employee.getName() + " not assigned to task");
                copiedAssignedEmployees.put(employee, QualStatus.UNQUALIFIED);
            }
            return new Task(this.name, copiedAssignedEmployees, this.status, this.requiredSkills, this.timeSpent, this.dependentTasks);
        }
    }

    //method to replace any of the given lists? like to replace the assigned employee list
    public List<Skill> getRequiredSkills () { return this.requiredSkills;}

    public Task addRequiredSkill (Skill skill) {
        List<Skill> copiedRequiredSkill = new ArrayList<> (this.requiredSkills);
        copiedRequiredSkill.add(skill);
        return new Task(this.name , this.assignedEmployees, this.status, copiedRequiredSkill, this.timeSpent, this.dependentTasks);
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
        return new Task (this.name, this.assignedEmployees, status, this.requiredSkills, this.timeSpent, this.dependentTasks);
    }

    public Status getStatus () {
        return this.status;
    }

    public void printStatus() {
        this.status.printStatus();
    }

    public double getTimeSpent() {return this.timeSpent;}

    public Task setTimeSpent(double timeSpent) {
        return new Task (this.name, this.assignedEmployees, this.status, this.requiredSkills, timeSpent, this.dependentTasks);

    }

    public List<Task> getDependentTasks () {
        return this.dependentTasks.getTaskList();
    }

    public Task setDependentTasks (List<Task> dependentTasks) {
        return new Task(this.name, this.assignedEmployees, this.status, this.requiredSkills, this.timeSpent, this.dependentTasks.setTaskList(dependentTasks));
    }

    public UUID getUuid() {
        return this.uuid;
    }

    private boolean taskAssigned () {
        return this.assignedEmployees.size() >= 1;
    }

    private boolean isAssigned (Employee employee) {
        return this.assignedEmployees.containsKey(employee);
    }

    public boolean started () {
        return this.timeSpent > 0;
    }

    private boolean isQualified (Employee employee) {
        return requiredSkills.containsAll(employee.getSkills());
    }

    private boolean allQualified() {
        return !assignedEmployees.containsValue(QualStatus.UNQUALIFIED);
    }

    private boolean isDependent (Task task) {
        return this.dependentTasks.contains(task.getUuid());
    }

    private boolean dependentTasksRemain() {
        return !dependentTasks.getTaskList().isEmpty();
    }

    private Status decideStatus () {
        if (taskAssigned() && started() && !dependentTasksRemain() && allQualified()) return new Status(Status.Progress.FINISHED);
        else if (taskAssigned() && started() && (dependentTasksRemain() || allQualified())) return new Status(Status.Progress.ONHOLD);
        else if (taskAssigned() && started()) return new Status(Status.Progress.EXECUTING);
        else if (taskAssigned()) return new Status(Status.Progress.READY);
        else return new Status(Status.Progress.CREATED);
    }
}
