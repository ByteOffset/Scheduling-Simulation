/**
 * Created by Preston Peterson on 10/16/2015.
 */
public class Job implements Comparable<Job> {

    private int jobNo;
    private int priority;
    private int createdAtTime;
    private int timeLeft;
    private static int jobsCreated = 0;

    /**
     * Constructor
     * @param priority the integer value of the priority of the job (1-5)
     * @param timeCreated the integer value of the time this job was created
     * @param timeLeft the integer value of the time left to complete this job
     */
    public Job(int priority, int timeCreated, int timeLeft) {
        this.priority = priority;
        this.createdAtTime = timeCreated;
        this.timeLeft = timeLeft;
        jobsCreated++;
        this.jobNo = jobsCreated;
    }

    /**
     * @param other the Job with which to compare this job to
     * @return positive if this job has lower priority than the other
     */
    public int compareTo(Job other) { //compares priority
        //return this.priority - other.priority;
        int value;
        if (this.priority - other.priority == 0)
            value = 0;
        else if (this.priority > other.priority)
            value = 1;
        else
            value = -1;
        return value;
    }

    /**
     * @return the amount of time left to complete this job
     */
    public int getTimeLeft() {
        return this.timeLeft;
    }

    /**
     * @return the number of this job
     */
    public int getJobNo() {
        return this.jobNo;
    }

    /**
     * @param timeLeft the new amount of time left to complete this job
     */
    public void update(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * @return true if the job has 0 time left to complete
     */
    public boolean isFinished() {
        boolean finished = false;
        if (timeLeft == 0)
            finished = true;
        return finished;
    }

    /**
     * @return a string displaying the job number, priority, and time created
     */
    public String toString() {
        return "Job #" + this.jobNo + " priority(" + priority +
                ") created at " + this.createdAtTime + ", time left " + this.timeLeft;
    }







}
