package softwaredesign.projectManager;

import java.util.*;

//Each task has to be assigned to atleast one employee. Map is a good option here.
public class TaskList {
    //can use map to with uuid .

    private String name;

    //Changed from list to task
    private Map<UUID, Task> tasks;

    private UUID uuid;

    public TaskList(String name, Map<UUID, Task> tasks) {
        this.name = name;
        this.tasks = tasks;
        this.uuid = UUID.randomUUID();
    }

    public TaskList(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.tasks = new HashMap<>();
    }

    public TaskList(TaskList taskList) {
        this.name = taskList.name;
        this.uuid = taskList.uuid;
        this.tasks = taskList.tasks;
    }

    public void addTask(Task task) {
        task = new Task(task);
        tasks.put(task.getUuid(), task);
    }

    public String getName() {
        return String.copyValueOf(this.name.toCharArray());
    }

    public boolean contains(UUID taskId) {
        return this.tasks.containsKey(taskId);
    }

    public void replaceTask(UUID oldTaskId, Task newTask) {
        if (tasks.containsKey(oldTaskId)) {
            newTask = new Task(newTask);
            tasks.remove(oldTaskId);
            tasks.put(newTask.getUuid(), newTask);
        }
    }

    public void removeTask(UUID oldTaskId) {
        if (tasks.containsKey(oldTaskId)) {
            tasks.remove(oldTaskId);
        }
        else {System.err.println("Task not found");}
    }

    public List<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    public void setTaskList (List<Task> tasks) {
        this.tasks = new HashMap<>();
        for (Task currentTask: tasks){
            currentTask = new Task(currentTask);
            this.tasks.put(currentTask.getUuid(), currentTask);
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
