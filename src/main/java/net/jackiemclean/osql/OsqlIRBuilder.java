package net.jackiemclean.osql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import main.antlr4.net.jackiemclean.osql.OsqlBaseListener;
import main.antlr4.net.jackiemclean.osql.OsqlLexer;
import main.antlr4.net.jackiemclean.osql.OsqlParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.*;

public class OsqlIRBuilder {
  private OsqlParser parser;
  private List<IR.Stmt> statements;

  private OsqlIRBuilder(OsqlParser parser) {
    this.parser = parser;
    this.statements = new ArrayList<>();
    parser.addParseListener(this.new UpdateBuilder());
  }

  public static IR fromString(String osql) {
    CharStream stream = (CharStream) new ANTLRInputStream(osql);
    return parseInputStream(stream);
  }

  public static IR fromFile(String fileName) throws IOException {
    CharStream stream = (CharStream) new ANTLRFileStream(fileName);
    return parseInputStream(stream);
  }

  private static IR parseInputStream(CharStream stream) {
    OsqlLexer lexer = new OsqlLexer(stream);
    TokenStream tokenStream = new CommonTokenStream(lexer);
    OsqlParser parser = new OsqlParser(tokenStream);

    OsqlIRBuilder irBuilder = new OsqlIRBuilder(parser);

    return irBuilder.build();
  }

  private IR build() {
    parser.osql();
    IR ir = new IR(statements);
    return ir;
  }

  private class UpdateBuilder extends OsqlBaseListener {
    private String worldName;
    private Set<UUID> orderIds;
    private Set<UUID> orderObjectIds;
    private Set<IR.UpdateStmt.Assignment> assignments;

    private UpdateBuilder() {}

    @Override
    public void enterUpdateStatement(@NotNull OsqlParser.UpdateStatementContext ctx) {
      this.worldName = null;
      this.orderIds = new HashSet<>();
      this.orderObjectIds = new HashSet<>();
      this.assignments = new HashSet<>();
    }

    @Override
    public void exitUpdateStatement(@NotNull OsqlParser.UpdateStatementContext ctx) {
      IR.UpdateStmt updateStmt =
          new IR.UpdateStmt(worldName, assignments, orderIds, orderObjectIds);
      statements.add(updateStmt);
    }

    @Override
    public void exitWorldName(@NotNull OsqlParser.WorldNameContext ctx) {
      this.worldName = ctx.getChild(0).getText();
    }

    @Override
    public void exitOrderIdSingle(@NotNull OsqlParser.OrderIdSingleContext ctx) {
      UUID orderId = UUID.fromString(ctx.getChild(2).getText());
      orderIds.add(orderId);
    }

    @Override
    public void exitOrderIdList(@NotNull OsqlParser.OrderIdListContext ctx) {
      orderIds.addAll(extractUUIDs(ctx.uuidList()));
    }

    @Override
    public void exitOrderObjectIdSingle(@NotNull OsqlParser.OrderObjectIdSingleContext ctx) {
      UUID orderObjectId = UUID.fromString(ctx.getChild(2).getText());
      orderObjectIds.add(orderObjectId);
    }

    @Override
    public void exitOrderObjectIdList(@NotNull OsqlParser.OrderObjectIdListContext ctx) {
      orderObjectIds.addAll(extractUUIDs(ctx.uuidList()));
    }

    private Collection<UUID> extractUUIDs(OsqlParser.UuidListContext ctx) {
      ArrayList<UUID> ids = new ArrayList<UUID>();
      for (TerminalNode idNode : ctx.uuidListStatements().UUID()) {
        ids.add(UUID.fromString(idNode.getText()));
      }
      return ids;
    }
  }
}
