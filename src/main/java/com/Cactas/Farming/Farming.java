package com.Cactas.Farming;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sun.security.util.SecurityConstants;

import java.awt.*;

@Mod(modid = Farming.MODID, version = Farming.VERSION)
public class Farming
{
    public static final String MODID = "farming_time";
    public static final String VERSION = "1.0";
    public static com.Cactas.Farming.EventHandler myEventHandler;
    @EventHandler
    public void init(FMLInitializationEvent event) {
        try{ myEventHandler = new com.Cactas.Farming.EventHandler();}
        catch(AWTException awtException) {
            System.out.println("AWTException at eventHandler init");
        }
        MinecraftForge.EVENT_BUS.register(myEventHandler);
    }
}
