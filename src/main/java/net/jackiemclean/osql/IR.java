package net.jackiemclean.osql;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class IR {
  private final List<Stmt> statements;

  public IR(List<Stmt> statements) {
    this.statements = Collections.unmodifiableList(statements);
  }

  public static interface Stmt {}

  public static class UpdateStmt implements Stmt {
    private final String worldName;
    private final Set<Assignment> assignments;
    private final Set<UUID> orderIds;
    private final Set<UUID> orderObjectIds;

    public UpdateStmt(
        String worldName,
        Set<Assignment> assignments,
        Set<UUID> orderIds,
        Set<UUID> orderObjectIds) {
      this.worldName = worldName;
      this.assignments = assignments;
      this.orderIds = orderIds;
      this.orderObjectIds = orderObjectIds;
    }

    public static class Assignment {}

    @Override
    public boolean equals(Object o) {
      if (o == null || !(o instanceof UpdateStmt)) return false;
      if (o == this) return true;
      UpdateStmt ou = (UpdateStmt) o;
      return Objects.equals(worldName, ou.worldName)
          && Objects.equals(assignments, ou.assignments)
          && Objects.equals(orderIds, ou.orderIds)
          && Objects.equals(orderObjectIds, ou.orderObjectIds);
    }

    @Override
    public int hashCode() {
      return Objects.hash(worldName, assignments, orderIds, orderObjectIds);
    }

    @Override
    public String toString() {
      return String.join(
          System.lineSeparator(),
          "UpdateStmt(",
          "  world: " + worldName,
          "  assignments: " + assignments,
          "  orderIds: " + orderIds,
          "  orderObjectIds: " + orderObjectIds,
          ")");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !(o instanceof IR)) return false;
    if (o == this) return true;
    IR oi = (IR) o;
    return Objects.equals(statements, oi.statements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statements);
  }

  @Override
  public String toString() {
    return String.join(System.lineSeparator(), "IR (", "  statements: " + statements, ")");
  }
}
