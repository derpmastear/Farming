package com.Cactas.Farming;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.event.KeyEvent;

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
        mySlave.currentMouseExec.cancel(true);
        mySlave.currentKeyExec.cancel(false);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event){
        if(startFarm.isPressed()){
            mySlave.moveMouse(-360, 0, 1000);
            mySlave.pressKey(0, 10000);
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
