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
    IFn plus = Clojure.var("clojure.core", "+");
    assertEquals(3L, plus.invoke(1, 2));
  }

  @Test
  public void testEval() {
    UUID orderId = UUID.randomUUID();
    Order.Builder order = Order.newBuilder();

    IFn eval = Clojure.var("clojure.core", "eval");
    IFn idSetter =
        (IFn) eval.invoke(Clojure.read("(fn [x] (.setId x \"" + orderId.toString() + "\"))"));
    idSetter.invoke(order);

    assertEquals(orderId.toString(), order.build().getId());
  }
}
