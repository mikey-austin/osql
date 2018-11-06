package net.jackiemclean.osql;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.stringtemplate.v4.ST;
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
    List<AssignmentDisplay> assignments =
        updateStatement
            .getAssignments()
            .stream()
            .map(AssignmentDisplay::fromIRAssignment)
            .collect(toList());
    ST st = clojureTemplGroup.getInstanceOf("updateStmt");
    st.add("assignments", assignments);
    return st.render();
  }

  public static class AssignmentDisplay {
    private final String field;
    private final String value;
    private final AssignmentDisplay child;

    public AssignmentDisplay(String field, String value, AssignmentDisplay child) {
      this.value = value;
      this.child = child;
      this.field = makeAccessor(value != null ? ".set" : ".get", field);
    }

    private String makeAccessor(String prefix, String field) {
      String capField = field.substring(0, 1).toUpperCase() + field.substring(1);
      return prefix + capField;
    }

    public String getField() {
      return field;
    }

    public String getValue() {
      return value;
    }

    public AssignmentDisplay getChild() {
      return child;
    }

    public static AssignmentDisplay fromIRAssignment(IR.UpdateStmt.Assignment assignment) {
      IR.Field field = assignment.getField();
      String expr = assignment.getExpr();

      List<String> path = field.getPath();
      String lastComponent = path.get(path.size() - 1);

      return new AssignmentDisplay(lastComponent, expr, nextComponent(path.size() - 2, path));
    }

    private static AssignmentDisplay nextComponent(int level, List<String> path) {
      if (level < 0) return null;
      return new AssignmentDisplay(path.get(level), null, nextComponent(--level, path));
    }
  }
}
