package net.jackiemclean.osql;

import static org.junit.Assert.*;

import java.util.UUID;
import net.jackiemclean.osql.OrderProto.Order;
import org.junit.Test;

public class OrderProtoTest {
  @Test
  public void shouldAnswerWithTrue() {
    UUID orderId = UUID.randomUUID();
    Order order = Order.newBuilder().setId(orderId.toString()).build();
    assertEquals(order.getId(), orderId.toString());
  }
}
