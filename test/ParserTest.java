package test;

import src.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.Test
    void parseToTree() {
        String[] correct1 = {"85.193.148.81 141.255.1.133 34.49.145.239 231.189.0.127"
                , "141.255.1.133 122.117.67.158 0.146.197.108"
                , "231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77"};
        String[] correct2 = {"85.193.148.81 141.255.1.133 34.49.145.239 231.189.0.127"};
        String[] correct3 = {"85.193.148.81 34.49.145.239 141.255.1.133 231.189.0.127",
            "141.255.1.133 0.146.197.108 122.117.67.158", "231.189.0.127 39.20.222.120 116.132.83.77 252.29.23.0",
            "39.20.222.120 77.135.84.171"};
        String[] correct4 = {"85.193.148.81 141.255.1.133 231.189.0.127", "141.255.1.133 34.49.145.239", "34.49.145.239 231.189.0.127"
                , "231.189.0.127 77.135.84.171", "77.135.84.171 39.20.222.120", "39.20.222.120 252.29.23.0 116.132.83.77"};
        String[] returnString1 = Parser.parseToTree("(85.193.148.81"
                + " (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239"
                + " (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");
        String[] returnString2 = Parser.parseToTree("(85.193.148.81 141.255.1.133 34.49.145.239 231.189.0.127)");
        String[] returnString3 = Parser.parseToTree("(85.193.148.81 34.49.145.239 (141.255.1.133 0.146.197.108"
                + " 122.117.67.158) (231.189.0.127 (39.20.222.120"
                + " 77.135.84.171) 116.132.83.77 252.29.23.0))");
        String[] returnString4 = Parser.parseToTree("(85.193.148.81 (141.255.1.133 (34.49.145.239 231.189.0.127))"
                + " (231.189.0.127 (77.135.84.171 (39.20.222.120 252.29.23.0 116.132.83.77))))");
        System.out.println("case 1");
        for (int i = 0; i < correct1.length; i++) {
            System.out.println("correct: " + correct1[i] + " |Output: " + returnString1[i]);
            assertEquals(correct1[i], returnString1[i]);
        }
        System.out.println("case 2");
        for (int i = 0; i < correct2.length; i++) {
            System.out.println("correct: " + correct2[i] + " |Output: " + returnString2[i]);
            assertEquals(correct2[i], returnString2[i]);
        }
        System.out.println("case 3");
        for (int i = 0; i < correct3.length; i++) {
            System.out.println("correct: " + correct3[i] + " |Output: " + returnString3[i]);
            assertEquals(correct3[i], returnString3[i]);
        }
        System.out.println("case 4");
        for (int i = 0; i < correct4.length; i++) {
            System.out.println("correct: " + correct4[i] + " |Output: " + returnString4[i]);
            assertEquals(correct4[i], returnString4[i]);
        }
    }
}