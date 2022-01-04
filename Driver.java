import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.util.Scanner;
import java.util.ArrayList;

public class Driver extends JComponent implements KeyListener{
    private int WIDTH;
    private int HEIGHT;
    
    private int xRes = 30;
    private int yRes = 2;
    
    private int lives = 3;
    private int giftNum = 1;
    
    private double score = 0;
    
    private boolean loop = true;
    private boolean animate = true;
    private boolean help = false;
    
    private boolean slowDown = false;
    
    private boolean clean = false;
    
    private int obsType = 0;
    private int obsPos;
    private int topChance;
    
    private int top = 0;
        
    private boolean autopilot = false;
    private boolean cheats = true;
    
    private boolean boss = false;
    private int bossLife = 10;
    private int facial = 0;
    
    private int hearts = 8;//FOR BOSS REWARD SPAM
    
    double scoreKeep = 0;
    private int slowLast = 20;
    
    Group player = new Group("2,1");
    
    public Driver(){//INPUT PAD CODE
        WIDTH = 250;
        HEIGHT = 250;

        JFrame gui = new JFrame();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setTitle("Input Pad");
        gui.setPreferredSize(new Dimension(WIDTH + 5, HEIGHT + 30));
        gui.getContentPane().add(this);
        gui.pack();
        gui.setVisible(true);
        gui.addKeyListener(this);
    }
    public void keyPressed(KeyEvent e){
//        System.out.println(e.getKeyCode());
        if(e.getKeyCode() == 32){//SPACEBAR
            if(player.isUp() && animate){
                player.setUp(false);
            }else if(!player.isUp() && animate){
                player.setUp(true);
            }
        }else if(e.getKeyCode() == 82){//R
            animate = true;
            lives = 3;
            clean = true;
            score = 0;
        }else if(e.getKeyCode() == 80 && lives > 0){//P
            if(animate == true){
                animate = false;
            }else{
                animate = true;
            }
        }else if(e.getKeyCode() == 69){//E
            System.exit(0);
        }else if(e.getKeyCode() == 72){//H
            if(help == false){
                help = true;
                animate = false;
            }else{
                help = false;
            }
        }else if(e.getKeyCode() == 65 && cheats){//A
            if(autopilot == false){
                autopilot = true;
            }else{
                autopilot = false;
            }
        }else if(e.getKeyCode() == 66 & cheats){//B
            if(boss){
                boss = false;
                bossLife = 0;
            }else{
                boss = true;
                bossLife = 10;
            }
        }
    }
    public void loop(){
        Timer delay = new Timer();
        Timer timer = new Timer();
        Timer hurt = new Timer();
        Timer bossDelay = new Timer();
                
        Timer obsType1Timer = new Timer();
        Timer obsType1TimerBreak = new Timer();
        
        Timer restartGifts = new Timer();
        
        Scanner reader = new Scanner(System.in);
        int total = xRes * yRes;
        
        //GENERATING COORDINATES
        ArrayList<Coordinate> grid = new ArrayList<>();
        for(int x=0;x<xRes;x++){
            for(int y=0;y<yRes;y++){
                grid.add(new Coordinate(x, y, false));
            }
        }
        
        boolean gameRun = true;
        delay.start();
        timer.start();
        hurt.start();
        obsType1Timer.start();
        while(gameRun){
            //SETTING DELAY
            while(!slowDown && delay.getTime() < 70){}//70 NORMAL
            while(slowDown && delay.getTime() < 250){}//500 MOLASSES
            if(score - scoreKeep > slowLast){
                slowDown = false;
            }
            delay.start();
            
            //CLEANER
            if(clean){
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setObstacle(false);
                }
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setObstacle1(false);
                }
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setObstacle2(false);
                }
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setGift(false);
                }
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setSkull(false);
                }
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setBoss(false);
                }
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setBossObs(false);
                }
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setBossBack(false);
                }
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setSlow(false);
                }
                player.setUp(false);
                boss = false;
                clean = false;
                slowDown = false;
            }
            
            //SELECTING OBSTACLE TYPE
            int selectChance = (int)(Math.random()*1000);
            if(selectChance < 20){
                obsType = 0;
            }else if(selectChance >= 20 && selectChance < 30 && !slowDown){//Prevent obsType to spawn if slowDown
                obsType = 1;
            }else if(selectChance >=30 && selectChance < 40){
                obsType = 2;
            }
            
            //CREATING BOSS
            if(score != 0 && score %500 == 0 && !boss){
                grid.get(xRes-1).setBoss(true);
                boss = true;
                bossLife = 10;
                bossDelay.start();
                slowDown = false;
            }
            if(bossLife == 0){
                boss = false;
                for(int i=0;i<grid.size();i++){
                    grid.get(i).setBoss(false);
                }
                bossLife = 10;
                hearts = 500;
                obsType = 1000;//Clears it temporarly after boss dies?
                restartGifts.start();
            }
            if(restartGifts.getTime() <= 2000){
                obsType = 1000;
            }
            if(restartGifts.getTime() > 2000){
                hearts = 8;
            }
            
            //ANIMATING BOSS
            if(boss && animate && bossDelay.getTime() > 3000){
                if((int)(Math.random()*100) < 20){
                    if(grid.get(xRes-1).isBoss() == true){
                        grid.get(xRes-1).setBoss(false);
                        grid.get((xRes*2)-1).setBoss(true);
                    }else if(grid.get(xRes-1).isBoss() == false){
                        grid.get(xRes-1).setBoss(true);
                        grid.get((xRes*2)-1).setBoss(false);
                    }
                }
                slowDown = false;//Prevents slow during boss
            }
            
            //CREATING BOSS OBSTACLES
            if(animate && boss && bossDelay.getTime() > 3000){
                top = (int)(Math.random()*2);
                if((int)(Math.random()*100)+1 < 13){
                    if(grid.get(xRes-1).isBoss() == true){
                        obsPos = (xRes * 0) + (xRes-1);
                        grid.get(obsPos).setBossObs(true);
                    }else if(grid.get(xRes-1).isBoss() == false){
                        obsPos = (xRes * 1) + (xRes-1);
                        grid.get(obsPos).setBossObs(true);
                    }
                }
            }
            //CREATING BOSS BACKS
            if(animate && boss && bossDelay.getTime() > 3000){
                top = (int)(Math.random()*2);
                if((int)(Math.random()*100)+1 < 5){
                    if(grid.get(xRes-1).isBoss() == true){
                        obsPos = (xRes * 0) + (xRes-1);
                        grid.get(obsPos).setBossBack(true);
                    }else if(grid.get(xRes-1).isBoss() == false){
                        obsPos = (xRes * 1) + (xRes-1);
                        grid.get(obsPos).setBossBack(true);
                    }
                }
            }
            
            //CREATING OBSTACLES TYPE 0
            if(animate && !boss && obsType == 0){
                top = (int)(Math.random()*2);
                if((int)(Math.random()*100)+1 < 10){
                    obsPos = (xRes * top) + (xRes-1);
                    grid.get(obsPos).setObstacle(true);
                }
            }

            //CREATING OBSTACLES TYPE 1
            if(animate &&!boss && obsType == 1){
                topChance = (int)(Math.random() * 100);
                if(topChance < 20 && obsType1Timer.getTime() > 500){
                    if(top == 0){
                        top = 1;
                        obsType1TimerBreak.start();
                    }else if(top == 1){
                        top = 0;
                        obsType1TimerBreak.start();
                    }
                    obsType1Timer.start();
                }
                obsPos = (xRes * top) + (xRes-1);
                if(obsType1TimerBreak.getTime() > 70){//GENERATES GAPS 50
                    grid.get(obsPos).setObstacle1(true);
                }
            }
            
            //CREATING OBSTACLE 2
            if(animate &&!boss && obsType == 2){
                top = (int)(Math.random()*2);
                if((int)(Math.random()*100)+1 < 10){
                    obsPos = (xRes * top) + (xRes-1);
                    grid.get(obsPos).setObstacle2(true);
                }
            }
            
            //CREATING GIFTS
            int giftPos;
            if(animate && !boss){
                int topG = (int)(Math.random()*2);
                if((int)(Math.random()*1000)+1 < hearts){//hearts (8)
                    giftPos = (xRes * topG) + (xRes-1);
                    grid.get(giftPos).setGift(true);
                    
                    grid.get(giftPos).setObstacle(false);//Prevents obstacle from spawning underneath
                    grid.get(giftPos).setObstacle1(false);
                }
            }
            
            //CREATING SKULLS
            int skullPos;
            if(animate && !boss && obsType != 1){
                int topG = (int)(Math.random()*2);
                if((int)(Math.random()*1000)+1 < 10){//10
                    skullPos = (xRes * topG) + (xRes-1);
                    grid.get(skullPos).setSkull(true);
                    
                    grid.get(skullPos).setObstacle(false);//Prevents obstacle from spawning underneath
                    grid.get(skullPos).setObstacle1(false);
                }
            }
            
            //CREATING SLOWS
            int slowPos;
            if(animate && !boss && obsType != 1){
                int topG = (int)(Math.random()*2);
                if((int)(Math.random()*1000)+1 < 8){//8
                    slowPos = (xRes * topG) + (xRes-1);
                    grid.get(slowPos).setSlow(true);
                    
                    grid.get(slowPos).setObstacle(false);//Prevents obstacle from spawning underneath
                    grid.get(slowPos).setObstacle1(false);
                }
            }
            
            //MAKING BUFFER
            for(int i=0;i<100;i++){
                System.out.println();
            }
            
            //CHARACTER
            int arrayPos;
            for(int x=0;x<xRes;x++){
                for(int y=0;y<yRes;y++){
                    if(player.isUp()){
                        //EQUATION TO CONVERT (X,Y) TO ARRAY POSITION: A = XrY + X
                        arrayPos = (xRes * 0) + 2;
                        grid.get(arrayPos).setDisplay(true);
                        arrayPos = (xRes * 1) + 2;
                        grid.get(arrayPos).setDisplay(false);
                    }else{
                        arrayPos = (xRes * 0) + 2;
                        grid.get(arrayPos).setDisplay(false);
                        arrayPos = (xRes * 1) + 2;
                        grid.get(arrayPos).setDisplay(true);
                    }
                }
            }
            
            //BUG FIXING
            for(int i=0;i<grid.size();i++){
                if(i < xRes && grid.get(i).isSkull() && (grid.get(i+xRes).isObstacle() || grid.get(i+xRes).isObstacle1() || grid.get(i+xRes).isObstacle2() || grid.get(i).isGift() || grid.get(i).isSlow())){
                    grid.get(i).setSkull(false);
                }else if(i > xRes-1 && grid.get(i).isSkull() && (grid.get(i-xRes).isObstacle() || grid.get(i-xRes).isObstacle1() || grid.get(i-xRes).isObstacle2() || grid.get(i).isGift() || grid.get(i).isSlow())){
                    grid.get(i).setSkull(false);
                }
                
                if(i < xRes && grid.get(i).isSlow() && (grid.get(i+xRes).isObstacle() || grid.get(i+xRes).isObstacle1() || grid.get(i+xRes).isObstacle2() || grid.get(i).isGift() || grid.get(i).isSkull())){
                    grid.get(i).setSlow(false);
                }else if(i > xRes-1 && grid.get(i).isSlow() && (grid.get(i-xRes).isObstacle() || grid.get(i-xRes).isObstacle1() || grid.get(i-xRes).isObstacle2() || grid.get(i).isGift() || grid.get(i).isSkull())){
                    grid.get(i).setSlow(false);
                }
            }
            
            //LifeBar and Score
            System.out.print("Lives: " + lives);
            if(score < 15 && !autopilot){
                System.out.print("              HELP [H]");
            }
            
            if(lives == 4){
                System.out.print(" MAX");
            }
            
            if(autopilot){
                System.out.print("    AUTOPILOT: ON [CHEATS]");
            }
            System.out.print("\nScore: " + score);
            if(boss){
                System.out.print("        [BOSS: " + bossLife + "]");
            }
            if(animate && !boss){
                score = score + 0.5;
            }
            
            //PRINT GRID
            for(int i=0;i<total;i++){
                if(i%xRes==0){
                    System.out.println();
                }
                
                if(grid.get(i).getDisplay() == true && animate && (hurt.getTime() >= 500 || score <= 10)){//NORMAL
                    System.out.print("■");
                }else if(grid.get(i).getDisplay() == true && animate && lives > 0 && hurt.getTime() < 250 && score > 10){//IF HURT
                    System.out.print("◇");
                }else if(grid.get(i).getDisplay() == true && animate && lives > 0 && hurt.getTime() < 500 && score > 10){//IF HURT
                    System.out.print("◆");
                }else if(grid.get(i).getDisplay() == true && !animate && lives < 1){//IF DEAD
                    System.out.print("□");
                }else if(grid.get(i).getDisplay() == true && !animate && lives > 0){//IF PAUSED
                    System.out.print("■");
                }else if(grid.get(i).isGift()){
                    System.out.print("❤");
                }else if(grid.get(i).isSkull()){
                    System.out.print("☠");
                }else if(grid.get(i).isSlow()){
                    System.out.print("◎");
                }else if(grid.get(i).isObstacle() && i < xRes){
                    System.out.print("▼");
                }else if(grid.get(i).isObstacle() && i > xRes-1){
                    System.out.print("▲");
                }else if(grid.get(i).isObstacle1()){
                    System.out.print("[");
                }else if(i < xRes && grid.get(i+xRes).isObstacle2()){
                    System.out.print("⇈");//→↑↓
                }else if(i > xRes-1 && grid.get(i-xRes).isObstacle2()){
                    System.out.print("⇊");
                }else if(grid.get(i).isBoss() == true){
                    if((int)(Math.random()*100) < 5 && animate){
                        facial = (int)(Math.random()*100);
                    }
                    if(bossLife == 1){
                        System.out.print(" :O");
                    }else if(hurt.getTime() < 1000){
                        if((int)(Math.random()*2) == 0){
                            System.out.print(" >:)");
                        }else{
                            System.out.print(" >:D");
                        }
                    }else if(facial < 80 && lives > 0){
                        System.out.print(" >:(");
                    }else if(facial >= 80 && facial < 100 && lives > 0){
                        System.out.print(" >:/");
                    }else{
                        System.out.print(" >:(");
                    }
                }else if(grid.get(i).isBossObs()){
                    System.out.print("x");
                }else if(grid.get(i).isBossBack()){
                    System.out.print("☺");
                }else if(i<xRes){
                    System.out.print("¯");
                }else{
                    System.out.print("_");
                }
            }
            
            //AUTOPILOT
            if(autopilot){
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        obsPos = obsPos + 1;
                        if(obsPos < grid.size()){
                            if(!player.isUp() && x == 1 && y == 1 && (grid.get(obsPos).isObstacle() || grid.get(obsPos).isObstacle1() || grid.get(obsPos).isObstacle2() || grid.get(obsPos).isSkull() || grid.get(obsPos+1).isBossObs())){
                                player.setUp(true);
                            }else if(player.isUp() && x == 1 && y == 0 && (grid.get(obsPos).isObstacle() || grid.get(obsPos).isObstacle1() || grid.get(obsPos).isObstacle2() || grid.get(obsPos).isSkull() || grid.get(obsPos+1).isBossObs())){
                                player.setUp(false);
                            }
                            
                            //GO FOR BOSS BACK
                            if(!player.isUp() && x == 1 && y == 1 && obsPos > xRes-1 && (grid.get(obsPos+1-xRes).isBossBack())){
                                player.setUp(true);
                            }else if(player.isUp() && x == 1 && y == 0 && obsPos < xRes && (grid.get(obsPos+1+xRes).isBossBack())){
                                player.setUp(false);
                            }
                            
                            //GO FOR GIFTS
                            if(!player.isUp() && x == 1 && y == 1 && lives < 4 && obsPos > xRes-1 && (grid.get(obsPos-xRes).isGift())){
                                player.setUp(true);
                            }else if(player.isUp() && x == 1 && y == 0 && lives < 4 && obsPos < xRes && (grid.get(obsPos+xRes).isGift())){
                                player.setUp(false);
                            }
                        }
                    }
                }
            }
            
            if(animate){
                //ANIMATES MOVING OBSTACLES TYPE 0
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isObstacle() && obsPos < xRes){
                            if(obsPos > 0){
                               grid.get(obsPos-1).setObstacle(true); 
                            }
                            grid.get(obsPos).setObstacle(false);
                        }
                        if(grid.get(obsPos).isObstacle() && obsPos >= xRes){
                            if(obsPos > xRes){
                               grid.get(obsPos-1).setObstacle(true); 
                            }
                            grid.get(obsPos).setObstacle(false);
                        }
                    }
                }
                
                //ANIMATES MOVING OBSTACLES TYPE 1
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isObstacle1() && obsPos < xRes){
                            if(obsPos > 0){
                               grid.get(obsPos-1).setObstacle1(true); 
                            }
                            grid.get(obsPos).setObstacle1(false);
                        }
                        if(grid.get(obsPos).isObstacle1() && obsPos >= xRes){
                            if(obsPos > xRes){
                               grid.get(obsPos-1).setObstacle1(true); 
                            }
                            grid.get(obsPos).setObstacle1(false);
                        }
                    }
                }
                
                //ANIMATES MOVING OBSTACLES TYPE 2
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isObstacle2() && obsPos < xRes){
                            if(obsPos > 0){
                               grid.get(obsPos-1).setObstacle2(true); 
                            }
                            grid.get(obsPos).setObstacle2(false);
                        }
                        if(grid.get(obsPos).isObstacle2() && obsPos >= xRes){
                            if(obsPos > xRes){
                               grid.get(obsPos-1).setObstacle2(true); 
                            }
                            grid.get(obsPos).setObstacle2(false);
                        }
                    }
                }
                
                //ANIMATES MOVING BOSS OBSTACLES
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isBossObs() && obsPos < xRes){
                            if(obsPos > 1){
                               grid.get(obsPos-2).setBossObs(true); 
                            }
                            grid.get(obsPos).setBossObs(false);
                        }
                        if(grid.get(obsPos).isBossObs() && obsPos >= xRes){
                            if(obsPos > xRes+1){
                               grid.get(obsPos-2).setBossObs(true); 
                            }
                            grid.get(obsPos).setBossObs(false);
                        }
                    }
                }
                
                //ANIMATES MOVING BOSS BACKS
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isBossBack() && obsPos < xRes){
                            if(obsPos > 1){
                               grid.get(obsPos-2).setBossBack(true); 
                            }
                            grid.get(obsPos).setBossBack(false);
                        }
                        if(grid.get(obsPos).isBossBack() && obsPos >= xRes){
                            if(obsPos > xRes+1){
                               grid.get(obsPos-2).setBossBack(true); 
                            }
                            grid.get(obsPos).setBossBack(false);
                        }
                    }
                }
                
                //ANIMATES MOVING GIFTS
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        giftPos = (xRes * y) + x;
                        if(grid.get(giftPos).isGift() && giftPos < xRes){
                            if(giftPos > 0){
                               grid.get(giftPos-1).setGift(true); 
                            }
                            grid.get(giftPos).setGift(false);
                        }
                        if(grid.get(giftPos).isGift() && giftPos >= xRes){
                            if(giftPos > xRes){
                               grid.get(giftPos-1).setGift(true); 
                            }
                            grid.get(giftPos).setGift(false);
                        }
                    }
                }
                
                //ANIMATES MOVING SKULLS
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        skullPos = (xRes * y) + x;
                        if(grid.get(skullPos).isSkull() && skullPos < xRes){
                            if(skullPos > 0){
                               grid.get(skullPos-1).setSkull(true); 
                            }
                            grid.get(skullPos).setSkull(false);
                        }
                        if(grid.get(skullPos).isSkull() && skullPos >= xRes){
                            if(skullPos > xRes){
                               grid.get(skullPos-1).setSkull(true); 
                            }
                            grid.get(skullPos).setSkull(false);
                        }
                    }
                }
                
                //ANIMATES MOVING SLOWS
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        slowPos = (xRes * y) + x;
                        if(grid.get(slowPos).isSlow() && slowPos < xRes){
                            if(slowPos > 0){
                               grid.get(slowPos-1).setSlow(true); 
                            }
                            grid.get(slowPos).setSlow(false);
                        }
                        if(grid.get(slowPos).isSlow() && slowPos >= xRes){
                            if(slowPos > xRes){
                               grid.get(slowPos-1).setSlow(true); 
                            }
                            grid.get(slowPos).setSlow(false);
                        }
                    }
                }
                
                /*
                    *This is purposly hardcoded, so that further down the road I'll have the ability
                    to give different obstacles different effects of health
                */
                
                //COLLISIONS OBSTACLES TYPE 0
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isObstacle() && !player.isUp() && x == 1 && y == 1){
                            lives = lives - 1;
                            hurt.start();
                        }else if(grid.get(obsPos).isObstacle() && player.isUp() && x == 1 && y == 0){
                            lives = lives - 1;
                            hurt.start();
                        }
                    }
                }
                //COLLISIONS OBSTACLES TYPE 1
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isObstacle1() && !player.isUp() && x == 1 && y == 1){
                            lives = lives - 1;
                            hurt.start();
                        }else if(grid.get(obsPos).isObstacle1() && player.isUp() && x == 1 && y == 0){
                            lives = lives - 1;
                            hurt.start();
                        }
                    }
                }
                //COLLISIONS OBSTACLES TYPE 2
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isObstacle2() && !player.isUp() && x == 1 && y == 1){
                            lives = lives - 1;
                            hurt.start();
                        }else if(grid.get(obsPos).isObstacle2() && player.isUp() && x == 1 && y == 0){
                            lives = lives - 1;
                            hurt.start();
                        }
                    }
                }
                
                //COLLISIONS BOSS OBSTACLES
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isBossObs() && !player.isUp() && x == 1 && y == 1){
                            lives = lives - 1;
                            hurt.start();
                        }else if(grid.get(obsPos).isBossObs() && player.isUp() && x == 1 && y == 0){
                            lives = lives - 1;
                            hurt.start();
                        }
                    }
                }
                
                //COLLISIONS BOSS BACKS
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        obsPos = (xRes * y) + x;
                        if(grid.get(obsPos).isBossBack() && !player.isUp() && x == 1 && y == 1){
                            bossLife = bossLife - 1;
                        }else if(grid.get(obsPos).isBossBack() && player.isUp() && x == 1 && y == 0){
                            bossLife = bossLife - 1;
                        }
                    }
                }
                
                //COLLISIONS GIFTS
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        giftPos = (xRes * y) + x;
                        if(grid.get(giftPos).isGift() && !player.isUp() && x == 1 && y == 1 && lives < 4){
                            lives = lives + giftNum;
                        }else if(grid.get(giftPos).isGift() && player.isUp() && x == 1 && y == 0 && lives < 4){
                            lives = lives + giftNum;
                        }
                    }
                }
                //COLLISIONS SKULLS
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        skullPos = (xRes * y) + x;
                        if(grid.get(skullPos).isSkull() && !player.isUp() && x == 1 && y == 1){
                            lives = -99;
                        }else if(grid.get(skullPos).isSkull() && player.isUp() && x == 1 && y == 0){
                            lives = -99;
                        }
                    }
                }
                //COLLISIONS SLOWS
                for(int x=0;x<xRes;x++){
                    for(int y=0;y<yRes;y++){
                        slowPos = (xRes * y) + x;
                        if(grid.get(slowPos).isSlow() && !player.isUp() && x == 1 && y == 1){
                            slowDown = true;
                            scoreKeep = score;
                            if(obsType == 1){//Prevents slow and obs1
                                obsType = 0;
                            }
                        }else if(grid.get(slowPos).isSlow() && player.isUp() && x == 1 && y == 0){
                            slowDown = true;
                            scoreKeep = score;
                            if(obsType == 1){//Prevents slow and obs1
                                obsType = 0;
                            }
                        }
                    }
                }
            }
            if(lives <= 0){
                animate = false;
                System.out.println();
                System.out.println("You ran out of lives!      [R]↺");
            }
            if(lives > 0){
                if(animate == true){
                    if(!slowDown || score - scoreKeep >= slowLast - 5){
                        System.out.println();
                        System.out.println("►❚❚ PLAY [P]    ");
                    }else if(slowDown && score - scoreKeep < slowLast - 5){
                        System.out.println();
                        System.out.println("►❚❚ MOLASSES [P]    ");
                    }
                }else{
                    System.out.println();
                    System.out.println("►❚❚ PAUSED [P]");
                }
            }
            
            if(help){
                for(int i=0;i<2;i++){
                    System.out.println();
                }
                System.out.println("**Tutorial**");
                System.out.println("Avoid the obstacles!");
                System.out.println("\nPress spacebar to jump, and switch lanes");
                System.out.println("[E] Exit, [P] Pause, [H] Help, [R] Restart");
                
                System.out.println("\nTouch the hearts to gain one life! Always pass through arrows");
                System.out.println("Don't touch the skulls, they'll kill you instantly");
            }
        }
    }
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    public void start(final int ticks){
        Thread gameThread = new Thread(){
            public void run(){
                while(loop){
                    loop();
                    try{
                        Thread.sleep(1000 / ticks);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };	
        gameThread.start();
    }
    public static void main(String[]args){
        Driver g = new Driver();
        g.start(60);
    }
}