package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculatorController {

    StringBuilder expression = new StringBuilder();
    List<Character> OPERATORS = Arrays.asList('+', '-', '*', '/');



    @FXML
    private TextField display;

    @FXML
    private Button one;

    @FXML
    private Button two;

    @FXML
    private Button three;

    @FXML
    private Button four;

    @FXML
    private Button five;

    @FXML
    private Button six;

    @FXML
    private Button seven;

    @FXML
    private Button eight;

    @FXML
    private Button nine;

    @FXML
    private Button zero;

    @FXML
    private Button clear;

    @FXML
    private Button plus;

    @FXML
    private Button minus;

    @FXML
    private Button mult;

    @FXML
    private Button div;

    @FXML
    private Button unary;

    @FXML
    private Button point;

    @FXML
    void handleButtonPress(ActionEvent event) {
        if (event.getSource() == one) {
            expression.append('1');
            display.setText(display.getText() + '1');
        } else if (event.getSource() == two) {
            expression.append('2');
            display.setText(display.getText() + '2');
        } else if (event.getSource() == three) {
            expression.append('3');
            display.setText(display.getText() + '3');
        } else if (event.getSource() == four) {
            expression.append('4');
            display.setText(display.getText() + '4');
        } else if (event.getSource() == five) {
            expression.append('5');
            display.setText(display.getText() + '5');
        } else if (event.getSource() == six) {
            expression.append('6');
            display.setText(display.getText() + '6');
        } else if (event.getSource() == seven) {
            expression.append('7');
            display.setText(display.getText() + '7');
        } else if (event.getSource() == eight) {
            expression.append('8');
            display.setText(display.getText() + '8');
        } else if (event.getSource() == nine) {
            expression.append('9');
            display.setText(display.getText() + '9');
        } else if (event.getSource() == zero) {
            expression.append('0');
            display.setText(display.getText() + '0');
        } else if (event.getSource() == clear) {
            expression = new StringBuilder();
            display.setText("");
        } else if (event.getSource() == plus) {
            if (!isExpressionEmpty() && canPlaceOperator()) {
                expression.append('+');
                display.setText(display.getText() + '+');
            }
        } else if (event.getSource() == minus) {
            if (isExpressionEmpty() || canPlaceOperator()) {
                expression.append('-');
                display.setText(display.getText() + '-');
            }
        } else if (event.getSource() == mult) {
            if (!isExpressionEmpty() && canPlaceOperator()) {
                expression.append('*');
                display.setText(display.getText() + '*');
            }
        } else if (event.getSource() == div) {
            if (!isExpressionEmpty() && canPlaceOperator()) {
                expression.append('/');
                display.setText(display.getText() + '/');
            }
        } else if (event.getSource() == point) {
            if (isExpressionEmpty() || (expression.length() == 1 && expression.charAt(0) == '-')) {
                expression.append("0.");
                display.setText(display.getText() + "0.");
            } else {
                if (canPlaceDecimal()) {
                    expression.append('.');
                    display.setText(display.getText() + '.');
                }
            }
        } else if (event.getSource() == unary) {
            String s = expression.toString();
            if (!isExpressionEmpty() && isNumeric(s)) {
                double number = Double.parseDouble(s) * -1;
                if (number < 0) {
                    expression.insert(0, '-');
                    display.setText("-" + display.getText());
                } else {
                    expression.deleteCharAt(0);
                    display.setText(display.getText().substring(1));
                }
            }
        } else {
            if (expression.length() != 0) {
                double result = eval(expression.toString());
                if (result == Double.POSITIVE_INFINITY)
                    display.setText("Cannot divide by zero");
                else
                    display.setText(Double.toString(result));
            }
        }
    }

    private boolean canPlaceDecimal() {
        if (expression.charAt(expression.length()-1) == '.') {
            return false;
        }
        StringBuilder number = new StringBuilder();
        for (int i = expression.length()-1; i >= 0; i--) {
             if (OPERATORS.contains(expression.charAt(i)))
                 break;
             number.insert(0, expression.charAt(i));
        }
        return isNumeric(number.toString()) && number.indexOf(".") == -1;
    }

    private boolean canPlaceOperator() {
        return !OPERATORS.contains(expression.charAt(expression.length()-1));
//        for (char operator : OPERATORS) {
//            if (expression.charAt(expression.length()-1) == operator)
//                return false;
//        }
//        return true;
    }

    private boolean isExpressionEmpty() {
        return expression.length() == 0;
    }

    private double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                return parseExpression();
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | number
            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+'))
                        x += parseTerm();
                    else if (eat('-'))
                        x -= parseTerm();
                    else
                        return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if (eat('*'))
                        x *= parseFactor();
                    else if (eat('/'))
                        x /= parseFactor();
                    else
                        return x;
                }
            }

            private double parseFactor() {
                double x = 0;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.')
                        nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                }
                return x;
            }
        }.parse();
    }

    private boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
