public class Timer{
    private long time;
    
    public void start(){
        time = System.currentTimeMillis();
    }
    public long getTime(){
        return System.currentTimeMillis() - time;
    }
}