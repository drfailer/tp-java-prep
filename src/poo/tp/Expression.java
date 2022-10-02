package poo.tp;

import java.util.HashMap;

public class Expression {
  private Node root;


  public Expression(Node root) {
    this.root = root;
  }


  public Expression(String expression) {
    Expression left;
    Expression right;
    String[] expressionParts = new String[2];
    expressionParts[0] = "";
    expressionParts[1] = "";
    char sign = expression.charAt(1); // Si il n'y a pas d'espaces inutiles
                                      // dans la string, le signe de la formule
                                      // est le second caractère 
    expression.replace("\\s+", " "); // remplace des espaces multiples par des simples
                                     // si l'utilisateur en a rajouté par erreur
    this.getBinaryExpressionFromString(expression, expressionParts);

    if (expressionParts[0] == "" && expressionParts[1] == "") {
      // Cas ou l'expression est de la forme: (. x y):
      extractLitteralFromBinaryExpression(expression, expressionParts);
      left = createExpressionFromLitteral(expressionParts[0]);
      right = createExpressionFromLitteral(expressionParts[1]);
    } else if (expressionParts[0] == "") {
      // Cas ou l'expression est de la forme: (. (...) x):
      right = new Expression(expressionParts[1]);
      left = createExpressionFromLitteral(expression.split("\\s+")[1]);
    } else if (expressionParts[1] == "") {
      // Cas ou l'expression est de la forme: (. x (...)):
      String expressionEnd = expression.substring(expression.lastIndexOf(' ')+1);
      expressionParts[1] = expressionEnd.substring(0, expressionEnd.indexOf(')'));
      right = createExpressionFromLitteral(expressionParts[1]);
      left = new Expression(expressionParts[0]);
    } else { // expression est une expression binaires composée de deux
             // expressions gauche et droite: (. (...) (...))
      left = new Expression(expressionParts[0]);
      right = new Expression(expressionParts[1]);
    }
    this.root = createOperation(sign, left, right);
  }


  /*
   * Récupère les parties gauche et droite d'une expression binaire et les
   * met dans expressionParts.
   *
   * Les partie gauche et droite de l'expression doivent elles aussi être des
   * expression binaires (pas de constante ou de variable). Si ce n'est pas le
   * cas, expressionParts contiendra deux chaînes vides à la fin de
   * l'exécution.
   */
  private void getBinaryExpressionFromString(String expression, String[] expressionParts) {
    int formulaLevel = 0;
    int i = expression.indexOf('(', 1);
    // Récupération de la partie gauche:
    if (i > 0) {
      do {
        if (expression.charAt(i) == '(') {
          formulaLevel++;
        } else if (expression.charAt(i) == ')') {
          formulaLevel--;
        }
        expressionParts[0] += expression.charAt(i);
        i++;
      } while (formulaLevel > 0 && i < expression.length());
      i = expression.indexOf('(', i);
      if (i > 0) { // si i > 0, l'expression est composée de deux expression binaires
        expressionParts[1] = expression.substring(i);
      } else if (expression.charAt(3) != '(') {
        /* Si i < 0, on regarde si la chaîne de caractère récupérée dans
         * expressionParts[0] est la partie gauche de l'expression, ie le
         * caractère en 3ème position dans la chaîne est '(':
         * - expression = (. (...) var), le troisième char est '(' est la partie
         * gauche de expression est une expression binaire et la droite est un
         * littéral
         * - expression = (. var (...)), le troisième char n'est par '(' dans
         * la partie gauche est un littéral et la partie droite une expression
         * binaire
         */
        expressionParts[1] = expressionParts[0];
        expressionParts[0] = "";
      }
    }
  }


  /*
   * Récupère les parties gauche et droite de l'expression et les met dans
   * expressionParts lorsque celles-ci sont des variables ou des constantes.
   */
  private void extractLitteralFromBinaryExpression(String expression, String[] expressionParts) {
    // il faut supprimer les ')' à la fin pour ne pas les récupérer dans expressionParts[1]:
    String[] parts = expression.replace(')', ' ').split("\\s+"); // split avec 1 ou plusieurs espaces
    expressionParts[0] = parts[1];
    expressionParts[1] = parts[2];
  }


  /*
   * Créé une variable ou une constante depuis une string.
   */
  private Expression createExpressionFromLitteral(String litt) {
    if (this.isNumeric(litt)) {
      return new Expression(new Const(Double.parseDouble(litt)));
    } else {
      return new Expression(new Variable(litt));
    }
  }


  /*
   * Teste si un string contient un double, retourne true si oui.
   */
  private boolean isNumeric(String str) {
    try {
      double d = Double.parseDouble(str);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }


  /*
   * Créé un nouveau noeud à partir de deux parties gauches et droites d'une
   * expression. Ce nouveau noeud sera retourné à la fin de l'exécution et
   * son type dépend de sign donné en paramètre.
   */
  private Node createOperation(char sign, Expression left, Expression right) {
    Node output;

    switch (sign) {
      case '+':
        output = new Sum(left, right);
        break;
      case '-':
        output = new Substraction(left, right);
        break;
      case '*':
        output = new Multiplication(left, right);
        break;
      case '/':
        output = new Division(left, right);
        break;
      default:
        throw new IllegalArgumentException("L'expression n'est pas valide: erreur de signe.");
    }

    return output;
  }


  public double eval(HashMap<String, Double> assignment) {
    return this.root.eval(assignment);
  }


  @Override
  public String toString() {
    return this.root.toString();
  }
}
