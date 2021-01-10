package com.Cactas.Farming;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChangeTypeCommand extends CommandBase {
    public CustomRobot mySlave;
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender){
        return true;
    }
    @Override
    public String getCommandName(){
        return "farming_config";
    }

    @Override
    public String getCommandUsage(ICommandSender sender){
        return "Use to set type of farm: farming_config <cane/wart>";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException{
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("cane")){
                mySlave.currentFarm = 0;
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[Farm]: Set to cane."));
            }
            else if(args[0].equalsIgnoreCase("wart")){
                mySlave.currentFarm = 1;
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[Farm]: Set to wart."));
            }
        }
        else{
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[Farm]: Wrong Usage, Use to set type of farm: farming_config <cane/wart>"));
        }
    }
}
