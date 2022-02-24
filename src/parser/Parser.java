package src.parser;

import java.util.List;

/**
 * The type Parser.
 * @author Hannes Schniz
 * @version 1.0
 */
public class Parser {
    /**
     * Parse to string string.
     *
     * @param input the input
     * @return the string
     */
    public static String parseToString(int[] input) {
        String returnValue = "";
        for (int i : input) {
            returnValue = returnValue.concat(i + ".");
        }
        return returnValue;
    }

    /**
     * Parse to address int [ ].
     *
     * @param input the input
     * @return the int [ ]
     * @throws ParseException the parse exception
     */
    public static int[] parseToAddress(String input) throws ParseException {
        int[] returnValue = new int[4];
        String[] workingValue = input.split("[.]");
        if (workingValue.length != 4) {
            throw new ParseException("Input does not match the dot pattern");
        }
        for (int i = 0; i < workingValue.length; i++) {
            returnValue[i] = Integer.parseInt(workingValue[i]);
            if (returnValue[i] > 255 || returnValue[i] < 0) {
                throw new ParseException("Input does not match the number pattern");
            }
        }
        return returnValue;
    }

    public static String[] parseToTree(String bracketNotation) {
        String[] working = bracketNotation.split("\s");
        String[] parsedInput = new String[1];
        for (int i = 0; i < working.length; i++) {
            int pos = 0;
            if (distance(working[i]) == 1) {
                int match = findMatching(working, i);
                if (i > 0) {
                    parsedInput = extend(parsedInput);
                }
                parsedInput[parsedInput.length - 1] = crop(working[i]);
                for (int j = i + 1; j < match + 1; j++) {
                    if (pos == 0) {
                        parsedInput[parsedInput.length - 1] += " " + crop(working[j]);
                    }
                    pos += distance(working[j]);
                }
            }
        }
        return parsedInput;
    }

    public static String parseToBracket(List<String> input) {

    }

    public static String[] pointNotation(String bracketIPs) throws ParseException {
        String[] returnString = bracketIPs.split("\s");
        return returnString;
    }

    private static String crop(String input) {
        int distance = distance(input);
        if (distance < 0) {
            return input.substring(0, input.length() + distance);
        }
        return input.substring(distance);
    }

    private static String[] extend(String[] input) {
        String[] returnValue = new String[input.length + 1];
        System.arraycopy(input, 0, returnValue , 0, input.length);
        return returnValue;
    }

    private static int distance(String input) {
        if (input.charAt(0) == '(') {
            return 1;
        }
        else {
            for (int i = input.length() - 1; i > 0; i--) {
                if (input.charAt(i) != ')') {
                    return -(input.length() - 1 - i);
                }
            }
        }
        return 0;
    }

    private static int findMatching(String[] input, int start) {
        int pos = 0;
        for (int i = start; i < input.length; i++) {
            pos += distance(input[i]);
            if (pos <= 0) {
                return i;
            }
        }
        return -1;
    }
}
