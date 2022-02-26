package softwaredesign.projectManager;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class test {
    //Think where to fit for polymorphism

    String[] skillNames = {"Programming", "Driving", "Cooking", "Music"};
    String[] skillsTestSet = {"Blockchain, Cloud computing, Analytical Reasoning"};
    String[] employeeNames = {"Bobby", "Sammy", "Pink man"};
    String[] tasks = {"Wash car", "Cook food", "Eat", "Sleep", "Build a rocket", "Get a million dollars", "Do a moon dance"};

    //Use string array to create and add skills to an arrayList.
    public List<Skill> createSkillList (String[] skillNames) {
        System.out.println("Creating skills");
        List<Skill> skills = new ArrayList<>();
        for (String skill : skillNames) {
            skills.add(new Skill(skill));
        }
        return skills;
    }

    public Map<Employee, QualStatus> createEmployeeMap (List<Skill> skills, String @NotNull [] employeeNames) {
        System.out.println("Creating Employee list");
        Map<Employee, QualStatus> employeesMap = new HashMap<>();
        for (String currentName : employeeNames) {
            employeesMap.put(new Employee(currentName, 0d,  skills), QualStatus.QUALIFIED);
        }
        return employeesMap;
    }

    public List<Task> createTasks ( Map<Employee, QualStatus> employees, String[] tasksNames, List<Skill> skills) {
        System.out.println("Creating tasks");
        List<Task> tasksList = new ArrayList<>();

        TaskList dependentTasks = new TaskList("Dependent Tasks");

        for (String currentTaskName : tasksNames) {
            tasksList.add(new Task(currentTaskName, employees, new Status(Status.Progress.CREATED), skills, 0d, dependentTasks));
        }
        return tasksList;
    }

    List<Skill> skills = new ArrayList<>(createSkillList(skillNames));
    Map<Employee, QualStatus> employees = new HashMap<>(createEmployeeMap(skills, employeeNames));
    List<Employee> employeeList = new ArrayList<>(employees.keySet());
    List<Task> tasksList = new ArrayList<>(createTasks(employees, tasks, skills));
    List<Skill> testSkillSet = new ArrayList<>(createSkillList(skillsTestSet));


   @Test
   public void printAllEmployees() {
       System.out.println("\nPrinting all employees\n");
       for (Employee currentEmployee : employees.keySet()) {
           currentEmployee.print();
       }
   }

   @Test
   public void testEmployee() {
       System.out.println("\nTesting Employees class.\n");
       Employee testSubject = employeeList.get(0);
       System.out.println("Current details of test subject:");
       testSubject.print();
       System.out.println("Changing " + testSubject.getName() + " name....");
       testSubject.setName("Shelby");
       System.out.println("Changing " + testSubject.getName() + " skills....");
       testSubject.setSkills(testSkillSet);
       System.out.println("Changing " + testSubject.getName() + " worked hours....");
       testSubject.setHours(20d);
       System.out.println("\n Details of test subject after changes: \n");
       testSubject.print();
   }

   public void testAllEmployeeReferences (List<Employee> employeeList, List<Employee> employeeList0) {
       for (int i = 0; i < employeeList.size(); i++) {
           if (employeeList.get(i) == employeeList0.get(i)) {
               System.err.println(employeeList.get(i).getName() + "reference is the same. Check for escaping references for employees");
           }
       }
       System.err.println("No escaping references found");
   }

   @Test
   public void testTask() {
       System.out.println("\nTesting Task class.\n");
       Task testSubject = tasksList.get(0);
       System.out.println("Changing status....");
       testSubject.setStatus(Status.Progress.FINISHED);
       System.out.println("Adding employee....");
       testSubject.assignEmployeeToTask(employeeList.get(0));
       List<Employee> employeeCopy = testSubject.getAssignedEmployees();
       testSubject.setAssignedEmployees(employees);

       System.out.println("\nTesting(shallow) wether any employee has the same reference after using setAssignedEmployees.");
       List<Employee> employeeCopy2 = testSubject.getAssignedEmployees();
       testAllEmployeeReferences(employeeCopy, employeeCopy2);

   }

   @Test
    public void printList () {
       for (Skill currentSkill: skills){
           System.out.println(currentSkill.getName());
       }
   }

}