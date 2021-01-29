package com.Cactas.Farming;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class EventHandler {
    public static CustomRobot mySlave;
    public static KeyBinding startFarm;

    public EventHandler() throws AWTException {
        mySlave = new CustomRobot(getKeybinds());
        registerKeyBindings();
    }
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event){
        if(!Mouse.getEventButtonState()){return;}
        if(mySlave.currentMouseExec != null){
            mySlave.currentMouseExec.cancel(true);
        }
        if(mySlave.currentKeyExec != null){
            mySlave.currentKeyExec.cancel(false);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) throws InterruptedException{
        if(startFarm.isPressed()){
            mySlave.farm();
        }
    }

    public KeyBinding[] getKeybinds(){
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        return new KeyBinding[]{gs.keyBindForward, gs.keyBindLeft, gs.keyBindBack, gs.keyBindRight, gs.keyBindJump, gs.keyBindSneak, gs.keyBindAttack};
    }
    public static void registerKeyBindings(){
        startFarm = new KeyBinding("key.categories.movement", Keyboard.KEY_V, "Farming Time");
        ClientRegistry.registerKeyBinding(startFarm);
    }
}
