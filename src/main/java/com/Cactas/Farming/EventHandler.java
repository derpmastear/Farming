package com.Cactas.Farming;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class EventHandler {
    EventHandler(){

    }
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event){
        if(!Mouse.getEventButtonState()){
            return;
        }
        switch(Mouse.getEventButton()){
        }
    }

    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event){
        
    }
}
