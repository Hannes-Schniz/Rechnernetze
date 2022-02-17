package test;

import org.junit.Assert;
import src.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.Test
    void parseToTree() {
        String[] correct = {"85.193.148.81 141.255.1.133 34.49.145.239 231.189.0.127"
                , "141.255.1.133 122.117.67.158 0.146.197.108"
                , "231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77"};
        String[] returnString = Parser.parseToTree("(85.193.148.81"
                + " (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239"
                + " (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");
        for (int i = 0; i < correct.length; i++) {
            assertEquals(correct[i], returnString[i]);
        }
    }
}