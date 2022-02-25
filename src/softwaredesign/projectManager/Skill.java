package softwaredesign.projectManager;

final public class Skill {
    //Be sure to mention the difference between this and status. (particularly the enumeration)
    private String name;

    public Skill(String name) {
        this.name = name;
    }

    public Skill (Skill skill) {
        this.name = skill.getName();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newSkill) {this.name = String.copyValueOf(newSkill.toCharArray());}
}