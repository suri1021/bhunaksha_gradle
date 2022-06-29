package in.nic.lrisd.bhunakshav6.bhunakshacommon.bean;

public enum LayerCodes {

    VILLAGE_MAP("VILLAGE_MAP", "Village Map (Only plots)"),
    VILLAGE_MAP_WITH_LAYERS("VILLAGE_MAP_WITH_LAYERS", "Village Map with layers overlayed"),
    VILLAGE_BOUNDARIES("VILLAGE_BOUNDARIES", "Village boundaries georeferenced"),
    PLOT_LIST("PLOT_LIST", "List of plots supplied in query"),
    SAME_OWNER_PLOT_LIST("SAME_OWNER_PLOT_LIST", "Same owner plot list"),
    OVERLAY_LAYER("OVERLAY_LAYER", "Overlay Layers,Synatx OVERLAY_LAYERS"),
    FMB_LAYERS("FMB_LAYERS", "FMB Layers"),
    MOSAICED_FMB_SUBDIV("MOSAICED_FMB_SUBDIV", "Mosaiced FMB Subdivision Layers"),
    THEME("THEME", "Themes ,Synatx THEME"),
    ETS_LAYERS("ETS_LAYERS", "ETS Layers"),
    DERIVED_LAYER("DERIVED_LAYER", "Derived Layers,Syntax DERIVED_LAYERS"),
    DYNA_REFERENCED_LAYERS("DYNA_REFERENCED_LAYERS", "Dyna Referenced Layers"),
    VILLAGE_MAP_PLOT_WITH_SAME_NAME_DIFF_SHEETS("VILLAGE_MAP_PLOT_WITH_SAME_NAME_DIFF_SHEETS","");
    private final String code;
    private final String description;

    LayerCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }

    public String desccription() {
        return description;
    }
}