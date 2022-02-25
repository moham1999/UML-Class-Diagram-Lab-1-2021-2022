package softwaredesign.projectManager;

import java.util.*;

public class Project {
    //Use "this" for creating new instance of class in the parameters

    private String name;
    private UUID uuid;
    private Status status;
    private double timeSpent;
    private double availableFunds;

    private List<TaskList> taskLists;
    private List<Employee> allEmployees;

    public Project(String name, List<TaskList> taskLists, List<Employee> employees, double availableFunds) {
        this.name = name;
        this.taskLists = taskLists;
        this.allEmployees = employees;
        this.uuid = UUID.randomUUID();
        this.status = decideStatus();
        this.timeSpent = updateTimeSpent();
        this.availableFunds = availableFunds;
    }

    public Project(String name, List<TaskList> taskLists, List<Employee> employees, Status status, double availableFunds) {
        this.name = name;
        this.taskLists = taskLists;
        this.allEmployees = employees;
        this.uuid = UUID.randomUUID();
        this.status = status;
        this.timeSpent = updateTimeSpent();
        this.availableFunds = availableFunds;
    }

    public Project (Project project) {
        this.name = project.name;
        this.taskLists = project.taskLists;
        this.allEmployees = project.allEmployees;
        this.uuid = project.uuid;
        this.status = project.status;
        this.timeSpent = project.timeSpent;
        this.availableFunds = project.availableFunds;
    }

    public String getName() {
        return String.copyValueOf(this.name.toCharArray());
    }

    public void setName(String name) {
        this.name = String.copyValueOf(name.toCharArray());
    }

    public Status getStatus() {return new Status(this.status);}

    private double updateTimeSpent() {
        double newTime = 0d;
        for (TaskList currentTaskList : taskLists){
            for (Task task : currentTaskList.getTaskList()) {
                newTime += task.getTimeSpent();
            }
        }
        return newTime;
    }


    public void setStatus(Status status) {
        if (status.getProgress() != decideStatus().getProgress()) {
            //Introduce a switch statement here to see what the status is and why it might have failed for error handling.
            System.err.println("Status not changed, minimum requirements for the desired status not met.");
            return;
        }
        this.status = new Status(status);
    }

    public List<TaskList> getTaskLists() {
        return Collections.unmodifiableList(this.taskLists);
    }

    public void addTaskList(TaskList taskList) {
        taskList = new TaskList(taskList);
        taskLists.add(taskList);
    }

    public void addWorker(Employee employee) {
        for(Employee currentEmployee: this.allEmployees) {
            if (currentEmployee.is(employee.getUuid())) {
                System.err.println("Employee already ");
            }
        }
    }

    public void updateEmployees () {
        this.taskLists.forEach(x-> x.getTaskList().forEach(y-> addEmployees(y.getAssignedEmployees())));
    }
    private void addEmployees (List<Employee> employees) {
        if (!this.allEmployees.containsAll(employees)) {
            for (Employee currentEmployee : employees) {
                if (!this.allEmployees.contains(currentEmployee)) this.allEmployees.add(currentEmployee);
            }
        }
    }

    public void replaceTaskList(TaskList oldTaskList, TaskList newTaskList) {
        int index = 0;
        for (TaskList currentTL : this.taskLists) {
            if (currentTL == oldTaskList) {
                newTaskList = new TaskList(newTaskList);
                this.taskLists.remove(oldTaskList);
                this.taskLists.add(index, newTaskList);
            }
            //Use try catch here
            else {
                System.err.println("Task list not found. Task list not replaced.");
                return;
            }
        }
    }


    public void moveTask(Task task, TaskList previousTaskList, TaskList currentTaskList) {
        task = new Task(task);
        previousTaskList.removeTask(task.getUuid());
        currentTaskList.addTask(task);
        replaceTaskList(previousTaskList, previousTaskList);
    }

    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(this.allEmployees);
    }

    public UUID getUUID () {
        return this.uuid;
    }

    private Status decideStatus () {
        if (!tasksAssigned() && isStarted() && allTasksFinished()) {
            return new Status(Status.Progress.FINISHED);
        }
        else if (!tasksAssigned() && isStarted() && !fundsAvailable()){
            return new Status(Status.Progress.ONHOLD);
        }
        else if (!tasksAssigned() && isStarted() && fundsAvailable()){
            return new Status(Status.Progress.EXECUTING);
        }
        else if (!tasksAssigned() && !isStarted()) {
            return new Status(Status.Progress.READY);
        }
        else return new Status(Status.Progress.CREATED);
        //Think of how to do on hold
    }

    private boolean tasksAssigned () {return taskLists.isEmpty();}

    private boolean isStarted () {
        return timeSpent > 0;
    }

    private boolean fundsAvailable () {
        return this.availableFunds > 0;
    }

    private boolean allTasksFinished () {
        for (TaskList currentTaskList : taskLists) {
            for (Task task : currentTaskList.getTaskList()) {
                if (task.getStatus().getProgress() != Status.Progress.FINISHED) return false;
            }
        }
        return true;
    }
}
