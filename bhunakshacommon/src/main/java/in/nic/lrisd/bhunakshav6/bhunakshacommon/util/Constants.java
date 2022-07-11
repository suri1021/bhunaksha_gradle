package in.nic.lrisd.bhunakshav6.bhunakshacommon.util;

public enum Constants {
    PLOT_AT_XY_EXTRA_FILTER_CQL("PLOT_AT_XY_EXTRA_FILTER_CQL");

    public static final String FORWARD = "F";
    public static final String SENDBACK = "S";
    public static final String APPROVE = "A";

    public final String code;

    Constants(String code) {
        this.code = code;
    }
}
