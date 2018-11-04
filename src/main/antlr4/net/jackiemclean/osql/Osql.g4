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
    : setStatement (',' EOL* setStatement)*
    ;

setStatement
    : fieldPath '=' fieldExpr
    ;

fieldPath
    : SYMBOL ('.' fieldPath)*
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
    : 'orderId' '=' UUID
    | 'orderId' 'IN' uuidList
    ;

orderObjectClause
    : 'orderObjectId' '=' UUID
    | 'orderObjectId' 'IN' uuidList
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
