package matrix.domain.wolframalpha;

import matrix.Main;
import matrix.domain.matrix.Expression;
import matrix.domain.matrix.Matrix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class MatrixToWolframalphaConverter {
    static public String convert(Matrix matrix) {
        String result = "{";
        int i = 0;
        for (Expression[] row : matrix.getExpression()) {
            if (i != 0) {
                result += ",";
            }
            result += "{";
            int j = 0;
            for (Expression field : row) {
                if (j != 0) {
                    result += ",";
                }
                result += field instanceof Expression ? field.calculate() : "";
                j++;
            }
            result += "}";
            i++;
        }
        result += "}";
        return result;
    }

    static public String convertToSystemOfEquations(Matrix matrix) {
        String result = "{";
        int i = 0;
        for (Expression[] row : matrix.getExpression()) {
            if (i != 0) {
                result += ",";
            }
            result += "{";
            int j = 0;
            boolean zeroBefore = true;
            boolean onlyZeroBefore = true;
            for (Expression field : row) {
                int part = 0;
                String variablePart = "";
                if (field instanceof Expression) {
                    Pattern patternToFindNumberPart = Pattern.compile("-*\\d*", Pattern.CASE_INSENSITIVE);

                    Matcher matcherNumberPart = patternToFindNumberPart.matcher(field.calculate());
                    if (matcherNumberPart.find()) {
                        for (int k = 0; k <= matcherNumberPart.groupCount(); k++) {
                            String group = matcherNumberPart.group(k);
                            if (group.equals("-")) {
                                part = -1;
                            } else if (group.equals("")) {
                                part = 1;
                            } else {
                                part = Integer.parseInt(group);
                            }
                        }

                    }

                    Pattern patternVariablePart = Pattern.compile(".*[a-zA-Z]*.*");
                    Matcher matcherVariablePart = patternVariablePart.matcher(field.calculate());
                    if (matcherVariablePart.find()) {
                        for (int k = 0; k <= matcherVariablePart.groupCount(); k++) {
                            String group = matcherVariablePart.group(k);
                            variablePart = group;
                        }
                    }
                } else {
                    part = 0;
                }

                if (part == 0 && zeroBefore) {
                    zeroBefore = true;
                }
                if (part != 0) {
                    onlyZeroBefore = false;
                }

                if (j == Main.getMatrixGridWidth() - 1) {
                    result += "=";
                } else {
                    if (part != 0 && !(part < 0) && part != 0 && !zeroBefore) {
                        result += "+";
                    }
                }
                if (part == 1 || part == -1) {
                    if (j == Main.getMatrixGridWidth() - 1) {
                        result += field instanceof Expression ? field.calculate() : "";
                    } else if (!variablePart.equals("")) {
                        result += variablePart;
                    }
                } else if (part < 0) {
                    result += field instanceof Expression ? field.calculate() : "";
                } else {
                    if (!(j < Main.getMatrixGridWidth() - 1 && part == 0)) {
                        result += field instanceof Expression ? field.calculate() : "";
                    }
                }
                if (j != Main.getMatrixGridWidth() - 1) {
                    if (part != 0) {
                        if (part < 0) {
                            result += Main.getVariables()[j];
                        } else if (part != 0) {
                            result += Main.getVariables()[j];
                        }
                    }
                }
                if (onlyZeroBefore == true && j == Main.getMatrixGridWidth() - 2) {
                    result += 0;
                }
                j++;
                if (part != 0 && zeroBefore) {
                    zeroBefore = false;
                }
            }
            result += "}";
            i++;
        }
        result += "}";
        return result;
    }
}
