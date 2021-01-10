package com.Cactas.Farming;

import javafx.scene.control.TextFormatter;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;

@Mod(modid = Farming.MODID, version = Farming.VERSION)
public class Farming
{
    public static final String MODID = "farming_time";
    public static final String VERSION = "1.0";
    public static com.Cactas.Farming.EventHandler myEventHandler;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        try{ myEventHandler = new com.Cactas.Farming.EventHandler();}
        catch(AWTException awtException) {
            System.out.println("AWTException at eventHandler init");
        }
        MinecraftForge.EVENT_BUS.register(myEventHandler);
        try{
            ChangeTypeCommand ctc = new ChangeTypeCommand();
            ctc.mySlave = myEventHandler.mySlave;
            ClientCommandHandler.instance.registerCommand(new ChangeTypeCommand());
        }
        catch(Exception e){
            System.out.println("Something happened in preInit.");
            e.printStackTrace();
        }
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {

    }
}
