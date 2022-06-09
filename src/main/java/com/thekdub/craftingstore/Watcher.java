package com.thekdub.craftingstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Watcher implements Runnable {

  private final File dataFile = new File(CraftingStore.getInstance().getDataFolder() + File.separator + "data.yml");
  private final YamlConfiguration dataStore = YamlConfiguration.loadConfiguration(dataFile);
  private final HashSet<Command> pending = new HashSet<>();
  private long lastID = 0;

  public Watcher() {
    lastID = dataStore.getLong("lastID", 0);
    ObjectMapper mapper = new ObjectMapper();
    dataStore.getStringList("pending").stream().map(str -> {
      try {
        return mapper.readValue(str, Command.class);
      } catch (JsonProcessingException e) {
        CraftingStore.getLog().log("[ERROR] Could not map from JSON! " + e.getMessage());
      }
      String[] parts = str.split(",");
      return new Command(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], parts[4], parts[5],
            Double.parseDouble(parts[6]), Long.parseLong(parts[7]), Double.parseDouble(parts[8]), parts[9],
            Boolean.parseBoolean(parts[10]));
    }).forEach(pending::add);
  }

  @Override
  public void run() {
    CommandQueue queue = APIHandler.getCommands();
    ArrayList<Integer> completed = new ArrayList<>();
    if (queue.isSuccess()) {
      CraftingStore.getLog().log("[SUCCESS] Command retrieval completed!");
      queue.getResult().stream().filter(command -> command.getId() > lastID && !pending.contains(command))
            .forEach(command -> {
              if (command.getMcName() == null ||
                    (command.isRequireOnline() && Bukkit.getPlayer(command.getMcName()) != null)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.getCommand());
                CraftingStore.getLog().log("[PROCESSED] " + command);
                completed.add(command.getId());
                if (command.getId() > lastID) {
                  lastID = command.getId();
                }
              }
              else {
                pending.add(command);
                CraftingStore.getLog().log("[PENDING] " + command);
              }
            });
      new ArrayList<>(pending).forEach(command -> {
        if (command.getMcName() == null || Bukkit.getPlayer(command.getMcName()) != null) {
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.getCommand());
          CraftingStore.getLog().log("[PROCESSED] " + command);
          completed.add(command.getId());
          if (command.getId() > lastID) {
            lastID = command.getId();
          }
          pending.remove(command);
        }
      });
      if (completed.size() > 0) {
        int[] completedIDs = new int[completed.size()];
        for (int i = 0; i < completed.size(); i++) {
          completedIDs[i] = completed.get(i);
        }
        if (APIHandler.complete(completedIDs)) {
          CraftingStore.getLog().log("[SUCCESS] ID completion successful! " + Arrays.toString(completedIDs));
        }
        else {
          CraftingStore.getLog().log("[FAILURE] ID completion failed! " + Arrays.toString(completedIDs));
        }
      }
      save();
    }
    else {
      CraftingStore.getLog().log("[FAILURE] Command retrieval failed!");
    }
  }

  public void save() {
    dataStore.set("lastID", lastID);
    dataStore.set("pending", pending.stream().map(Command::toString).collect(Collectors.toList()));
    try {
      dataStore.save(dataFile);
    } catch (IOException e) {
      CraftingStore.getLog().log("[ERROR] Could not save datastore! " + e.getMessage());
    }
  }
}
