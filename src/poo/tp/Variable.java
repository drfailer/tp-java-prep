package poo.tp;

import java.util.HashMap;

public class Variable implements Node {
  private final String name;
  private boolean isNegative;

  public Variable(String name) {
    if (name.charAt(0) == '-') {
      this.isNegative = true;
      this.name = name.substring(1);
    } else {
      this.isNegative = false;
      this.name = name;
    }
  }

  @Override
  public double eval(HashMap<String, Double> assignment) {
    if (this.isNegative) {
      return -1 * assignment.get(this.name);
    } else {
      return assignment.get(this.name);
    }
  }

  @Override
  public String toString() {
    if (this.isNegative) {
      return "-" + this.name;
    } else {
      return this.name;
    }
  }
}
