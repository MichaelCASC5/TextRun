import java.util.ArrayList;
public class Group{
        
    ArrayList<Coordinate> list = new ArrayList<>();
    
    boolean up = false;
    
    public Group(String str){
        for(int i=0;i<str.length();i++){
            int comma;
            str = str + ",";
            
            comma = str.indexOf(",");
            String strNum1 = str.substring(0,comma);
            str = str.substring(comma+1,str.length());
            
            comma = str.indexOf(",");
            String strNum2 = str.substring(0,comma);
            str = str.substring(comma+1,str.length());
            
            int num1 = Integer.parseInt(strNum1);
            int num2 = Integer.parseInt(strNum2);
            
            list.add(new Coordinate(num1, num2, true));
        }
    }
    public void addX(int a){
        for(int i=0;i<list.size();i++){
            list.get(i).addX(a);
        }
    }
    public void addY(int a){
        for(int i=0;i<list.size();i++){
            list.get(i).addY(a);
        }
    }
    public boolean has(int a, int b){
        boolean output = false;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getX() == a && list.get(i).getY() == b){
                output = true;
            }
        }
        return output;
    }
    public void setUp(boolean a){
        up = a;
    }
    public boolean isUp(){
        return up;
    }
}