package softwaredesign.projectManager;


public class Status {
    //Enumeration of all the status --> DONE
    //Changes made to task as a result. Check there for relevant changes
    private final Progress currentStatus;

    //To change status of Progress, you can only choose so from the enumeration below.
    enum Progress {
        CREATED{
            @Override
            public String toString () {
                return "Created, not ready yet.";
            }
        },

        READY {
            @Override
            public String toString() {
                return "Ready to start";
            }
        },

        EXECUTING {
            @Override
            public String toString() {
                return "In progress";
            }
        },

        FINISHED {
            @Override
            public String toString() {
                return "Finished!";
            }
        },

        ONHOLD {
            @Override
            public String toString() {
                return "Put on hold.";
            }
        }
    }

    public Status(Progress currentStatus) {
        this.currentStatus = currentStatus;
        System.out.println(currentStatus.toString());
    }
    public Status (Status status) {
        this.currentStatus = status.currentStatus;
    }

    public Status () {
        this.currentStatus = Progress.CREATED;
    }

    public Progress getProgress () {return this.currentStatus;}

    public void printStatus () {
        System.out.println(this.currentStatus);
    }
}
