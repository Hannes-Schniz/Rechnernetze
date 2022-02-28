package src.structure;

import src.parser.ParseException;
import src.parser.Parser;

import java.util.Arrays;

/**
 * The type Ip.
 *
 * @author Hannes Schniz
 * @version 1.0
 */
public class IP implements Comparable<IP> {

    private final int[] address;

    /**
     * Instantiates a new Ip.
     *
     * @param pointNotation the point notation
     * @throws ParseException the parse exception
     */
    public IP(final String pointNotation) throws ParseException {
        this.address = Parser.parseToAddress(pointNotation);
    }

    @Override
    public String toString() {
        return Parser.parseToString(address).substring(0, Parser.parseToString(address).length() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IP ip = (IP) o;
        return Arrays.equals(address, ip.address);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(address);
    }

    @Override
    public int compareTo(IP o) {
        int[] toCompare = o.getAddress();
        if (this.equals(o)) {
            return 0;
        }
        for (int i = 0; i < address.length; i++) {
            if (address[i] < toCompare[i]) {
                return -1;
            }
            else if (address[i] > toCompare[i]) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Get address int [ ].
     *
     * @return the int [ ]
     */
    public int[] getAddress() {
        return address;
    }
}
