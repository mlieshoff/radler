package radler.persistence.db;

/**
 * This ...
 *
 * @author mlieshoff
 */
public enum DbMode {

    H2;

    public static DbMode fromConnectionUrl(String url) {
        if (url.contains(":h2:")) {
            return H2;
        }
        throw new UnsupportedOperationException("db is not supported: " + url);
    }

}
