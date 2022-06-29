package in.nic.lrisd.bhunakshav6.bhunakshacommon.bean;

import org.locationtech.jts.geom.Envelope;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;


public class WMSParms {

    private Envelope bbox;
    private int width = 256;
    private int height = 256;
    private boolean transparent = false;
    private Color bgcolor = Color.WHITE;
    private String format = "png";
    private String styles ="";
    private String sldBody = "";
    private String srs = "";
    private String layers = "";
    private String request = "GetMap";
    private String version = "1.1.1";
    private String cqlFilter = "";
    private String state = "";
    private String gisCodes = "";
    private String vsrNos = "";
    private String skipGisCodes = "";
    private String skipVsrNos = "";
    private String overlayCodes = "";
    private String themeCodes = "";
    private String vsrLevels = "";
    private String gisLevels = "";
    private String plotNos = "";
    private String plotIds = "";
    private String data="";
    private String layercodes = "";
    private String plotId = "";
    private String mapType="";

    public WMSParms(HttpServletRequest request) {

        String val = extractParm(request, "bbox");
        if (val != null) {

            String[] parts = val.split(",");

            if (parts.length == 4) {
                bbox = new Envelope(
                        Double.parseDouble(parts[0].trim()),
                        Double.parseDouble(parts[2].trim()),
                        Double.parseDouble(parts[1].trim()),
                        Double.parseDouble(parts[3].trim())
                );
            }
        }
//        val = extractParm(request, "crs");
//        if (val != null) {
//            srs = val;
//        }
        val = extractParm(request, "srs");
        if (val != null) {
            srs = val;
        }

        if (!isEmpty(srs)) {
            if (!srs.startsWith("EPSG")) {
                srs = "EPSG:" + srs;
            }
        }
        width = extractParm(request, "width") == null ? 256 : Integer.parseInt(request.getParameter("WIDTH"));
        height = extractParm(request, "height") == null ? 256 : Integer.parseInt(request.getParameter("HEIGHT"));
        val = extractParm(request, "bgcolor");
        if (val != null) {
            bgcolor = Color.decode(val);
        }
        val = extractParm(request, "transparent");
        if (val != null) {
            if (val.toLowerCase().equals("true") || val.toLowerCase().equals("Y")) {
                transparent = true;
            }
        }
        val = extractParm(request, "format");
        format = val == null ? "" : val;
        val = extractParm(request, "map_type");
        mapType = val == null ? "" : val;
        val = extractParm(request, "styles");
        styles = val == null ? "" : val;
        val = extractParm(request, "sld_body");
        sldBody = val == null ? "" : val;
        val = extractParm(request, "layers");
        layers = val == null ? "" : val;
        val = extractParm(request, "cql_filter");
        cqlFilter = val == null ? "" : val;
        val = extractParm(request, "state");
        state = val == null ? "" : val;
        val = extractParm(request, "gis_code");
        gisCodes = val == null ? "" : val;
        if(isEmpty(gisCodes)){
            val = extractParm(request, "giscode");
            gisCodes = val == null ? "" : val;
        }
        val = (extractParm(request, "vsrno") != null) ? extractParm(request, "vsrno") : extractParm(request, "village_code");
        vsrNos = val == null ? "" : val;
        val = extractParm(request, "skip_gis_code");
        skipGisCodes = val == null ? "" : val;
        val = (extractParm(request, "skip_vsrno") != null) ? extractParm(request, "skip_vsrno") : extractParm(request, "skip_village_code");
        skipVsrNos = val == null ? "" : val;
        val = extractParm(request, "overlay_codes");
        overlayCodes = val == null ? "" : val;
        val = extractParm(request, "theme_codes");
        themeCodes = val == null ? "" : val;
        val = extractParm(request, "vsr_levels");
        vsrLevels = (val == null || val.equals(",")) ? "" : val;
        val = extractParm(request, "gis_levels");
        gisLevels = (val == null || val.equals(",")) ? "" : val;
        val = extractParm(request, "plot_no");
        plotNos = (val == null || val.equals(",")) ? "" : val;
        val = extractParm(request, "plot_id");
        plotIds = (val == null || val.equals(",")) ? "" : val;
        val = extractParm(request, "data");
        data = (val == null || val.equals(",")) ? "" : val;
        val = extractParm(request, "layercodes");
        layercodes = val == null ? "" : val;
        val = extractParm(request, "plotId");
        plotId = (val == null || val.equals(",")) ? "" : val;
    }

    private String extractParm(HttpServletRequest request, String parm) {

        if (request.getParameter(parm) != null) {
            return request.getParameter(parm);
        } else if (request.getParameter(parm.toLowerCase()) != null) {
            return request.getParameter(parm.toLowerCase());
        } else if (request.getParameter(parm.toUpperCase()) != null) {
            return request.getParameter(parm.toUpperCase());
        } else {
            return null;
        }
    }

    public void setBbox(Envelope bbox) {
        this.bbox = bbox;
    }

    public Envelope getBbox() {
        return bbox;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public Color getBgcolor() {
        return bgcolor;
    }

    public String getFormat() {
        return format;
    }

    public String getSldBody() {
        return sldBody;
    }

    public String getSrs() {
        return srs;
    }

    public String getLayers() {
        return layers;
    }

    public String getRequest() {
        return request;
    }

    public String getVersion() {
        return version;
    }

    public String getCqlFilter() {
        return cqlFilter;
    }

    public String getState() {
        return state;
    }

    public String getGisCodes() {
        return gisCodes;
    }

    public boolean isEmpty(String value) {
        return (null == value || value.trim().isEmpty());
    }

    public String getSkipGisCodes() {
        return skipGisCodes;
    }

    public void setCqlFilter(String cql) {
        cqlFilter = cql;
    }

    public String getOverlayCodes() {
        return overlayCodes;
    }

    public String getVsrNos() {
        return vsrNos;
    }

    public String getSkipVsrNos() {
        return skipVsrNos;
    }

    public String getVsrLevels() {
        return vsrLevels;
    }

    public String getGisLevels() {
        return gisLevels;
    }

    public String getPlotNos() {
        return plotNos;
    }

    public String getPlotIds() {
        return plotIds;
    }

    public String getThemeCodes() {
        return themeCodes;

    }

    public String getStyles() {
        return styles;
    }

    public String getData() {
        return data;
    }

    public String getlayercodes() {
        return layercodes;
    }

    public String getplotId() {
        return plotId;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }



}

