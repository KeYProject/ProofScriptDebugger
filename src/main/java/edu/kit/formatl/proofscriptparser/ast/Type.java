
/**
 * Created by sarah on 4/30/17.
 */
public enum Type {
    bool, string, term, integer, any
    INT("int"), BOOL("bool");

    private final String symbol;

    Type(String symbol) {
        this.symbol = symbol;
    }

    public  String symbol() {
        return symbol;
    }
}
