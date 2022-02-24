package softwaredesign.projectManager;

import java.util.*;

public class Project {
    //Use "this" for creating new instance of class in the parameters

    private final String name;
    private final UUID uuid;
    private final Status status;
    private final double timeSpent;
    private final double availableFunds;

    private final List<TaskList> taskLists;
    private final List<Employee> employees;

    public Project(String name, List<TaskList> taskLists, List<Employee> employees, double availableFunds) {
        this.name = name;
        this.taskLists = taskLists;
        this.employees = employees;
        this.uuid = UUID.randomUUID();
        this.status = decideStatus();
        this.timeSpent = updateTimeSpent();
        this.availableFunds = availableFunds;
    }

    public Project(String name, List<TaskList> taskLists, List<Employee> employees, Status status, double availableFunds) {
        this.name = name;
        this.taskLists = taskLists;
        this.employees = employees;
        this.uuid = UUID.randomUUID();
        this.status = status;
        this.timeSpent = updateTimeSpent();
        this.availableFunds = availableFunds;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        return new Project(name, this.taskLists, this.employees, this.availableFunds);
    }

    public Status getStatus() {return this.status;}

    private double updateTimeSpent() {
        double newTime = 0d;
        for (TaskList currentTaskList : taskLists){
            for (Task task : currentTaskList.getTaskList()) {
                newTime += task.getTimeSpent();
            }
        }
        return newTime;
    }


    public Project setStatus(Status status) {
        if (status.getProgress() != decideStatus().getProgress()) {
            //Introduce a switch statement here to see what the status is and why it might have failed for error handling.
            System.err.println("Status not changed, minimum requirements for the desired status not met.");
            return this;
        }
        return new Project(this.name, this.taskLists, this.employees, status, this.availableFunds);
    }

    public List<TaskList> getTaskLists() {
        return this.taskLists;
    }

    public Project addTaskList(TaskList taskList) {
        List<TaskList> copiedTaskList = new ArrayList<>(taskLists);
        copiedTaskList.add(taskList);
        return new Project(this.name, copiedTaskList, this.employees, this.availableFunds);
    }

    public Project addWorker(Employee employee) {
        List<Employee> copiedEmployeeList = new ArrayList<>(employees);
        copiedEmployeeList.add(employee);
        return new Project(this.name, this.taskLists, copiedEmployeeList, this.availableFunds);
    }

    public Project replaceTaskList(TaskList oldTaskList, TaskList newTaskList) {
        int index = 0;
        List<TaskList> copiedTaskLists = this.taskLists;
        for (TaskList currentTL : copiedTaskLists) {
            if (currentTL == oldTaskList) {
                copiedTaskLists.remove(oldTaskList);
                copiedTaskLists.add(index, newTaskList);
            }
            //Use try catch here
            else {
                System.err.println("Task list not found. Task list not replaced.");
                return this;
            }
        }
        return new Project(this.name, copiedTaskLists, this.employees, this.availableFunds);
    }


    public Project moveTask(Task task, TaskList previousTaskList, TaskList currentTaskList) {
        TaskList oldTaskList = previousTaskList.removeTask(task.getUuid());
        TaskList newTaskList = currentTaskList.addTask(task);
        return replaceTaskList(previousTaskList, oldTaskList).replaceTaskList(currentTaskList, newTaskList);
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }

    public UUID getUUID () {
        return this.uuid;
    }

    private Status decideStatus () {
        if (tasksAssigned() && isStarted() && allTasksFinished()) {
            return new Status(Status.Progress.FINISHED);
        }
        else if (tasksAssigned() && isStarted() && !fundsAvailable() && !allTasksFinished()){
            return new Status(Status.Progress.ONHOLD);
        }
        else if (tasksAssigned() && isStarted() && fundsAvailable() && !allTasksFinished()){
            return new Status(Status.Progress.EXECUTING);
        }
        else if (tasksAssigned() && !isStarted()) {
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
