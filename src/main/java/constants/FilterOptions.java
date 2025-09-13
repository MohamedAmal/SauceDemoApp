package constants;

public enum FilterOptions {
    NAME_A_TO_Z(0),
    NAME_Z_TO_A(1),
    PRICE_LOW_TO_HIGH(2),
    PRICE_HIGH_TO_LOW(3);

    private final int index;

    FilterOptions(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}