package test;

import org.junit.jupiter.api.Test;
import src.parser.ParseException;
import src.structure.IP;

import static org.junit.jupiter.api.Assertions.*;

class IPTest {

    @Test
    void compareTo() {
        try {
            IP ip1 = new IP("000.000.000.000");
            IP ip2 = new IP("000.000.000.000");
            System.out.println(ip1.equals(ip2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}