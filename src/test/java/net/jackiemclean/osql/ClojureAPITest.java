package net.jackiemclean.osql;

import static org.junit.Assert.*;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import java.util.UUID;
import net.jackiemclean.osql.OrderProto.Order;
import org.junit.Test;

public class ClojureAPITest {
  @Test
  public void testSimple() {
    UUID orderId = UUID.randomUUID();
    Order order = Order.newBuilder().setId(orderId.toString()).build();

    IFn plus = Clojure.var("clojure.core", "+");
    assertEquals(3L, plus.invoke(1, 2));
  }
}
