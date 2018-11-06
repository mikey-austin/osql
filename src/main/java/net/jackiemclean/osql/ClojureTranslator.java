package net.jackiemclean.osql;

import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class ClojureTranslator {
  private static final String CLOJURE_TEMPL_GROUP = "templates/clojure.stg";

  private final STGroup clojureTemplGroup;

  public ClojureTranslator() {
    // Search for the template group in the resources directory.
    String groupFilePath = getClass().getClassLoader().getResource(CLOJURE_TEMPL_GROUP).getFile();
    this.clojureTemplGroup = new STGroupFile(groupFilePath);
  }

  public String translate(IR.UpdateStmt updateStatement) {
    return "";
  }
}
