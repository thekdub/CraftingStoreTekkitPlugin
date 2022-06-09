package com.thekdub.craftingstore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftingStore extends JavaPlugin {

  private static CraftingStore instance = null;

  private static Logger log = null;

  private static Watcher watcher = null;

  public static CraftingStore getInstance() {
    return instance;
  }

  public static Logger getLog() {
    return log;
  }

  public void onEnable() {

    instance = this;

    saveDefaultConfig();

    log = new Logger();
    log.log("Loading plugin...");

    if (getConfig().getString("token", "").length() == 0) {
      getLogger().warning("[CraftingStore] No token detected! Disabling plugin!");
      log.log("No token detected! Disabling plugin!");
      getPluginLoader().disablePlugin(this);
      return;
    }

    watcher = new Watcher();

    Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, watcher, 20 * 30, 20 * 30);

    log.log("Plugin loaded!");
  }

  public void onDisable() {
    watcher.save();
    Bukkit.getScheduler().cancelTasks(this);
    log.log("Plugin unloaded!");
    log.close();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getLabel().equalsIgnoreCase("csreload")) {
      Bukkit.getPluginManager().disablePlugin(this);
      Bukkit.getPluginManager().enablePlugin(this);
      reloadConfig();
      sender.sendMessage(ChatColor.GOLD + "CraftingStore config reloaded successfully!");
      return true;
    }
    return false;
  }
}
