package one.june.apimock.utils;

public class RegexPatterns {
    public static final String ALPHANUMERIC = "[a-zA-Z0-9]+";
    public static final String BOOLEAN = "(true|false)";
    public static final String INTEGER = "[0-9]+";
    public static final String NUMBER = "[0-9]+(.[0-9]+)?";
    public static final String UUID = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    public static final String ALL = ".*";

    public static final String PATH_PARAMETER_PATTERN = "^\\{.*\\}$";
}
