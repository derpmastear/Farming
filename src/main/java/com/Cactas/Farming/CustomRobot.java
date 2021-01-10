package com.Cactas.Farming;

import net.minecraft.client.settings.KeyBinding;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomRobot {
    private final ScheduledExecutorService execServ;
    /*
     * Order: Forward, Left, Back, Right, Jump, Sneak
     */
    private KeyBinding[] myKeyBinds;
    private KeyPressRunnable kpr;
    public CustomRobot(KeyBinding[] k) throws AWTException{
        myKeyBinds = k;
        execServ = Executors.newSingleThreadScheduledExecutor();
        kpr = new KeyPressRunnable(false, null);
    }

    public void testMove(){
        System.out.println("aaaaaaaaaaaaaaaaaa move movemove");
        kpr.set(true, myKeyBinds[0]);
        kpr.run();
        kpr.set(false, myKeyBinds[0]);
        execServ.schedule(kpr, 1, TimeUnit.SECONDS);
    }
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
}
