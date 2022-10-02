package poo.main;

import java.io.*;
import poo.tp.*;
import java.util.HashMap;

public class Main {
  public static void main(String[] args) {
    // Affiche des tests pré-écrits:
    basicTests();
    advancedTests();

    // Récupération d'une expression entrée par l'utilisateur:
    Expression userExpression = askFormula();
    System.out.println(userExpression.toString());
    HashMap<String, Double> assignment = askAssignment();
    System.out.println("resulat:");
    System.out.println(userExpression.eval(assignment));
  }


  /*
   * Permet de récupérer l'entrée utilisateur.
   */
  public static String askString() {
    String str = null;
    try {
      Reader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      str = br.readLine();
    } catch (IOException e) {}
    return str;
  }


  /*
   * Demande à l'utilisateur de saisir une formule et génère une nouvelle
   * expression qu'elle retourne.
   */
  public static Expression askFormula() {
    System.out.println("syntaxe: (+ (* 2 x) (/ y 3))");
    String formulaStr = askString();
    if (formulaStr.length() < 7) { // si la taille de la chaîne est inférieure
                                   // à 7 on est sûr que ce n'est pas une formule valide
      throw new IllegalStateException("Formule non valide");
    }
    Expression formula = new Expression(formulaStr);
    return formula;
  }


  public static HashMap<String, Double> askAssignment() {
    System.out.println("syntaxe: x=3, y=5, theta=60;");
    HashMap<String, Double> assignment = new HashMap<String, Double>();
    String assignmentStr = askString();
    int i = 0;
    boolean stop = false, numerical = false;
    String name = "", value = "";
    
    if (assignmentStr.length() > 3) { // teste si l'assignement est vide
      while (!stop) {
        switch (assignmentStr.charAt(i)) {
          case '=':
            numerical = true; // On ne récupère plus le nom de la variable mais
                              // sa valeur
            break;
          case ';':
            if (!isNumeric(value)) {
              throw new NumberFormatException("Last assigned value is not a number.");
            }
            assignment.put(name, Double.parseDouble(value));
            stop = true;
            break;
          case ',':
            if (!isNumeric(value)) {
              throw new NumberFormatException("Assigned value is not a number.");
            }
            assignment.put(name, Double.parseDouble(value));
            name = "";
            value = "";
            numerical = false;
            break;
          case ' ':
            break;
          default:
            if (numerical) {
              value += assignmentStr.charAt(i);
            } else {
              name += assignmentStr.charAt(i);
            }
            break;
        }
        i++;
      }
    }
    return assignment;
  }


  public static boolean isNumeric(String str) {
    try {
      double d = Double.parseDouble(str);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }


  public static void basicTests() {
    Expression expConst = new Expression(new Const(2.678));
    Expression expVariable = new Expression(new Variable("z"));
    Expression expSum = new Expression(new Sum(expConst, expVariable));
    Expression expMultiplication = new Expression(new Multiplication(expConst, expVariable));
    Expression exp = new Expression(new Division(expSum, expMultiplication));

    System.out.println("=Basic Tests============================");
    System.out.println("========================================");
    System.out.println("tests toString:");
    System.out.println("expSum:");
    System.out.println(expSum.toString());
    System.out.println("expMultiplication:");
    System.out.println(expMultiplication.toString());
    System.out.println("exp:");
    System.out.println(exp.toString());

    System.out.println("========================================");
    System.out.println("assignment:");
    HashMap<String, Double> m = new HashMap<String, Double>(5);
    m.put("z", 12.5);
    m.put("w", 1.56);
    m.put("x", 3.0);
    m.put("y", 6.0);
    m.put("varString", 6.5);
    System.out.println("x=3.0, y=6.0, z=12.5, varString=6.5");

    System.out.println("========================================");
    System.out.println("tests eval:");
    System.out.println("resulat expSum:");
    System.out.println(expSum.eval(m));
    System.out.println("resulat expMultiplication:");
    System.out.println(expMultiplication.eval(m));
    System.out.println("resulat exp:");
    System.out.println(exp.eval(m));

    System.out.println("========================================");
    System.out.println("tests with strings:");
    System.out.println("test string:");
    Expression testStr = new Expression("(+ (- 4.765 z) (* 6.654 w))");
    System.out.println(testStr.toString());
    System.out.println("resulat:");
    System.out.println(testStr.eval(m));
    
    System.out.println("========================================");
    System.out.println("test small string:");
    Expression testStrSmall = new Expression("(+ 2 z)");
    System.out.println(testStrSmall.toString());
    System.out.println("resulat:");
    System.out.println(testStrSmall.eval(m));

    System.out.println("========================================");
    System.out.println("test big string:");
    Expression testStrBig = new Expression("(/ (+ (* 1.2 varString) (/ x 3.7)) (- (/ varString z) (* 5.4 2)))");
    System.out.println(testStrBig.toString());
    System.out.println("resulat:");
    System.out.println(testStrBig.eval(m));

    System.out.println("========================================");
    System.out.println("test tp example:");
    Expression testTp = new Expression("(+ (* 2 x) (/ y 3))");
    System.out.println(testTp.toString());
    System.out.println("resulat:");
    System.out.println(testTp.eval(m));
    System.out.println("========================================");
  }


  public static void advancedTests() {
    HashMap<String, Double> m = new HashMap<String, Double>();
    m.put("x", -8.0);
    m.put("e", 35.0);
    m.put("r", -34.0);
    m.put("hello", -12.0);
    m.put("World", -13.0);
    m.put("toto", 3.8);
    m.put("hjkl", -18.0);

    System.out.println("=Advanced Tests=========================");
    System.out.println("========================================");
    Expression AdvancedTest1 = new Expression("(+ 2 2)");
    System.out.println(AdvancedTest1.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest1.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest2 = new Expression("(- (+ x 3) 6)");
    System.out.println(AdvancedTest2.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest2.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest3 = new Expression("(* (+ 7.2 8) (- 5 -4))");
    System.out.println(AdvancedTest3.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest3.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest4 = new Expression("(* (/ 8 -34) (+ 47 32))");
    System.out.println(AdvancedTest4.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest4.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest5 = new Expression("(+ (* (- 23 45) (+ -43 12)) 4)");
    System.out.println(AdvancedTest5.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest5.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest6 = new Expression("(+ (* (+ (- toto 5.4) 25) (/ 12 -toto)) (* (- 53 hello) (- 32 hello)))");
    System.out.println(AdvancedTest6.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest6.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest7 = new Expression("(* (/ 12.8 23) (+ (* 45 -12.4) (* 5 12)))");
    System.out.println(AdvancedTest7.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest7.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest8 = new Expression("(- (* (+ (- 4 32) (+ -5.2 12)) (- (+ -34 12) (* 2.9 7))) (* (/ 45 -32) (- 12 -32)))");
    System.out.println(AdvancedTest8.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest8.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest9 = new Expression("(+ (- (* 4 (+ -e 3)) (+ (* (/ 45 32) (+ World 24)) (- (* 3 21) (+ 5.4 -32)))) (* (+ 34 World) (/ 32 43)))");
    System.out.println(AdvancedTest9.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest9.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest10 = new Expression("(* (+ (- (/ 4.2 3) (* (+ -4 3) (- 1.2 -7))) (+ 34 -7)) (+ (* -5.2 8) 37))");
    System.out.println(AdvancedTest10.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest10.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest11 = new Expression("(/ (+ (* (- 5 2.8) (+ 5.6 (/ 21 7))) (/ 9 5.8)) (* (+ 65 -32) -75))");
    System.out.println(AdvancedTest11.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest11.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest12 = new Expression("(+ 2.6 -43 )");
    System.out.println(AdvancedTest12.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest12.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest13 = new Expression("(* (+ 7 -4.9) (- 5 (/ 4 7)))");
    System.out.println(AdvancedTest13.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest13.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest14 = new Expression("(+ (* (- (/ (+ 4 7.8) (- 12 9)) (+ (* 6 4.1) 12)) (+ (- (/ 6 2) (* 3.8 -23)) -56)) (* (- (/ 45 9) -34) (+ (- 4 8) 3.6)))");
    System.out.println(AdvancedTest14.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest14.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest15 = new Expression("(* hjkl (+ (* 2 hjkl) (- 6 8.8)))");
    System.out.println(AdvancedTest15.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest15.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest16 = new Expression("(/ 25 (+ (- 34 8) (/ -63 (+ -12 15))))");
    System.out.println(AdvancedTest16.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest16.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest17 = new Expression("(+ (* (/ 6 -3) 12.7) (- 43 (+ 7 8.9)))");
    System.out.println(AdvancedTest17.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest17.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest18 = new Expression("(* 12 (- 2 56))");
    System.out.println(AdvancedTest18.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest18.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest19 = new Expression("(+ 23 (* 6 (- 7 (+ 5 0.9))))");
    System.out.println(AdvancedTest19.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest19.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest20 = new Expression("(/ -9 (+ 34 (* 7 6)))");
    System.out.println(AdvancedTest20.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest20.eval(m));
    System.out.println("========================================");
    Expression AdvancedTest21 = new Expression("(+ (* (- 4 r) (+ 18 (/ 35 r))) (* 3 r))");
    System.out.println(AdvancedTest21.toString());
    System.out.println("resulat:");
    System.out.println(AdvancedTest21.eval(m));
    System.out.println("========================================");
  }
}
