package com.thekdub.craftingstore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandQueue {

  boolean success = false;
  String error;
  String message;
  ArrayList<Command> result;

  public CommandQueue() {}

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ArrayList<Command> getResult() {
    return result;
  }

  public void setResult(ArrayList<Command> result) {
    this.result = result;
  }

  @Override
  public String toString() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(this);
    } catch (Exception e) {
      CraftingStore.getLog().log("[ERROR] Could not map to JSON! " + e.getMessage());
    }
    return "CommandQueue{" +
          "success=" + success +
          ", error='" + error + '\'' +
          ", message='" + message + '\'' +
          ", result=" + result +
          '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommandQueue that = (CommandQueue) o;
    return success == that.success &&
          Objects.equals(error, that.error) &&
          Objects.equals(message, that.message) &&
          Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, error, message, result);
  }
}
