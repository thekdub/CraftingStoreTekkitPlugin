package com.thekdub.craftingstore;

import org.bukkit.ChatColor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * A generic logger object for ServerElements.
 */
public class Logger {

  /**
   * The file directory path.
   */
  private final String PATH;
  /**
   * The log file.
   */
  private File file;
  /**
   * The BufferedWriter for the log file.
   */
  private BufferedWriter writer;

  /**
   * Creates a new Logger object and updates the path variable using the plugin's directory.
   */
  public Logger() {
    this.PATH = CraftingStore.getInstance().getDataFolder() + File.separator + "logs" + File.separator;
  }

  /**
   * Creates directories for the logger file if not existing.
   * Initializes the File and BufferedWriter variables if not initialized.
   */
  private void init() {
    if (file == null) {
      file = new File(PATH + Time.today() + ".log");
      if (!file.exists()) {
        file.getParentFile().mkdirs();
      }
      writer = null;
    }
    if (writer == null) {
      try {
        writer = new BufferedWriter(new FileWriter(file, true));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Checks and updates the current log file date to ensure log files roll over properly at the start of each day.
   */
  private void checkDate() {
    if (file == null) {
      init();
      return;
    }
    if (!file.getName().equalsIgnoreCase(Time.today() + ".log")) {
      close();
      init();
    }
  }

  /**
   * Verifies log file fate.
   * Writes the passed message to file.
   * Saves the log file.
   *
   * @param MESSAGE the message to log to file.
   */
  public void log(final String MESSAGE) {
    checkDate();
    try {
      writer.write(Time.now() + " >> " + ChatColor.stripColor(MESSAGE) + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
    save();
  }

  /**
   * Flushes the BufferedWriter output.
   */
  private void save() {
    try {
      writer.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Flushes and closes the BufferedWriter.
   * Resets the File and BufferedWriter objects to null for re-initialization.
   */
  public void close() {
    init();
    try {
      writer.flush();
      writer.close();
      file = null;
      writer = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
