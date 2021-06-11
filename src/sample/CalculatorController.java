package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CalculatorController {

    ArrayList<Character> equation = new ArrayList<>();

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
    private Button openP;

    @FXML
    private Button closeP;

    @FXML
    private Button unary;

    @FXML
    private Button point;

    @FXML
    void handleButtonPress(ActionEvent event) {
        if (event.getSource() == one) {
            equation.add('1');
            display.setText(display.getText() + '1');
        } else if (event.getSource() == two) {
            equation.add('2');
            display.setText(display.getText() + '2');
        } else if (event.getSource() == three) {
            equation.add('3');
            display.setText(display.getText() + '3');
        } else if (event.getSource() == four) {
            equation.add('4');
            display.setText(display.getText() + '4');
        } else if (event.getSource() == five) {
            equation.add('5');
            display.setText(display.getText() + '5');
        } else if (event.getSource() == six) {
            equation.add('6');
            display.setText(display.getText() + '6');
        } else if (event.getSource() == seven) {
            equation.add('7');
            display.setText(display.getText() + '7');
        } else if (event.getSource() == eight) {
            equation.add('8');
            display.setText(display.getText() + '8');
        } else if (event.getSource() == nine) {
            equation.add('9');
            display.setText(display.getText() + '9');
        } else if (event.getSource() == zero) {
            equation.add('0');
            display.setText(display.getText() + '0');
        } else if (event.getSource() == clear) {
            equation.clear();
            display.setText("");
        } else if (event.getSource() == plus) {
            if (isValidInput() && !isInputEmpty()) {
                equation.add('+');
                display.setText(display.getText() + '+');
            }
        } else if (event.getSource() == minus) {
            if (isValidInput() && !isInputEmpty()) {
                equation.add('-');
                display.setText(display.getText() + '-');
            }
        } else if (event.getSource() == mult) {
            if (isValidInput() && !isInputEmpty()) {
                equation.add('*');
                display.setText(display.getText() + '*');
            }
        } else if (event.getSource() == div) {
            if (isValidInput() && !isInputEmpty()) {
                equation.add('/');
                display.setText(display.getText() + '/');
            }
        } else if (event.getSource() == openP) {
            equation.add('(');
                display.setText(display.getText() + '(');
        } else if (event.getSource() == closeP) {
            equation.add(')');
                display.setText(display.getText() + ')');
        } else if (event.getSource() == point) {
            if (isInputEmpty()) {
                equation.add('0');
                equation.add('.');
                display.setText(display.getText() + "0.");
            } else if (!equation.contains('.') && isValidInput()) {
                equation.add('.');
                display.setText(display.getText() + ".");
            }
        } else if (event.getSource() == unary) {
            // convert Character array to String
            String s = equation.stream().map(Object::toString).collect(Collectors.joining());
            if (!isInputEmpty() && isNumeric(s) && s.indexOf('-') == -1) {
                equation.add(0, '-');
                display.setText("-" + display.getText());
            }
        } else {
            if (equation.size() != 0) {
                double result = eval(equation.toString());
                if (result == Double.POSITIVE_INFINITY)
                    display.setText("Cannot divide by zero");
                else
                    display.setText(Double.toString(result));
            }
        }
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
                if ((ch >= '0' && ch <= '9') || ch == '.') {
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

    private boolean isInputEmpty() {
        return equation.size() == 0;
    }

    private boolean isValidInput() {
        return equation.get(0) != '+' && equation.get(0) != '-' && equation.get(0) != '*' && equation.get(0) != '/';
    }
}
