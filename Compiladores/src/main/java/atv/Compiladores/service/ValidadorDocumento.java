package atv.Compiladores.service;
import org.springframework.stereotype.Service;

@Service
public class ValidadorDocumento {

    private int sumRg = 0;
    private int poundRg = 2;

    public boolean run(final String document, final String type) {
        try {
            return isValidDocument(document, type);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isValidDocument(String document, String type) {
        if (type.equalsIgnoreCase("rg")) {
            return isValidRG(document);
        } else {
            return isValidCpf(document);
        }
    }

    public boolean isValidRG(String document) {
        sumRg = 0;
        poundRg = 2;
        State currentState = State.Q0;
        boolean hasFirstPoint = false;
        boolean hasSecondPoint = false;
        boolean hasFinalScore = false;

        for (char c : document.toCharArray()) {
            switch (currentState) {
                case Q0:
                    currentState = (Character.isDigit(c)) ? State.Q1 : State.ERROR;
                    incrementRgSum(c);
                    break;
                case Q1:
                    currentState = (Character.isDigit(c)) ? State.Q2 : State.ERROR;
                    incrementRgSum(c);
                    break;
                case Q2:
                    if (Character.isDigit(c)) {
                        currentState = State.Q3;
                        incrementRgSum(c);
                    } else if (c == '.' && !hasFirstPoint) {
                        currentState = State.Q2;
                        hasFirstPoint = true;
                    } else {
                        currentState = State.ERROR;
                    }
                    break;
                case Q3:
                    currentState = (Character.isDigit(c)) ? State.Q4 : State.ERROR;
                    incrementRgSum(c);
                    break;
                case Q4:
                    currentState = (Character.isDigit(c)) ? State.Q5 : State.ERROR;
                    incrementRgSum(c);
                    break;
                case Q5:
                    if (Character.isDigit(c)) {
                        currentState = State.Q6;
                        incrementRgSum(c);
                    } else if (c == '.' && !hasSecondPoint && hasFirstPoint) {
                        currentState = State.Q5;
                        hasSecondPoint = true;
                    } else {
                        currentState = State.ERROR;
                    }
                    break;
                case Q6:
                    currentState = (Character.isDigit(c)) ? State.Q7 : State.ERROR;
                    incrementRgSum(c);
                    break;
                case Q7:
                    currentState = (Character.isDigit(c)) ? State.Q8 : State.ERROR;
                    incrementRgSum(c);
                    break;
                case Q8:
                    if (Character.isDigit(c)) {
                        currentState = State.Q9;
                    } else if (c == '-' && !hasFinalScore && hasSecondPoint) {
                        currentState = State.Q8;
                        hasFinalScore = true;
                    } else {
                        currentState = State.ERROR;
                    }
                    break;
                default:
                    currentState = State.ERROR;
            }

            if (currentState == State.ERROR) {
                break;
            }
        }

        if (hasSecondPoint && !hasFinalScore) {
            return false;
        }

        if (currentState == State.Q9) {
            int dv = (sumRg % 11 == 0) ? 0 : 11 - (sumRg % 11);

            if (dv == 10 && String.valueOf(document.charAt(8)).equalsIgnoreCase("x")) {
                return true;
            } else if (dv == Integer.parseInt(String.valueOf(document.charAt(document.length() - 1)))) {
                return true;
            }
        }
        return false;
    }

    public void incrementRgSum(char c) {
        sumRg += Integer.parseInt(String.valueOf(c)) * poundRg;
        poundRg++;
    }

    public boolean isValidCpf(String cpf) {
        boolean hasSignals = false;
        int currentState = State.Q0.ordinal();
        int index = 10;
        int firstDigitSum = 0;
        int secondDigitSum = 0;
        int firstCheck = -1, secondCheck = -1;

        for (char c : cpf.toCharArray()) {
            switch (currentState) {
                case 0, 1, 2, 4, 5, 6, 8, 9, 10, 12, 13 -> {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                }
                case 3 -> {
                    if (c == '.') {
                        hasSignals = true;
                    } else if (!Character.isDigit(c) && c != '.') {
                        return false;
                    }
                }
                case 7 -> {
                    if (!(Character.isDigit(c) && !hasSignals) && !(c == '.' && hasSignals)) {
                        return false;
                    }
                }
                case 11 -> {
                    if (c != '-') {
                        return false;
                    }
                }
                default -> {
                    return false;
                }
            }

            if (Character.isDigit(c)) {
                int num = Character.getNumericValue(c);

                if ((!hasSignals && currentState < 9) || (hasSignals && currentState < 12)) {
                    firstDigitSum += num * index;
                    secondDigitSum += num * (index + 1);
                    index--;
                }

                if ((!hasSignals && currentState == 9) || (hasSignals && currentState == 12)) {
                    firstCheck = (firstDigitSum % 11) < 2 ? 0 : 11 - (firstDigitSum % 11);
                    if (num != firstCheck) {
                        return false;
                    }
                }

                if ((!hasSignals && currentState == 10) || (hasSignals && currentState == 13)) {
                    secondDigitSum += firstCheck * 2;
                    secondCheck = (secondDigitSum % 11) < 2 ? 0 : 11 - (secondDigitSum % 11);
                    if (num != secondCheck) {
                        return false;
                    }
                }
            }

            currentState++;
        }

        return currentState == (hasSignals ? 14 : 11);
    }

    private enum State {
        Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, ERROR
    }

}
