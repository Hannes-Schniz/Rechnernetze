package src.address;

public class IP implements Comparable<IP> {

    private int[] address;

    public IP(final String pointNotation) throws ParseException {
        this.address = Parser.parseToAddress(pointNotation);
    }

    @Override
    public String toString() {
        return Parser.parseToString(address);
    }

    @Override
    public int compareTo(IP o) {
        int[] toCompare = o.getAddress();
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

    public int[] getAddress() {
        return address;
    }
}
