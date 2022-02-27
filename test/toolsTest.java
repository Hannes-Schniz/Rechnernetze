package test;

import org.junit.jupiter.api.Test;
import src.parser.ParseException;
import src.structure.*;
import src.helper.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class toolsTest {

    @Test
    void copyTrees() throws ParseException {
         Network testNetwork = new Network("(85.193.148.81"
                + " (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239"
                + " (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");

         List<Node> newlist = tools.copyTrees(testNetwork.getTrees());
         return;



    }
}