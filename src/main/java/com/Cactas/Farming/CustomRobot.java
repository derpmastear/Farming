package com.Cactas.Farming;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;

import java.util.concurrent.*;

public class CustomRobot {
    public final ScheduledExecutorService mouseExec;
    public final ScheduledExecutorService keyExec;
    public final ScheduledExecutorService cancelExec;
    public final ScheduledExecutorService wrappedExec;
    public final SerialExecutor theExec;
    public ScheduledFuture<?> currentMouseExec;
    public ScheduledFuture<?> currentKeyExec;
    public int currentFarm = 0;
    public final int TICKSPEED = 10;
    public final Object MONITOR = new Object();
    public String[] farmType = new String[]{"Cane", "Wart"};
    /*
     * Order: 0:Forward, 1:Left, 2:Back, 3:Right, 4:Jump, 5:Sneak, 6:Click
     */
    private KeyBinding[] myKeyBinds;
    private KeyPressRunnable kpr;
    private MouseMoveRunnable mmr;
    private checkPositionRunnable cpr;
    private runCheckPositionRunnable rcpr;
    private Canceler cr;
    private EntityPlayerSP player;
    public CustomRobot(KeyBinding[] k){
        myKeyBinds = k;
        mouseExec = Executors.newSingleThreadScheduledExecutor();
        keyExec = Executors.newSingleThreadScheduledExecutor();
        cancelExec = Executors.newSingleThreadScheduledExecutor();
        wrappedExec = Executors.newSingleThreadScheduledExecutor();
        theExec = new SerialExecutor(wrappedExec);
        kpr = new KeyPressRunnable(false, null);
        mmr = new MouseMoveRunnable(-1, -1 );
        cr = new Canceler(null);
        cpr = new checkPositionRunnable(kpr);
        rcpr = new runCheckPositionRunnable(keyExec);
        player = Minecraft.getMinecraft().thePlayer;
    }

    public void farm() throws InterruptedException{
        if(currentFarm == 0){ //cane
            lockHeading(100);
        }
        else if(currentFarm == 1){ //wart
            lockHeading(1000);
            moveMouse(62.5, 0, 1000);
            pressKey(0, 32000);
            pressKey(6, 32000);

            moveMouse(-62.5, 0, 1000);
            lockHeading(1000);
            moveMouse(-62.5, 0, 1000);
            pressKey(0, 32000);
            pressKey(6, 32000);
        }
    }
    public void pressKey(int k, int time){
        kpr.set(true, myKeyBinds[k]);
        kpr.run();
        kpr.set(false, myKeyBinds[k]);
        currentKeyExec = keyExec.schedule(cr, time, TimeUnit.MILLISECONDS);
    }

    public void pressKeyUntil(int k, int dx, int dy, int dz){
        System.out.println("pressKeyUntil triggered!");
        player = Minecraft.getMinecraft().thePlayer;
        int targetX = (int)Math.floor(player.posX + dx);
        int targetY = (int)Math.floor(player.posY + dy);
        int targetZ = (int)Math.floor(player.posZ + dz);
        kpr.set(true, myKeyBinds[k]);
        theExec.execute(kpr);
        cpr.setTarget(targetX, targetY, targetZ);
        theExec.execute(rcpr);
    }

    public void moveMouse(double dyaw, double dpitch, int time){
        System.out.println("move mouse triggered!!");
        double tYaw = (dyaw * TICKSPEED) / time;
        double tPitch = (dpitch * TICKSPEED) / time;
        mmr.set(tYaw, tPitch);
        currentMouseExec = mouseExec.scheduleAtFixedRate(mmr, 0, TICKSPEED, TimeUnit.MILLISECONDS);
        cr = new Canceler(currentMouseExec);
        cancelExec.schedule(cr, (time + TICKSPEED / time), TimeUnit.MILLISECONDS);
    }

    public void lockHeading(int time){
        System.out.println("lock heading triggered!");
        player = Minecraft.getMinecraft().thePlayer;
        double yaw = 0;
        if (player.rotationYaw < 0) {
            yaw = player.rotationYaw % -180;
        } else {
            yaw = player.rotationYaw % 180;
        }
        double smallestDiff = 0;
        if (-45 < yaw && yaw < 45) {
            smallestDiff = -yaw;
        } else if (45 <= yaw && 135 >= yaw) {
            smallestDiff = 90 - yaw;
        } else if (-45 >= yaw && -135 <= yaw) {
            smallestDiff = -90 - yaw;
        } else if (-135 > yaw && yaw >= -180) {
            smallestDiff = -180 - yaw;
        } else if (yaw > 135 && yaw < 180) {
            smallestDiff = 180 - yaw;
        }
        System.out.println("Smallest: " + smallestDiff + "Player yaw: " + yaw);
        if (smallestDiff != 0) {
            double tYaw = (smallestDiff * TICKSPEED) / time;
            mmr.set(tYaw, 0);
            currentMouseExec = mouseExec.scheduleAtFixedRate(mmr, 0, TICKSPEED, TimeUnit.MILLISECONDS);
            cr = new Canceler(currentMouseExec);
            cancelExec.schedule(cr, (time + TICKSPEED / time), TimeUnit.MILLISECONDS);
        }
    }
    /*
        Private Runnable Classes
     */
    private class KeyPressRunnable implements Runnable{
        public boolean state;
        private KeyBinding myKeyBinds;
        public KeyPressRunnable(boolean b, KeyBinding kb)
        {
            state = b;
            myKeyBinds = kb;
        }
        public void run(){
            KeyBinding.setKeyBindState(myKeyBinds.getKeyCode(), state);
        }
        public void set(boolean b, KeyBinding kb){
            state = b;
            myKeyBinds = kb;
        }
    }

    private class checkPositionRunnable implements Runnable{
        private boolean blockVar;
        private int targetX;
        private int targetY;
        private int targetZ;
        private ScheduledFuture<?> task;
        private KeyPressRunnable kpr;
        public checkPositionRunnable(KeyPressRunnable k){
            kpr = k;
        }
        public void run(){
            EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
            if(p.posX == targetX && p.posY == targetY && p.posZ == targetZ){
                Canceler c = new Canceler(task);
                c.run();
                kpr.set(false, kpr.myKeyBinds);
            }
        }
        public void setTarget(int x, int y, int z){
            targetX = x;
            targetY = y;
            targetZ = z;
            blockVar = true;
            System.out.println("TARGET x: " + x + ", y: " + y + ", z:" + z);
        }
        public void setTask(ScheduledFuture<?> t){
            task = t;
        }
    }

    private class MouseMoveRunnable implements Runnable{
        private double dyaw;
        private double dpitch;
        public MouseMoveRunnable(double y, double p){
            dyaw = y;
            dpitch = p;
        }
        public void run(){
            player = Minecraft.getMinecraft().thePlayer;
            player.rotationPitch += dpitch;
            player.rotationYaw += dyaw;
        }
        public void set(double y, double p){
            dyaw = y;
            dpitch = p;
        }
    }

    private class Canceler implements Runnable{
        public ScheduledFuture<?> exec;
        public Canceler(ScheduledFuture<?> e){
            exec = e;
        }
        public void run(){
            exec.cancel(true);
        }
        public void set(ScheduledFuture<?> e){
            exec = e;
        }
    }

    public class runCheckPositionRunnable implements Runnable{
        ScheduledExecutorService keyExec;
        ScheduledFuture<?> sf;
        public runCheckPositionRunnable(ScheduledExecutorService x){
            keyExec = x;
        }
        public void run(){
            sf = keyExec.schedule(cpr, 50, TimeUnit.MILLISECONDS);
        }
        public boolean isComplete(){
            return sf.isCancelled();
        }
    }
}
