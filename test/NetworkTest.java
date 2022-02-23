package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.parser.ParseException;
import src.structure.IP;
import src.structure.Network;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NetworkTest {

    private Network testNetwork;

    private Network addNetwork;

    private Network stress;

    @BeforeEach
    void setUp() {
        try {
            testNetwork = new Network("(85.193.148.81"
                   + " (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239"
                    + " (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");
            addNetwork = new Network("(85.193.148.81 111.111.111.111)");
            stress = new Network("(85.193.148.81 34.49.145.239 (141.255.1.133 0.146.197.108"
                    + " 122.117.67.158) (231.189.0.127 (39.20.222.120"
                    + " 77.135.84.171) 116.132.83.77 252.29.23.0))");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void list() {
        List<IP> networkList = testNetwork.list();
        for (IP ip: networkList) {
            System.out.println(ip.toString());
        }
    }

    @Test
    void add() {
        testNetwork.add(addNetwork);
        testNetwork.add(stress);
        List<IP> networkList = testNetwork.list();
        for (IP ip: networkList) {
            System.out.println(ip.toString());
        }
    }

    @Test
    void connect() {
    }

    @Test
    void disconnect() {
    }

    @Test
    void contains() {
    }

    @Test
    void getHeight() {
    }

    @Test
    void getLevels() {
    }

    @Test
    void getRoute() {
    }

    @Test
    void testToString() {
    }
}