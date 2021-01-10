package com.Cactas.Farming;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Farming.MODID, version = Farming.VERSION)
public class Farming
{
    public static final String MODID = "cactas_farming";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		System.out.println("Hello World");
    }
}
