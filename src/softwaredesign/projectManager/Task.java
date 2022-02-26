package softwaredesign.projectManager;

import java.util.*;

public class Task {
    private Status status;
    private String name;
    private static final UUID uuid = UUID.randomUUID();
    private Map<Employee, QualStatus> assignedEmployees;
    private List<Skill> requiredSkills;
    private TaskList dependentTasks;
    private double timeSpent;
    private boolean taskFinished;

    //Used mostly to update a variable within the class, or create an instance from scratch.
    public Task (String name, Map<Employee, QualStatus> assignedEmployees, Status status, List<Skill> requiredSkills, double timeSpent, TaskList dependentTasks) {
        this.name = name;
        this.assignedEmployees = assignedEmployees;
        this.status = status;
        this.requiredSkills = requiredSkills;
        this.timeSpent = timeSpent;
        this.dependentTasks = dependentTasks;
        this.taskFinished = false;
    }

    public Task (String name, double estimatedTime, List<Skill> requiredSkills) {
        this.name = name;
        this.assignedEmployees = new HashMap<>();
        this.status = decideStatus();
        this.requiredSkills = requiredSkills;
        this.timeSpent = 0d;
        this.dependentTasks = new TaskList("Dependent Tasks");
        this.taskFinished = false;
    }

    public Task (Task task) {
        this.name = task.name;
        this.assignedEmployees = task.assignedEmployees;
        this.status = task.status;
        this.requiredSkills = task.requiredSkills;
        this.timeSpent = task.timeSpent;
        this.dependentTasks = task.dependentTasks;
        this.taskFinished = false;
    }

    public String getTaskName () {
        return String.copyValueOf(this.name.toCharArray());
    }

    //setAssignedEmployee instead of assign employee
    public void setAssignedEmployees (Map<Employee, QualStatus> assignedEmployees) {
        this.assignedEmployees = new HashMap<>();
        assignedEmployees.forEach((x,y)-> this.assignedEmployees.put(new Employee(x), y));
    }

    public List<Employee> getAssignedEmployees () {
        return new ArrayList<>(this.assignedEmployees.keySet());
    }

    public void assignEmployeeToTask (Employee employee) {
        employee = new Employee(employee);
        if (isAssigned(employee)) {
            System.err.println("Employee already assigned. ");
        }
        else {
            if (isQualified(employee)) {
                assignedEmployees.put(employee, QualStatus.QUALIFIED);
            }
            else {
                System.err.println(employee.getName() + " is not qualified to perform this task." + employee.getName() + " not assigned to task");
                assignedEmployees.put(employee, QualStatus.UNQUALIFIED);
            }
        }
    }

    //method to replace any of the given lists? like to replace the assigned employee list
    public List<Skill> getRequiredSkills () { return Collections.unmodifiableList(this.requiredSkills);}

    public void addRequiredSkill (Skill skill) {
        skill = new Skill(skill);
        this.requiredSkills.add(skill);
    }

    public void setStatus (Status.Progress progress) {
        //Could add switch statement here, but since it's just two cases, this suffices.
        if (decideStatus().getProgress() == progress) this.status = new Status(progress);
        else {
            System.err.println("Status not changed, requirements not met.");
        }
    }

    public Status getStatus () {
        return new Status(this.status);
    }

    public void printStatus() {
        this.status.printStatus();
    }

    public double getTimeSpent() {return new Double(timeSpent);}

    public void setTimeSpent(double timeSpent) {
        this.timeSpent = Double.valueOf(timeSpent);
    }

    public List<Task> getDependentTasks () {
        return Collections.unmodifiableList(this.dependentTasks.getTaskList());
    }

    public void setDependentTasks (List<Task> dependentTasks) {
        this.dependentTasks.setTaskList(dependentTasks);
    }

    public UUID getUuid() {
        return uuid;
    }

    private boolean taskAssigned () {
        return this.assignedEmployees.size() >= 1;
    }

    private boolean isAssigned (Employee employee) {
        for (Employee currentEmployee: assignedEmployees.keySet()) {
            if(currentEmployee.getUuid().equals(employee.getUuid())) return true;
        }
        return false;
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

    private boolean taskFinished() {return (taskExecuting() && !dependentTasksRemain());}

    private boolean taskOnHold() {return (taskExecuting() && dependentTasksRemain());}

    private boolean taskExecuting() { return (taskAssigned() && started()); }

    private Status decideStatus () {
        if (taskFinished()) return new Status(Status.Progress.FINISHED);
        else if (taskOnHold()) return new Status(Status.Progress.ONHOLD);
        else if (taskExecuting()) return new Status(Status.Progress.EXECUTING);
        else if (taskAssigned()) return new Status(Status.Progress.READY);
        else return new Status(Status.Progress.CREATED);
    }
}
