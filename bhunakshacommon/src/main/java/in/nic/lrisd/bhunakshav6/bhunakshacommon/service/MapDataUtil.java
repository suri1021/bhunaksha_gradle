package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.CommonUtil;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.*;
import org.apache.commons.codec.binary.Base64;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.operation.builder.AffineTransformBuilder;
import org.geotools.referencing.operation.builder.MappedPosition;
import org.geotools.referencing.operation.builder.MathTransformBuilder;
import org.geotools.referencing.operation.builder.SimilarTransformBuilder;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.styling.SLDParser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.StringReader;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MapDataUtil {

    @Autowired
    private LevelReaderService levelReader;

    @Autowired
    private LevelDecoderService levelDecoder;

    @Autowired
    private StateDataProviderService stateDataProvider;

    @Autowired
    private KhasramapService khasramapService;

    @Autowired
    private VvvvService vvvvService;

    @Autowired
    private MapStylesService mapStylesService;

    @Autowired
    private LayerMasterService layerMasterService;

    @Autowired
    private AppSettingsService appSettingsService;

    public ListFeatureCollection fetchFeatures(List<Object[]> data, String featureTypeList) throws SQLException, NoSuchAuthorityCodeException, FactoryException, ParseException, SchemaException {

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

        SimpleFeatureType featureType = DataUtilities.createType("TYPE", featureTypeList);
        //featureType= DataUtilities.createSubType(featureType, null, DefaultEngineeringCRS.GENERIC_2D);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        ListFeatureCollection collection = new ListFeatureCollection(featureType);
        WKTReader reader = new WKTReader(geometryFactory);

        for (Object[] row : data) {

            Geometry geom = reader.read(row[0].toString());
            featureBuilder.add(geom);

            for (int k = 1; k < row.length; k++) {
                featureBuilder.add(row[k]);
            }
            SimpleFeature feature = featureBuilder.buildFeature(null);

            collection.add(feature);

        }

        return collection;

    }

    public SimpleFeature createFeature(Object[] row, String featureTypeList) throws SQLException, NoSuchAuthorityCodeException, FactoryException, ParseException, SchemaException, ParseException {

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

        SimpleFeatureType featureType = DataUtilities.createType("TYPE", featureTypeList);
        // featureType= DataUtilities.createSubType(featureType, null, DefaultEngineeringCRS.GENERIC_2D);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        WKTReader reader = new WKTReader(geometryFactory);

        Geometry geom = reader.read(row[0].toString());
        featureBuilder.add(geom);
        for (int k = 1; k < row.length; k++) {
            featureBuilder.add(row[k]);
        }
        SimpleFeature feature = featureBuilder.buildFeature(null);

        return feature;

    }

    public SimpleFeature createFeature(Object[] row, String featureTypeList, CoordinateReferenceSystem crs) throws SQLException, NoSuchAuthorityCodeException, FactoryException, ParseException, SchemaException {

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

        SimpleFeatureType featureType = DataUtilities.createType("TYPE", featureTypeList);
        if (null != crs) {
            featureType = DataUtilities.createSubType(featureType, null, crs);
            //eatureType= DataUtilities.createSubType(featureType, null, DefaultEngineeringCRS.GENERIC_2D);
        }
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        WKTReader reader = new WKTReader(geometryFactory);

        Geometry geom = reader.read(row[0].toString());
        featureBuilder.add(geom);
        for (int k = 1; k < row.length; k++) {
            featureBuilder.add(row[k]);
        }
        SimpleFeature feature = featureBuilder.buildFeature(null);

        return feature;

    }

    public String getPlotID(String gisCode, String plotNo) throws SQLException {
        String id = "";
        try {
            if (levelDecoder != null && !levelDecoder.isGisCodeValid(gisCode)) {
                return id;
            }

            if (stateDataProvider != null && !stateDataProvider.isPlotNoValid(gisCode, plotNo)) {
                return id;
            }

            Khasramap khasraMap = khasramapService.findByBhucodeKide(stateDataProvider.getStateCode(), gisCode, plotNo);
            return khasraMap.getId();

        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String getPlotNo(String gisCode, String plotId) throws SQLException {
        String kide = "";
        try {
            if (levelDecoder != null && !levelDecoder.isGisCodeValid(gisCode) && (plotId != null) && !(plotId.isEmpty()) && (!CommonUtil.isValidId(plotId))) {
                return kide;
            }

            Khasramap khasramap = khasramapService.findByBhucodeId(stateDataProvider.getStateCode(), gisCode, plotId);

            return khasramap.getKide();
        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kide;
    }

    public double getArea(String gisCode, String plotId) throws SQLException {
        double area = 0;
        try {

            if (levelDecoder != null && !levelDecoder.isGisCodeValid(gisCode) && !CommonUtil.isValidId(plotId)) {
                return area;
            }

            Double value = khasramapService.getArea(stateDataProvider.getStateCode(), gisCode, plotId);
            if (value != null)
                area = (double) Math.round(value * 100) / 100;

        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return area;
    }

    public String formatedPlotArea(double vArea, Map<String, String> areaUnitsMap) {
        DecimalFormat form = new DecimalFormat("#");
        DecimalFormat form1 = new DecimalFormat("#.####");
        try {
            String areaFormat = "";
            areaFormat = areaUnitsMap.get("plotAreaFormat");
            String output = "";
            // areaFormat = "{bigas}-{k}-{lc}";
            ArrayList<Integer> startbrace = new ArrayList<>();
            ArrayList<Integer> endbrace = new ArrayList<>();
            int index = areaFormat.indexOf("{");

            while (index >= 0) {
                startbrace.add(index);
                index = areaFormat.indexOf("{", index + 1);
            }
//            index = -1;
            index = areaFormat.indexOf("}");

            while (index >= 0) {
                endbrace.add(index);
                index = areaFormat.indexOf("}", index + 1);
            }

            String val = "";
            for (index = 0; index < startbrace.size(); index++) {
                val += areaFormat.substring(startbrace.get(index) + 1, endbrace.get(index)) + "-";
            }
            String[] tempFormat = val.split("-");
            // String[] tempFormatValue = {"0.000746", "0.00374", "0.07476"};
            String[] tempFormatValue = new String[tempFormat.length];
            for (int i = 0; i < tempFormat.length; i++) {
                tempFormatValue[i] = areaUnitsMap.get(tempFormat[i]);
            }
            double conversionFactor1 = 0.0, conversionFactor2 = 0.0;
            ArrayList<String> outputValue = new ArrayList<>();
            if (tempFormat.length == 1) { //single unit
                double tempVal = 0.0;
                try {
                    conversionFactor1 = Double.parseDouble(tempFormatValue[0]);
                } catch (Exception x) {
                    Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, x);
                }
                tempVal = vArea * conversionFactor1;
                if (Double.isInfinite(tempVal) || Double.isNaN(tempVal)) {
                    tempVal = 0.0;
                }
                outputValue.add("" + Double.valueOf(form1.format(tempVal)));

            }
            for (int i = 0; i < tempFormat.length - 1; i++) { //multiple unit

                if (outputValue.size() > 0) {
                    outputValue.remove(outputValue.size() - 1);
                }

                conversionFactor1 = Double.parseDouble(tempFormatValue[i]);
                double tempVal = 0.0;
                if ((outputValue.size() <= 0)) {
                    tempVal = vArea * conversionFactor1;
                } else {
                    tempVal = vArea;
                }
                long iPart;
                double fPart;
                conversionFactor2 = Double.parseDouble(tempFormatValue[i + 1]);
                iPart = (long) tempVal;
                fPart = tempVal - iPart;
                fPart = fPart * (conversionFactor2 / conversionFactor1);
                vArea = fPart;
                outputValue.add("" + iPart);
                outputValue.add("" + Integer.valueOf(form.format(fPart)));

            }//end for loop
            int i = 0;
            int st_index = 0;
            for (i = 0; i < startbrace.size(); i++) {

                if (st_index == 0) { //starting value
                    output += areaFormat.substring(st_index, startbrace.get(i));
                    st_index++;
                }
                output += outputValue.get(i);
                if ((i + 1) < startbrace.size()) {
                    output += areaFormat.substring(endbrace.get(i), startbrace.get(i + 1));
                }
            }
            if (endbrace.size() > 1) {
                output += areaFormat.substring(endbrace.get(i - 1));
            }
            output = output.replaceAll("}", "");

            if (output != null && !output.equals("")) {
                return output;
            }

        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        double area = 0;
        try {
            if (Double.isInfinite(vArea) || Double.isNaN(vArea)) {
                vArea = 0.0;
            }
            area = Double.valueOf(form.format(vArea));
            // System.out.println("Area-------------"+area);
        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "" + area;
    }

    public Map getExtent(String dbSchema, String gisCode, String plotId) throws Exception {
        return khasramapService.getExtent(stateDataProvider.getStateCode(), dbSchema, gisCode, plotId);
    }

    public Map getClickInfo(String gisCode, double x, double y) {
        try {
            Point pt = new GeometryFactory().createPoint(new Coordinate(x, y));
            Map mapData = khasramapService.findBYInnerPointMapData(stateDataProvider.getStateCode(), gisCode, pt.toText());

            Map info = new HashMap();

            String plotNo = "";
            String jsonString = "";
            String pniu ="";
            if (! mapData.isEmpty()) {
                info.put("ID", mapData.get("id"));
                plotNo = (mapData.get("kide") == null) ? "" : ((String)mapData.get("kide")).trim();
                jsonString = (String) mapData.get("jsonbdata");
                info.put("plotNo", plotNo);
                pniu = (mapData.get("pniu") == null) ? "" : ((String)mapData.get("pniu")).trim();
                info.put("PNIU", pniu);
                if (jsonString == null) {
                    jsonString = "";
                }
                info.put("attrs", Base64.encodeBase64String(jsonString.getBytes()));
                info.put("gisCode", gisCode);
                String vsrNo = getVsrNo("public", gisCode);

                if (!jsonString.isEmpty()) {
                    info.put("info", stateDataProvider.getPlotInfo(null, vsrNo, jsonString, gisCode));
                } else {
                    info.put("info",stateDataProvider.getPlotInfo(null, vsrNo, plotNo, gisCode));
                }
                String rorUrl = "";
                if (!jsonString.isEmpty()) {
                    rorUrl = stateDataProvider.getPlotInfoLinks( gisCode, jsonString);
                } else {
                    rorUrl = stateDataProvider.getPlotInfoLinks(gisCode, plotNo);
                }
                info.put("plotInfoLinks", rorUrl);
                info.put("has_data", "Y");
            } else {
                info.put("has_data", "N");
            }

            return info;
        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Map getPlotInfo(String gisCode, String plotNo, String plotID) {
        try {
            Map info = new HashMap();

            if (stateDataProvider != null && !stateDataProvider.isPlotNoValid(gisCode, plotNo)) {
                return null;
            }
            Map mapData = khasramapService.findMapDataCentroidByBhucodeId(stateDataProvider.getStateCode(), gisCode, plotNo, plotID);
               WKTReader reader = new WKTReader();

            if (! mapData.isEmpty()) {
                String attrs = (String) mapData.get("attrs");
                info.put("ID", mapData.get("id"));
                info.put("PNIU", mapData.get("pniu"));
                if (attrs == null) {
                    attrs = "";
                }
                info.put("attrs", Base64.encodeBase64String(attrs.getBytes()));
                Point p = (Point) reader.read((String) mapData.get("centroid"));
                info.put("center_x", p.getX());
                info.put("center_y", p.getY());
                info.put("plotNo", plotNo);
                info.put("gisCode", gisCode);
                String vsrNo = getVsrNo("public", gisCode);

                if (!attrs.isEmpty()) {
                    info.put("info", stateDataProvider.getPlotInfo(null, vsrNo, attrs, gisCode));
                } else {
                    info.put("info", stateDataProvider.getPlotInfo(null, vsrNo, plotNo, gisCode));
                }
                String rorUrl = "";
                if (!attrs.isEmpty()) {
                    rorUrl = stateDataProvider.getPlotInfoLinks(gisCode, attrs);
                } else {
                    rorUrl = stateDataProvider.getPlotInfoLinks(gisCode, plotNo);
                }
                info.put("plotInfoLinks", rorUrl);
                info.put("has_data", "Y");
            } else {
                info.put("has_data", "N");
            }

            return info;
        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getVsrNo(String dbSchema, String gisCode) {
        Vvvv vvvv = vvvvService.findByBhucode(gisCode);

        return vvvv.getVsrno();
    }

    public Style getMapStyle(String mapStyleCode) {

        List<MapStyles> mapStylesList = mapStylesService.findAll();

        mapStylesList = mapStylesList.stream().filter(style -> style.getCode() == mapStyleCode).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(mapStylesList))
            return getStyleFromSymbolizer(mapStylesList.get(0).getSldSymbolizers());

        return null;
    }

    public double getScaleFactor(String gisCode) {
        double factor = 1.0;

        if (levelDecoder != null && !levelDecoder.isGisCodeValid(gisCode)) {
            return factor;
        }

        Vvvv vvvv = vvvvService.findByBhucode(gisCode);
        factor = vvvv.getMapscale();

        if (factor <= 0) {
            factor = 1;
        }

        return factor;
    }

    public String getKid(String gisCode, String plotNo) throws Exception {
        String kId = "";

            if (levelDecoder != null && !levelDecoder.isGisCodeValid(gisCode)) {
                return kId;
            }
            if (stateDataProvider != null && !stateDataProvider.isPlotNoValid(gisCode, plotNo)) {
                return kId;
            }

            Khasramap khasramap = khasramapService.findByBhucodeKide(stateDataProvider.getStateCode(), gisCode, plotNo);
            if (khasramap != null) return khasramap.getKide();

        return kId;
    }

    public String getSLDSymbolizer(String mapStyleCode) {
            List<MapStyles> symbolizers = mapStylesService.findAll();
            symbolizers = symbolizers.stream().filter(symbolizer -> symbolizer.getCode() == mapStyleCode).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(symbolizers)) return symbolizers.get(0).getSldSymbolizers();
        return null;
    }

    public String getLayerSLDSymbolizer(String layerTypeCode) {

        List<LayerMaster> layerMasterList = layerMasterService.findAll();
        layerMasterList = layerMasterList.stream().filter(layerMaster -> layerMaster.getLayerType() == layerTypeCode).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(layerMasterList)) return layerMasterList.get(0).getSldSymbolizers();
        return null;
    }

    public Style getStyleFromSymbolizer(String symbolyzer) {
        String servlet = "http://localhost:8080/bhunakshaweb3/SymbolServlet?state=\" + stateCode + \"&amp;code=";
        symbolyzer = symbolyzer.replaceAll("BHUNAKSHASYMBOL://", servlet);

        String sld = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
                + "<StyledLayerDescriptor version=\"1.0.0\" \n"
                + "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n"
                + "    xmlns=\"http://www.opengis.net/sld\" \n"
                + "    xmlns:ogc=\"http://www.opengis.net/ogc\" \n"
                + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n"
                + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                + "  <NamedLayer>\n"
                + "    <Name>Optimized label placement</Name>\n"
                + "    <UserStyle>\n"
                + "      <Title>SLD Cook Book: Optimized label placement</Title>\n"
                + "      <FeatureTypeStyle>"
                + wrapSymbolizerInRule(symbolyzer)
                + "</FeatureTypeStyle>\n"
                + "    </UserStyle>\n"
                + "  </NamedLayer>\n"
                + "</StyledLayerDescriptor>";
        // System.out.println(sld);
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        Style style = null;
        SLDParser sldParser = new SLDParser(styleFactory, new StringReader(sld));
        style = sldParser.readXML()[0];
        return style;
    }

    public Style getStyleFromSymbolizer(String symbolServletUrl, String symbolyzer) {

        symbolyzer = symbolyzer.replaceAll("BHUNAKSHASYMBOL://", symbolServletUrl);
        String sld = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
                + "<StyledLayerDescriptor version=\"1.0.0\" \n"
                + "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n"
                + "    xmlns=\"http://www.opengis.net/sld\" \n"
                + "    xmlns:ogc=\"http://www.opengis.net/ogc\" \n"
                + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n"
                + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                + "  <NamedLayer>\n"
                + "    <Name>Optimized label placement</Name>\n"
                + "    <UserStyle>\n"
                + "      <Title>SLD Cook Book: Optimized label placement</Title>\n"
                + "      <FeatureTypeStyle>"
                + wrapSymbolizerInRule(symbolyzer)
                + "</FeatureTypeStyle>\n"
                + "    </UserStyle>\n"
                + "  </NamedLayer>\n"
                + "</StyledLayerDescriptor>";
        // System.out.println(sld);
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        Style style = null;
        SLDParser sldParser = new SLDParser(styleFactory, new StringReader(sld));
        style = sldParser.readXML()[0];
        return style;
    }

    public Style getStyleFromRules(String rules) {

        String servlet = "http://localhost:8080/bhunakshaweb3/SymbolServlet?state=\" + stateCode + \"&amp;code=";

        rules = rules.replaceAll("BHUNAKSHASYMBOL://", servlet);

        String sld = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
                + "<StyledLayerDescriptor version=\"1.0.0\" \n"
                + "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n"
                + "    xmlns=\"http://www.opengis.net/sld\" \n"
                + "    xmlns:ogc=\"http://www.opengis.net/ogc\" \n"
                + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n"
                + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                + "  <NamedLayer>\n"
                + "    <Name>Optimized label placement</Name>\n"
                + "    <UserStyle>\n"
                + "      <Title>SLD Cook Book: Optimized label placement</Title>\n"
                + "      <FeatureTypeStyle>"
                + rules
                + "</FeatureTypeStyle>\n"
                + "    </UserStyle>\n"
                + "  </NamedLayer>\n"
                + "</StyledLayerDescriptor>";
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        Style style = null;
        SLDParser sldParser = new SLDParser(styleFactory, new StringReader(sld));
        style = sldParser.readXML()[0];

        return style;
    }

    public String wrapSymbolizerInRule(String symbolyzer) {
        if (symbolyzer.trim().toLowerCase().startsWith("<rule>")) {
            return symbolyzer;
        } else {
            String sld = "<Rule>"
                    + symbolyzer
                    + "</Rule>";

            return sld;
        }
    }

    public Map getClickInfoGeoRef(double x, double y, String srs) {
        try {
            Point pt = new GeometryFactory().createPoint(new Coordinate(x, y));
            Map info = new HashMap();

            MathTransformFactory mf = ReferencingFactoryFinder.getMathTransformFactory(null);
            List<Vvvv> vvvvList = vvvvService.findByPointInterSection(pt.toText(), srs);

            if (! CollectionUtils.isEmpty(vvvvList))
            {
                Vvvv vvvv = vvvvList.get(0);
                String gisCode = vvvv.getGisCode();
                if (levelDecoder != null && gisCode != null && !gisCode.isEmpty() && !levelDecoder.isGisCodeValid(gisCode)) {
                    return null;
                }
                String trs = vvvv.getGeotransform();
                MathTransform tr = null;
                if (trs.startsWith("EPSG")) {
                    CoordinateReferenceSystem sourceCRS = CRS.decode(trs);
                    CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + srs);
                    tr = CRS.findMathTransform(targetCRS, sourceCRS);
                } else {
                    tr = mf.createFromWKT(trs).inverse();
                }

                Geometry point1 = JTS.transform(pt, tr);
                Khasramap khasramap = khasramapService.findByIntersectionPoint(stateDataProvider.getStateCode(), gisCode, point1.toText());
                String plotNo = "";
                if (khasramap != null) {

                    info.put("ID", khasramap.getId());
                    plotNo = khasramap.getKide().trim();
                    info.put("plotNo", plotNo);
                    info.put("gisCode", gisCode);
                    String vsrNo = getVsrNo("public", gisCode);

                    String location =levelReader.fetchGisInfo(gisCode);
                    info.put("location", location);
                    info.put("info", stateDataProvider.getPlotInfo(null, vsrNo, plotNo, gisCode));
                    info.put("has_data", "Y");
                } else {
                    info.put("has_data", "N");
                }

                return info;

            }

        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public String transformationAsWkt(List<String> source, List<String> dest) {

        WKTReader reader = new WKTReader(JTSFactoryFinder.getGeometryFactory(null));
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.GENERIC_2D;
        try {
            List<MappedPosition> pts = new ArrayList<MappedPosition>();
            for (int i = 0; i < source.size(); i++) {

                Point srcGeom = (Point) reader.read(source.get(i));
                Point destGeom = (Point) reader.read(dest.get(i));
                pts.add(new MappedPosition(new DirectPosition2D(crs, srcGeom.getX(), srcGeom.getY()), new DirectPosition2D(crs, destGeom.getX(), destGeom.getY())));
            }
            MathTransformBuilder builder;
            if (pts.size() > 2) {
                builder = new AffineTransformBuilder(pts);
            } else {
                builder = new SimilarTransformBuilder(pts);
            }
            MathTransform tr = builder.getMathTransform();
            return tr.toWKT();
        } catch (ParseException ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FactoryException ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MismatchedDimensionException ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public String getAttributionForMap(String schema, String gisCode) {
        String attribution = "", updatedOn = "", disclaimer = "", updated_on_flag = "", disclaimer_flag = "";
        try {

            List<AppSettings> appSettingsList = appSettingsService.findAll();
            appSettingsList = appSettingsList.stream().filter(appSettings ->
                appSettings.getCode() == "disclaimer_flag" || appSettings.getCode() == "disclaimer" || appSettings.getCode() == "updated_on_flag").collect(Collectors.toList());

            if (!levelDecoder.isGisCodeValid(gisCode)) {
                return null;
            }

            for(AppSettings appSettings: appSettingsList) {
                if (appSettings.getCode().equals("disclaimer_flag")) {
                    disclaimer_flag = appSettings.getValue();
                } else if (appSettings.getCode().equals("disclaimer")) {
                    disclaimer = appSettings.getValue();
                } else if (appSettings.getCode().equals("updated_on_flag")) {
                    updated_on_flag = appSettings.getValue();
                }

            }

            if (updated_on_flag != null && updated_on_flag.equals("Y")) {
                updatedOn = "Updated on :" + khasramapService.findMaxLastUpdatedByBhucode(stateDataProvider.getStateCode(), gisCode);
            }

            String gisInfo = levelReader.fetchGisInfo(gisCode);
            if (disclaimer_flag.equals("N")) {
                disclaimer = "";
            }
            if (updated_on_flag.equals("N")) {
                updatedOn = "";
            } else {
                updatedOn = updatedOn + "<br />";
            }
            String atrfnt = "style=\"font-family:arial; color: #0000FF; font-size: 10.0px;\"";
            //   String atrfnt = "";
            // attribution += "<br /><br /><br /><br /><span " + atrfnt + ">" + gisInfo + "<br />" + updatedOn + "<br />" + disclaimer + "</span>";
            attribution += "<br /><br /><br /><br /><span " + atrfnt + ">" + gisInfo + "<br />" + updatedOn + disclaimer + "</span>";

        } catch (Exception ex) {
            Logger.getLogger(MapDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return attribution;
    }

}
