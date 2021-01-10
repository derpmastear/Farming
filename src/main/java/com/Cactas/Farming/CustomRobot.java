package com.Cactas.Farming;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CustomRobot {
    public final ScheduledExecutorService mouseExec;
    public final ScheduledExecutorService keyExec;
    public final ScheduledExecutorService cancelExec;
    public ScheduledFuture<?> currentMouseExec;
    public ScheduledFuture<?> currentKeyExec;
    public int currentFarm = 0;
    public String[] farmType = new String[]{"Cane", "Wart"};
    /*
     * Order: 0:Forward, 1:Left, 2:Back, 3:Right, 4:Jump, 5:Sneak, 6:Click
     */
    private KeyBinding[] myKeyBinds;
    private KeyPressRunnable kpr;
    private MouseMoveRunnable mmr;
    private Canceler cr;
    private EntityPlayerSP player;
    public CustomRobot(KeyBinding[] k) throws AWTException{
        myKeyBinds = k;
        mouseExec = Executors.newSingleThreadScheduledExecutor();
        keyExec = Executors.newSingleThreadScheduledExecutor();
        cancelExec = Executors.newSingleThreadScheduledExecutor();
        kpr = new KeyPressRunnable(false, null);
        mmr = new MouseMoveRunnable(-1, -1 );
        cr = new Canceler(null);
        player = Minecraft.getMinecraft().thePlayer;
    }

    public void pressKey(int k, int time){
        kpr.set(true, myKeyBinds[k]);
        kpr.run();
        kpr.set(false, myKeyBinds[k]);
        currentKeyExec = keyExec.schedule(kpr, time, TimeUnit.MILLISECONDS);
    }

    public void moveMouse(int dyaw, int dpitch, int time){
        double tYaw = ((double)dyaw * 50)/time;
        double tPitch = ((double)dpitch * 50)/time;
        mmr.set(tYaw, tPitch);
        currentMouseExec = mouseExec.scheduleAtFixedRate(mmr, 0, 50, TimeUnit.MILLISECONDS);
        cr.set(currentMouseExec);
        cancelExec.schedule(cr, (time + 50/time), TimeUnit.MILLISECONDS);
    }

    /*
        Private Runnable Classes
     */
    private class KeyPressRunnable implements Runnable{
        private boolean state;
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
            exec.cancel(false);
            System.out.println("CANCELING: " + exec);
        }
        public void set(ScheduledFuture<?> e){
            exec = e;
        }
    }
}
