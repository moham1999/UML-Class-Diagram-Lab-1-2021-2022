package softwaredesign.projectManager;

import java.util.*;

public class Project {
    //Use "this" for creating new instance of class in the parameters

    private final String name;
    private final UUID uuid;
    private final Status status;

    private final List<TaskList> taskLists;
    private final List<Employee> employees;

    public Project(String name, List<TaskList> taskLists, List<Employee> employees) {
        this.name = name;
        this.taskLists = taskLists;
        this.employees = employees;
        this.uuid = UUID.randomUUID();
        this.status = decideStatus();
    }

    public Project(String name, List<TaskList> taskLists, List<Employee> employees, Status status) {
        this.name = name;
        this.taskLists = taskLists;
        this.employees = employees;
        this.uuid = UUID.randomUUID();
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        return new Project(name, this.taskLists, this.employees);
    }

    public Status getStatus() {return this.status;}

    public Project setStatus(Status status) {
        //Could add switch statement here, but since it's just two cases, this suffices.
        if (status.getProgress() != Status.Progress.CREATED && !tasksAssigned()) {
            System.err.println("No tasks assigned to the project. Status not changed");
            return this;
        }
        else if (status.getProgress() == Status.Progress.EXECUTING && !isStarted()) {
            System.err.println("No amount of hours worked on any of the tasks. Status not changed");
            return this;
        }

        return new Project(this.name, this.taskLists, this.employees, status);
    }

    public List<TaskList> getTaskLists() {
        return this.taskLists;
    }

    public Project addTaskList(TaskList taskList) {
        List<TaskList> copiedTaskList = new ArrayList<>(taskLists);
        copiedTaskList.add(taskList);
        return new Project(this.name, copiedTaskList, this.employees);
    }

    public Project addWorker(Employee employee) {
        List<Employee> copiedEmployeeList = new ArrayList<>(employees);
        copiedEmployeeList.add(employee);
        return new Project(this.name, this.taskLists, copiedEmployeeList);
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
        return new Project(this.name, copiedTaskLists, this.employees);
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
        if (taskLists.isEmpty() && employees.isEmpty()) {
            return new Status(Status.Progress.CREATED);
        }
        else return new Status(Status.Progress.CREATED);
    }

    private boolean tasksAssigned () {return taskLists.isEmpty();}

    private boolean isStarted () {
        for (TaskList currentTL: taskLists){
            for (Task currentTask : currentTL.getTaskList()){
                if (currentTask.started()) return true;
            }
        }
        return false;
    }
}
