package net.jackiemclean.osql;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;

public class ClojureTranslatorTest {
  @Test
  public void testTranslation() {
    String osql =
        String.join(
            System.lineSeparator(),
            "UPDATE myWorldName SET",
            "  hotel.room.id  = 123,",
            "  reservation.guest.firstName = \"john smith\"",
            "WHERE orderId IN (",
            "  6f84704c-9f0f-4407-95de-1f727ff09c54,",
            "  b28ccd63-5aa5-4f8b-8ec2-a314f3607b2f",
            ") AND orderObjectId IN (",
            "  c37923ff-fe93-4c36-84a5-bf547e21186d",
            ")");

    IR ir = OsqlIRBuilder.fromString(osql);
    List<IR.Stmt> statements = ir.getStatements();
    assertEquals(1, statements.size());
    assertTrue(statements.get(0) instanceof IR.UpdateStmt);

    IR.UpdateStmt stmt = (IR.UpdateStmt) statements.get(0);
    ClojureTranslator translator = new ClojureTranslator();
    String clojureCode = translator.translate(stmt);

    String expected =
        String.join(
            System.lineSeparator(),
            "(fn [obj]",
            "  (.setId (.getRoom (.getHotel obj)) 123)",
            "  (.setFirstName (.getGuest (.getReservation obj)) \"john smith\")",
            ")");
    assertEquals(expected, clojureCode);
  }
}
