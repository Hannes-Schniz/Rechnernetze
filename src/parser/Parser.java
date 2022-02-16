package src.parser;

public class Parser {
    public static String parseToString(int[] input) {
        String returnValue = "";
        for (int i : input) {
            returnValue = returnValue.concat(i + ".");
        }
        return returnValue;
    }

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

   // public static String[] parseToPoint(String input) {
   //     String working = input.substring(1, input.length() - 2);
   //     String[] returnValue = working.split()
   // }
}
