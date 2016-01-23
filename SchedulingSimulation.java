import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Preston Peterson on 10/16/2015.
 */
public class SchedulingSimulation {

    private PriorityQueue<Job> waitingJobs;
    private LinkedBlockingDeque<Job> allJobs;
    private Job currentJob;
    private int priorityMode;
    private final int TIME_SLICE_PER_JOB = 3;
    //Jobs execute for a minimum of 3 seconds regardless of their priority, unless they require less than 3 seconds to complete.
    private final int SIMULATION_DURATION = 100;
    private final int JOB_PROBABILITY = 30;
    private final int JOB_PRIORITY = 4;
    private final int JOB_MIN_TIME = 1;
    private final int JOB_MAX_TIME = 5;
    private static final int SORT_BY_PRIORITY = 0;
    private static final int SORT_BY_LENGTH = 1;

    /**
     * Default constructor calls secondary constructor and initializes priority mode to 0.
     */
    public SchedulingSimulation() {
        this(0);
    }

    /**
     * Secondary constructor
     * @param sortBy 0 indicates priority sorting. 1 indicates length sorting
     */
    public SchedulingSimulation(int sortBy) {
        System.out.println("**************  SCHEDULING SIMULATION  **************");
        this.priorityMode = sortBy;
        this.waitingJobs = new PriorityQueue<>(10, new Comparator<Job>() {
            //@Override
            public int compare(Job job1, Job job2) { //compares length
                int value;
                if (priorityMode == SORT_BY_PRIORITY) {
                    value = job1.compareTo(job2);
                    //returns positive if job1 has higher priority than job2
                    //returns negative if job1 has lower priority than job2
                }
                else {
                    value = job1.getTimeLeft() - job2.getTimeLeft();
                    //returns positive if job1 has more time than job2
                    //returns negative if job1 has less time than job2
                }
                return value;
            }
        }
        );
        allJobs = new LinkedBlockingDeque<>();
        this.currentJob = null;
        System.out.print("***STARTING THE SIMULATION WITH PRIORITY MODE SET TO ");
        System.out.println(sortBy == 0 ? "SORT_BY_PRIORITY***" : "SORT_BY_LENGTH***");
        runSimulation();
    }

    /**
     * Runs the simulation for a length of time equivalent to SIMULATION_DURATION.
     * Utilizes all methods in the SchedulingSimulation class.
     */
    public void runSimulation() {
        int timeSlice = TIME_SLICE_PER_JOB;
        String completed = "";
        boolean finished;
        for (int clock = 0; clock < SIMULATION_DURATION; clock++) {
            finished = false;
            if (timeSlice == 0) {//reset timeSlice once it hits 0
                timeSlice = TIME_SLICE_PER_JOB;
            }
            if (!waitingJobs.isEmpty() && timeSlice == TIME_SLICE_PER_JOB) { //if timeSlice has been reset select the next job with highest priority
                currentJob = waitingJobs.peek(); //get highest priority job
            }
            currentReport(clock);
            if (currentJob != null) {
                currentJob.update(currentJob.getTimeLeft() - 1);
                if (currentJob.isFinished()) {
                    finished = true;
                    completed = "\tcompleted: Job #" + currentJob.getJobNo() + " at time " + clock;
                    waitingJobs.remove(currentJob);
                    timeSlice = TIME_SLICE_PER_JOB;
                    currentJob = null;
                }
            }
            generateJob(clock);
            if (finished)
                System.out.println(completed);
            if (currentJob != null)
                timeSlice--;
        }
        finalReport();
    }

    /**
     * Has a random chance to generate a job at the given clock time.
     * If a job is generated, adds it to the waitingJobs queue and the allJobs deque.
     * @param clock the current time value of the simulation.
     */
    public void generateJob(int clock) {
        //Jobs are generated at random times.
        //Each job is given:
        //  a random priority from 1 to 4, where 1 is the highest priority
        //  a random amount of time to complete its execution.
        Random generator = new Random();
        Job newJob;
        int jobTime;
        if (generator.nextInt(100)+1 <= JOB_PROBABILITY) {
            int priority = generator.nextInt(JOB_PRIORITY)+1;
            jobTime = generator.nextInt(JOB_MAX_TIME)+JOB_MIN_TIME;
            newJob = new Job(priority,clock,jobTime);
            System.out.print("\tcreated: ");
            waitingJobs.add(newJob);
            allJobs.add(newJob);
            System.out.println(newJob);
        }
    }

    /**
     * Outputs the current time value, the number of jobs waiting,
     * and the job currently being processed if there is one.
     * @param clock the current time value of the simulation.
     */
    public void currentReport(int clock) {
        System.out.println("Time Marker " + clock + "  waiting: " + waitingJobs.size());
        System.out.print("\texecuting: ");
        if (waitingJobs.size() == 0 || this.currentJob == null)
            System.out.println("NONE");
        else
            System.out.println(currentJob);
    }

    /**
     * Outputs the final report of the simulation, including number of jobs in the jobsWaiting queue,
     * the average time left to complete them, the number of jobs completed, and the total number of jobs.
     */
    public void finalReport() {
        DecimalFormat format = new DecimalFormat("#0.00");
        System.out.println("**************  Final Report:  **************");
        System.out.println("\tActive jobs:");
        int totalTime = 0;
        for (Job job : waitingJobs) {
            totalTime += job.getTimeLeft();
            System.out.println(job);
        }
        double avgTime = (double) totalTime / waitingJobs.size();
        System.out.println("The number of jobs currently executing is " + waitingJobs.size());
        System.out.println("The number of completed jobs is " + (allJobs.size() - waitingJobs.size()));
        System.out.println("The total number of jobs is " + allJobs.size());
        System.out.println("The average time left for unfinished jobs is " + format.format(avgTime));
        System.out.println("\n***END OF SIMULATION***");
    }


    public static void main(String[] args) {
        SchedulingSimulation simulator = new SchedulingSimulation(1);
    }

}