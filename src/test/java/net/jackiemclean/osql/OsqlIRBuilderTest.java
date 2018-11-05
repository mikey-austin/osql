package net.jackiemclean.osql;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;

public class OsqlIRBuilderTest {
  @Test
  public void testBuilderFromString() {
    // Build a the expected intermediate result.
    Set<UUID> orderIds =
        new HashSet<>(
            asList(
                UUID.fromString("6f84704c-9f0f-4407-95de-1f727ff09c54"),
                UUID.fromString("b28ccd63-5aa5-4f8b-8ec2-a314f3607b2f")));
    Set<UUID> orderObjectIds =
        new HashSet<>(asList(UUID.fromString("c37923ff-fe93-4c36-84a5-bf547e21186d")));
    IR.UpdateStmt updateStmt =
        new IR.UpdateStmt(
            "myWorldName", new HashSet<IR.UpdateStmt.Assignment>(), orderIds, orderObjectIds);
    IR expected = new IR(Arrays.<IR.Stmt>asList(updateStmt));

    String osql =
        String.join(
            System.lineSeparator(),
            "UPDATE myWorldName SET",
            "  my.cool.field  = 123,",
            "  my.other.field = 333",
            "WHERE orderId IN (",
            "  6f84704c-9f0f-4407-95de-1f727ff09c54,",
            "  b28ccd63-5aa5-4f8b-8ec2-a314f3607b2f",
            ") AND orderObjectId IN (",
            "  c37923ff-fe93-4c36-84a5-bf547e21186d",
            ")");
    IR got = OsqlIRBuilder.fromString(osql);
    assertEquals(expected, got);
  }
}
