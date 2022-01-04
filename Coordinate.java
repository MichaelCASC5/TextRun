public class Coordinate{
    private int x;
    private int y;
    private boolean display;
    
    private boolean isGift;
    private boolean isSkull;
    private boolean isSlow;
    
    private boolean isBoss;
    private boolean isBossObs;
    private boolean isBossBack;
    
    private boolean obstacle;
    private boolean obstacle1;
    private boolean obstacle2;
    
    public Coordinate(){
        x = 0;
        y = 0;
        display = false;
    }
    public Coordinate(int a, int b, boolean c){
        x = a;
        y = b;
        display = c;
    }
    public void setX(int a){
        x = a;
    }
    public void setY(int a){
        y = a;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void addX(int a){
        x = x + a;
    }
    public void addY(int a){
        y = y + a;
    }
    public void setDisplay(boolean a){
        display = a;
    }
    public String toString(){
        return "(" + x + ", " + y + ") D:" + display + " O:" + obstacle;
    }
    public boolean getDisplay(){
        return display;
    }
    public boolean isObstacle(){
        return obstacle;
    }
    public void setObstacle(boolean a){
        if(a){
            obstacle1 = false;
            obstacle2 = false;
        }
        obstacle = a;
    }
    public boolean isObstacle1(){
        return obstacle1;
    }
    public void setObstacle1(boolean a){
        if(a){
            obstacle = false;
            obstacle2 = false;
        }
        obstacle1 = a;
    }
    public boolean isObstacle2(){
        return obstacle2;
    }
    public void setObstacle2(boolean a){
        if(a){
            obstacle = false;
            obstacle1 = false;
        }
        obstacle2 = a;
    }
    public boolean isGift(){
        return isGift;
    }
    public void setGift(boolean a){
        isGift = a;
    }
    public boolean isSkull(){
        return isSkull;
    }
    public void setSkull(boolean a){
        isSkull = a;
    }
    public boolean isSlow(){
        return isSlow;
    }
    public void setSlow(boolean a){
        isSlow = a;
    }
    public boolean isBoss(){
        return isBoss;
    }
    public void setBoss(boolean a){
        isBoss = a;
    }
    public boolean isBossObs(){
        return isBossObs;
    }
    public void setBossObs(boolean a){
        isBossObs = a;
    }
    public boolean isBossBack(){
        return isBossBack;
    }
    public void setBossBack(boolean a){
        isBossBack = a;
    }
}