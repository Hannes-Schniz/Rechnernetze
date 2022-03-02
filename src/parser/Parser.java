package src.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Parser.
 *
 * @author Hannes Schniz
 * @version 1.0
 */
public class Parser {
    /**
     * Parse IP to string string.
     *
     * @param input the input
     * @return the string
     */
    public static String parseToString(int[] input) {
        String returnValue = "";
        for (int i : input) {
            returnValue = returnValue.concat(i + ".");
        }
        return returnValue.substring(0, returnValue.length() - 1);
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
        String[] workingValue = input.split("[.]"); //splits the String at the dots
        if (workingValue.length != 4) {
            throw new ParseException("Input does not match the dot pattern");
        }
        for (int i = 0; i < workingValue.length; i++) {
            returnValue[i] = Integer.parseInt(workingValue[i]);
            if (returnValue[i] > 255 || returnValue[i] < 0) { //if the ints are not between 0 and 255
                throw new ParseException("Input does not match the number pattern"); //throws an exception
            }
        }
        return returnValue;
    }

    /**
     * Parse to tree string [ ].
     *
     * @param bracketNotation the bracket notation
     * @return the string [ ]
     */
    public static String[] parseToTree(String bracketNotation) throws ParseException {
        if (bracketNotation.length() < 9) { //if the input is shorter than 9 it'll throw an error immediately
            throw new ParseException("Input too short");
        }
        if (bracketNotation.charAt(0) != '(' || bracketNotation.charAt(bracketNotation.length() - 1) != ')') {
            throw new ParseException("Input doesn't contain necessary brackets");
        }
        String[] working = bracketNotation.split("\s"); //splits the input at " "
        check(working);
        String[] parsedInput = new String[1]; //simulates an unlimited array
        for (int i = 0; i < working.length; i++) {
            int pos = 0;
            if (distance(working[i]) == 1) { //if working[i] has a leading bracket
                int match = findMatching(working, i); //finds the corresponding closing bracket
                if (i > 0) {
                    parsedInput = extend(parsedInput); //extends the unlimited array if i > 0
                }
                parsedInput[parsedInput.length - 1] = crop(working[i]); //removes leading and trailing brackets
                for (int j = i + 1; j < match + 1; j++) { // finds all Ips on the same layer and adds them to the array
                    if (pos == 0) {
                        parsedInput[parsedInput.length - 1] += " " + crop(working[j]);
                    }
                    pos += distance(working[j]);
                }
            }
        }
        return parsedInput;
    }

    private static void check(String[] input) throws ParseException {
        int brackets = 0;
        List<String> contained = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            if (input[i].length() < 9) {
                throw new ParseException("input is too short");
            }
            brackets += distance(input[i]);
            if (i != 0 && input[i].charAt(0) == '(' && input[i].charAt(input[i].length() - 1) == ')') {
                throw new ParseException("contains wrong use of brackets");
            }
            String croppedString = crop(input[i]);
            if (contained.contains(croppedString)) {
                throw new ParseException("IP is not unique");
            }
            if (!croppedString.matches("([0-2][0-9][0-9][.]){3}([0-2][0-9][0-9])")) {
                throw new ParseException("IP does not match IPV4 pattern");
            }
            contained.add(croppedString);
        }
        if (brackets != 0) {
            throw new ParseException("input doesn't match given bracket pattern");
        }

    }

    /**
     * Parse to bracket string.
     *
     * @param input the input
     * @return the string
     */
    public static String parseToBracket(List<String> input) {
        StringBuilder output = new StringBuilder();
        for (String string: input) {
            if (output.toString().equals("")) {
                output.append("(").append(string);
            }
            else {
                output.append(" ").append(string);
            }
        }
        return String.valueOf(output.append(")"));
    }

    /**
     * Point notation string [ ].
     *
     * @param bracketIPs the bracket i ps
     * @return the string [ ]
     * @throws ParseException the parse exception
     */
    public static String[] pointNotation(String bracketIPs) throws ParseException {
        return bracketIPs.split("\s");
    }

    private static String crop(String input) throws ParseException {
        int distance = distance(input); //gets the difference between leading and trailing brackets
        if (distance < 0) { //if it has trailing brackets it cuts them off
            return   input.substring(0, input.length() + distance);
        }
        else if (distance == 0 && input.charAt(0) == '(') { //if distance is 0 and it has a leading bracket cuts off the
            return input.substring(1, input.length() - 1); // and back
        } //else returns the string without aether the leading bracket or as it is
        return input.substring(distance);
    }

    private static String[] extend(String[] input) {
        String[] returnValue = new String[input.length + 1];
        System.arraycopy(input, 0, returnValue , 0, input.length);
        return returnValue;
    }

    private static int distance(String input) throws ParseException {
        int leading = 0;
        int trailing = 0;
        if (input.charAt(0) == '(') { //the input can only have one leading "("
            leading = 1;
        }
        for (int i = input.length() - 1; i > 0; i--) { //finds all trailing ")" and adds them up
            if (input.charAt(i) != ')') {
                trailing = -(input.length() - 1 - i);
                break;
            }
        }
        if (leading == 1 && (input.length() - 1 + trailing) <= -1) { //if it has a leading bracket it can only have one trailing bracket
            throw new ParseException("too many trailing brackets");
        }
        return leading + trailing; //returns difference
    }

    private static int findMatching(String[] input, int start) throws ParseException {
        int pos = 0;
        for (int i = start; i < input.length; i++) { //goes through the whole input
            pos += distance(input[i]); //adds the difference between leading and trailing brackets
            if (pos <= 0) {
                return i; //returns the position of the corresponding bracket when pos falls to or under 0
            }
        }
        return -1;
    }
}
