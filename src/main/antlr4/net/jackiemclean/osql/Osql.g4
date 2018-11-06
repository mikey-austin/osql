grammar Osql;

osql
    : statements EOL* EOF
    ;

statements
    : statement (statementSep* statement)*
    ;

statementSep
    : EOS
    | EOL
    ;

// TODO: add more types of statements, like SELECT, etc.
statement
    : updateStatement
    | EOL
    ;

updateStatement
    : 'UPDATE' world 'SET' setStatements 'WHERE' updateWhere
    ;

world
    : SYMBOL # worldName
    ;

setStatements
    : EOL* setStatement (',' EOL* setStatement)* EOL*
    ;

setStatement
    : fieldPath '=' fieldExpr
    ;

fieldPath
    : SYMBOL ('.' SYMBOL)*
    ;

// TODO: add more sophisticated types and function calls below.
fieldExpr
    : INTEGER # intFieldExpr
    | STRING  # strFieldExpr
    | UUID    # uuidFieldExpr
    ;

// TODO: this is pretty basic.
updateWhere
    : orderClause
    | orderClause 'AND' orderObjectClause
    ;

orderClause
    : 'orderId' '=' UUID      # orderIdSingle
    | 'orderId' 'IN' uuidList # orderIdList
    ;

orderObjectClause
    : 'orderObjectId' '=' UUID      # orderObjectIdSingle
    | 'orderObjectId' 'IN' uuidList # orderObjectIdList
    ;

uuidList
    : '(' EOL* uuidListStatements EOL* ')'
    ;

uuidListStatements
    : UUID (',' EOL* UUID)*
    ;

//
// Token definitions.
//

UUID
    : BLOCK BLOCK '-' BLOCK '-' BLOCK '-' BLOCK '-' BLOCK BLOCK BLOCK
    ;
fragment BLOCK: [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F];

INTEGER
    : '-'?[0-9]+
    ;

SYMBOL
    : [a-zA-Z0-9_-]+
    ;

COMMENT
    : '--' ~[\r\n]* -> skip
    ;

STRING
    : '"' ~('\r' | '\n' | '"')* '"'
    ;

EOS
    : ';'
    ;

EOL
    : ('\r'?'\n')
    ;

WHITESPACE
    : ('\t' | ' ' | '\r' | '\u000C')+ -> skip
    ;
