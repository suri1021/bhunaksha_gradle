package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.LayerCodes;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.WMSParms;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Khasramap;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.globalsettings.TablePartition;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WMSLayerBuilder {

    public static final int STMT_PARM_STRING = 1;
    public static final int STMT_PARM_INT = 2;
    public static final int STMT_PARM_ARRAY = 3;

    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    WKTReader reader = new WKTReader(geometryFactory);

    MathTransformFactory mf = ReferencingFactoryFinder.getMathTransformFactory(null);

    private static Map<String, Method> annotMethods = new HashMap<String, Method>();

    @Autowired
    private MapDataUtil mapDataUtil = new MapDataUtil();

    @Autowired
    private StateDataProvider stateDataProvider;

    @Autowired
    private LevelReader levelReader;

    @Autowired
    private LevelDecoder levelDecoder;

    @Autowired
    private AppSettingsService appSettingsService;

    @Autowired
    private VvvvService vvvvService;

    @Autowired
    private KhasramapService khasramapService;

    public WMSLayerBuilder() {

    }

    public List<Layer> createLayers(WMSParms parms) throws Exception {
        List<Layer> layers = new ArrayList<Layer>();

        try {
            if (parms.getState().equals("36") && (parms.isEmpty(parms.getGisCodes()) || levelDecoder.isGisCodeValid(parms.getGisCodes()) == false)) {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        String[] layerNames = parms.getLayers().split(",");
        try {
            if (!parms.isEmpty(parms.getCqlFilter())) {
                if (isValidFilter(parms.getCqlFilter())) {
                    parms.setCqlFilter(parms.getCqlFilter().replaceAll("(?i)strsubstring", "substring"));
                } else {
                    throw new SQLException("Invalid ECQL Filter Syntax");

                }
            }
            for (String layerName : layerNames) {
                if (layerName.equals(LayerCodes.VILLAGE_MAP_PLOT_WITH_SAME_NAME_DIFF_SHEETS.code()))
                {
                  /*  List<Object[]> list = new ArrayList<Object[]>();
                    String giscode = parms.getGisCodes();
                    String plotId = parms.getPlotIds();
                    String plotNo = parms.getPlotNos();
                    int[] subStrIdx = TablePartition.getSubstrRange4Java(stateDataProvider.getStateCode());
                    String tableID = giscode.substring(subStrIdx[0], subStrIdx[1]);
                    String sql = " select st_astext(st_transform(st_setsrid(k.wkb_geometry,cast(substring(v.geotransform, 6,10) as integer)),4326))  geom,\n" +
                            " kide, bhucode from khasramap_" + tableID +" k , vvvv v where bhucode like  ? and gis_code=bhucode\n" +
                            " and kide = ?";
                    PreparedStatement pstmt = conMgr.getVectorConnection().prepareStatement(sql);
                    pstmt.setString(1,giscode.substring(0,18)+"%");
                    pstmt.setString(2, plotNo);
                    ResultSet rstmt = pstmt.executeQuery();
                    while (rstmt.next())
                    {
                        Object[] obj = new Object[3];
                        obj[0] = rstmt.getString("geom");
                        obj[1] = rstmt.getString("kide");
                        obj[2] = rstmt.getString("bhucode");
                        list.add(obj);
                    }
                    ListFeatureCollection col = mapDataUtil.fetchFeatures(list, "the_geom:MultiPolygon,name:\"\",tag:\"\"");
                    Style style = mapDataUtil.getMapStyle("PLOT_WISE_MAP");
                    Layer layer = new FeatureLayer(col, style);
                    layers.add(layer);
                    rstmt.close();
                    pstmt.close();*/

                }
                else if (layerName.equals(LayerCodes.VILLAGE_MAP.code())) {

                    List<Map<String, String>> gisCodes = getGisCodesAsList(parms);
                    List<Object[]> data = fetchPlotMaps(parms, gisCodes);
                    Style style = null;

                    if (!parms.isEmpty(parms.getStyles())) {
                        style = mapDataUtil.getMapStyle(parms.getStyles());

                    } else if (!parms.isEmpty(parms.getSldBody())) {
                        style = mapDataUtil.getStyleFromSymbolizer(parms.getSldBody());
                    } else {
                        style = mapDataUtil.getMapStyle("VILLAGE_MAP_TRANSPARENT");
                    }

                    if (style == null) {
                        style = SLD.createPolygonStyle(Color.GREEN, Color.WHITE, 0.0f, "name", null);
                    }

                    ListFeatureCollection col = mapDataUtil.fetchFeatures(data, "the_geom:MultiPolygon,name:\"\",tag:\"\"");

                    Layer layer = new FeatureLayer(col, style);
                    layers.add(layer);

                } else if (layerName.equals(LayerCodes.PLOT_LIST.code())) {
                    List<Map<String, String>> gisCodes = getGisCodesAsList(parms);
                    List<Object[]> data = fetchPlotMaps(parms, gisCodes);
                    Style style = null;
                    if (!parms.isEmpty(parms.getStyles())) {
                        style = mapDataUtil.getMapStyle(parms.getStyles());

                    } else if (!parms.isEmpty(parms.getSldBody())) {
                        style = mapDataUtil.getStyleFromSymbolizer(parms.getSldBody());
                    } else {
                        style = mapDataUtil.getMapStyle("PLOT_SELECTION");
                    }

                    if (style == null) {
                        style = SLD.createPolygonStyle(Color.GREEN, Color.WHITE, 0.0f, "name", null);
                    }

                    ListFeatureCollection col = mapDataUtil.fetchFeatures(data, "the_geom:MultiPolygon,name:\"\",tag:\"\"");

                    Layer layer = new FeatureLayer(col, style);
                    layers.add(layer);

                } else if (layerName.equals(LayerCodes.SAME_OWNER_PLOT_LIST.code())) {

                    List<Map<String, String>> gisCodes = getGisCodesAsList(parms);
                    List<Object[]> data = fetchSameOwnerPlotMaps(parms, gisCodes);
                    Style style = null;
                    if (!parms.isEmpty(parms.getStyles())) {
                        style = mapDataUtil.getMapStyle(parms.getStyles());

                    } else if (!parms.isEmpty(parms.getSldBody())) {
                        style = mapDataUtil.getStyleFromSymbolizer(parms.getSldBody());
                    } else {
                        style = mapDataUtil.getMapStyle("OWNER_PLOTS");
                    }

                    if (style == null) {
                        style = SLD.createPolygonStyle(Color.GREEN, Color.WHITE, 0.0f, "name", null);
                    }

                    ListFeatureCollection col = mapDataUtil.fetchFeatures(data, "the_geom:MultiPolygon,name:\"\",tag:\"\"");

                    Layer layer = new FeatureLayer(col, style);
                    layers.add(layer);

                } else if (layerName.equals(LayerCodes.VILLAGE_BOUNDARIES.code())) {

                    List<Object[]> data = fetchVillageBoundaryMaps(parms);
                    Style style = null;
                    if (!parms.isEmpty(parms.getStyles())) {
                        style = mapDataUtil.getMapStyle(parms.getStyles());

                    } else if (!parms.isEmpty(parms.getSldBody())) {
                        style = mapDataUtil.getStyleFromSymbolizer(parms.getSldBody());
                    } else {
                        style = mapDataUtil.getMapStyle("VILLAGE_BOUNDARY_TRANSPARENT");
                    }

                    if (style == null) {
                        style = SLD.createPolygonStyle(Color.GREEN, Color.WHITE, 0.0f, "name", null);
                    }

                    ListFeatureCollection col = mapDataUtil.fetchFeatures(data, "the_geom:MultiPolygon,name:\"\",tag:\"\"");

                    Layer layer = new FeatureLayer(col, style);
                    layers.add(layer);

                } else if (layerName.equals(LayerCodes.OVERLAY_LAYER.code())) {
                    List<Map<String, String>> gisCodes = getGisCodesAsList(parms);
                    layers = createOverlayLayers(parms, gisCodes);

                } else if (layerName.equals(LayerCodes.FMB_LAYERS.code())) {
                    //List<Map<String, String>> gisCodes = getGisCodesAsList(conMgr, parms);
                    layers = createFMBLayers(parms);

                } else if (layerName.equals(LayerCodes.MOSAICED_FMB_SUBDIV.code())) {
                    //List<Map<String, String>> gisCodes = getGisCodesAsList(conMgr, parms);
                    //List<Object[]> data = createMosaicedSubDivLayers(conMgr, parms);

                    List<Map<String, String>> gisCodes = getGisCodesAsList(parms);

                    Map<String, String> tempMap;
                    List<Object[]> data = null;
                    String mapType = null;

                    for (int i = 0; i < gisCodes.size(); i++) {
                        tempMap = gisCodes.get(i);
                        String str = tempMap.get("gis_code");
                        mapType = str.substring(0, 1);
                    }

                    if (mapType != null && !mapType.trim().isEmpty() && mapType.equals("M")) {
                        data = fetchPlotMaps(parms, gisCodes);
                    } else if (mapType != null && !mapType.trim().isEmpty() && mapType.equals("G")) {
                        data = fetchPlotMaps(parms, gisCodes);
                    } else {
                        data = createMosaicedSubDivLayers(parms);
                    }

                    Style style = null;

                    if (!parms.isEmpty(parms.getStyles())) {
                        style = mapDataUtil.getMapStyle(parms.getStyles());

                    } else if (!parms.isEmpty(parms.getSldBody())) {
                        style = mapDataUtil.getStyleFromSymbolizer(parms.getSldBody());
                    } else {

                        if (mapType != null && !mapType.trim().isEmpty() && mapType.equals("M")) {
                            style = mapDataUtil.getMapStyle("VILLAGE_MAP_MOSAIC");
                        } else if (mapType != null && !mapType.trim().isEmpty() && mapType.equals("G")) {
                            style = mapDataUtil.getMapStyle("VILLAGE_MAP_GEO");
                        } else {
                            style = mapDataUtil.getMapStyle("VILLAGE_MAP_TRANSPARENT");
                        }
                    }

                    if (style == null) {
                        style = SLD.createPolygonStyle(Color.GREEN, Color.WHITE, 0.0f, "name", null);
                    }

                    ListFeatureCollection col = mapDataUtil.fetchFeatures(data, "the_geom:MultiPolygon,name:\"\",tag:\"\"");

                    Layer layer = new FeatureLayer(col, style);
                    layers.add(layer);

                } else if (layerName.equals(LayerCodes.THEME.code())) {
                    List<Map<String, String>> gisCodes = getGisCodesAsList(parms);
                    layers = createThemeLayers(parms, gisCodes);

                } else if (layerName.equals(LayerCodes.ETS_LAYERS.code())) {
                    //List<Map<String, String>> gisCodes = getGisCodesAsList(conMgr, parms);
                    layers = createETSLayers(parms);
                } else if (layerName.equals(LayerCodes.DERIVED_LAYER.code())) {
                    //List<Map<String, String>> gisCodes = getGisCodesAsList(conMgr, parms);
                    layers = createDerivedLayers(parms);

                } else if (layerName.equals(LayerCodes.DYNA_REFERENCED_LAYERS.code())) {

                    //List<Map<String, String>> gisCodes = getGisCodesAsList(conMgr, parms);
                    //System.out.println(parms.getsrcpoints().length+" - "+Arrays.asList(parms.getsrcpoints())+"\n"+Arrays.asList(parms.getdestpoints()));
                    String[] data1 = parms.getData().split("\\:");
                    String transWkt = "";
                    if (data1.length > 1) {
                        String[] srcPts = data1[0].split(",");
                        String[] destPts = data1[1].split(",");
                        transWkt = mapDataUtil.transformationAsWkt(Arrays.asList(srcPts), Arrays.asList(destPts));
                    }
                    //String transWkt = mapDataUtil.transformationAsWkt(Arrays.asList(parms.getsrcpoints()), Arrays.asList(parms.getdestpoints()));
                    List<Object[]> data = dynaReferencedlayers(parms.getGisCodes(), transWkt, parms);

                    //List<Object[]> data = fetchPlotMaps(conMgr, parms, gisCodes);
                    Style style = null;

                    if (!parms.isEmpty(parms.getStyles())) {
                        style = mapDataUtil.getMapStyle(parms.getStyles());

                    } else if (!parms.isEmpty(parms.getSldBody())) {
                        style = mapDataUtil.getStyleFromSymbolizer(parms.getSldBody());
                    } else {
                        style = mapDataUtil.getMapStyle("VILLAGE_MAP_TRANSPARENT");
                    }

                    if (style == null) {
                        style = SLD.createPolygonStyle(Color.GREEN, Color.WHITE, 0.0f, "name", null);
                    }

                    ListFeatureCollection col = mapDataUtil.fetchFeatures(data, "the_geom:MultiPolygon,name:\"\",tag:\"\"");

                    Layer layer = new FeatureLayer(col, style);
                    layers.add(layer);
                } else {

                  /*if (!annotMethods.containsKey(layerName)) {
                        Reflections reflections = new Reflections(new ConfigurationBuilder()
                                .addUrls(ClasspathHelper.forPackage("in.nic.lrisd.bhunaksha.state.wms"))
                                .setScanners(new MethodAnnotationsScanner()));

                        Set<Method> methods = reflections.getMethodsAnnotatedWith(AnnotationCustomWMSLayer.class);

                        for (Method method : methods) {

                            AnnotationCustomWMSLayer wms = method.getAnnotation(AnnotationCustomWMSLayer.class);

                            try {
                                annotMethods.put(wms.layerCode(), method);
                                //  annotClass.put(wms.layerCode(), method.getDeclaringClass().newInstance());

//                            } catch (InstantiationException ex) {
//                                Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
//                            } catch (IllegalAccessException ex) {
//                                Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }*/
                    if (annotMethods.containsKey(layerName)) {
                        try {
                            Object lyr = annotMethods.get(layerName).invoke(annotMethods.get(layerName).getDeclaringClass().newInstance(), parms);
                            layers = (List<Layer>) lyr;
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvocationTargetException ex) {
                            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FactoryException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchemaException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return layers;
    }

    //    ***********ETS Layer*************
    public List<Layer> createETSLayers(WMSParms parms) {
        List<Layer> layers = new ArrayList<Layer>();
      /*  Map<String, Object> jsonList1 = new HashMap<String, Object>();
        String json = parms.getData();

        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonList1 = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });

            Object parcelObj = jsonList1.get("parcels");
            Object pointObj = jsonList1.get("pointLayer");
            Object lineObj = jsonList1.get("lineLayer");
            Object polygonObj = jsonList1.get("polygonLayer");
            List<Map<String, Object>> parcelList = (List<Map<String, Object>>) parcelObj;
            List<Map<String, Object>> pointList = (List<Map<String, Object>>) pointObj;
            List<Map<String, Object>> lineList = (List<Map<String, Object>>) lineObj;
            List<Map<String, Object>> polygonList = (List<Map<String, Object>>) polygonObj;

            Connection con = conMgr.getVectorConnection();
            PreparedStatement pstm = con.prepareStatement("SELECT sld_symbolizers FROM layer_master WHERE layer_type=? and geometry_type=?");

            Map<String, ListFeatureCollection> features = new HashMap<String, ListFeatureCollection>();
            Map<String, Style> styles = new HashMap<String, Style>();

            for (Map<String, Object> map : parcelList) {
                Object[] obj = new Object[3];
                obj[0] = map.get("geometry");
                obj[1] = map.get("name");
                obj[2] = "PARCEL";
                if (obj[0] != null && !obj[0].toString().isEmpty() && !(reader.read(map.get("geometry").toString())).isValid()) {
                    return layers;
                }
                if (obj[1] != null && !obj[1].toString().isEmpty() && !(CommonUtil.isValidCharacters(obj[1].toString()))) {
                    return layers;
                }
                if (obj[2] != null && !obj[2].toString().isEmpty() && !(CommonUtil.isValidCharacters(obj[2].toString()))) {
                    return layers;
                }
                String layerType = "PARCEL";

                if (!styles.containsKey(layerType)) {

                    styles.put(layerType, mapDataUtil.getStyleFromSymbolizer( mapDataUtil.getSLDSymbolizer("VILLAGE_MAP")));
                    SimpleFeatureType featureType = DataUtilities.createType("TYPE", "the_geom:MultiPolygon,name:\"\",tag:\"\"");
                    ListFeatureCollection collection = new ListFeatureCollection(featureType);
                    features.put(layerType, collection);
                }
                features.get(layerType).add(mapDataUtil.createFeature(obj, "the_geom:MultiPolygon,name:\"\",tag:\"\""));
            }
            for (Map.Entry<String, Style> entry : styles.entrySet()) {
                layers.add(new FeatureLayer(features.get(entry.getKey()), entry.getValue()));
            }
            features.clear();
            styles.clear();
            for (Map<String, Object> map : pointList) {
                String sldSymb = null;
                Object[] obj = new Object[3];
                obj[0] = map.get("geometry");
                obj[1] = map.get("name");
                obj[2] = map.get("type");

                if (obj[0] != null && !obj[0].toString().isEmpty() && !(reader.read(map.get("geometry").toString())).isValid()) {
                    return layers;
                }
                if (obj[1] != null && !obj[1].toString().isEmpty() && !(CommonUtil.isValidCharacters(obj[1].toString()))) {
                    return layers;
                }
                if (obj[2] != null && !obj[2].toString().isEmpty() && !(CommonUtil.isValidCharacters(obj[2].toString()))) {
                    return layers;
                }

                String status = Objects.toString(map.get("status"));
                if (status.equalsIgnoreCase("D")) {
                    continue;
                }
                String layerType = Objects.toString(map.get("type"));

                if (!styles.containsKey(layerType)) {
                    pstm.setString(1, layerType);
                    pstm.setString(2, "MultiPoint");

                    ResultSet rs = pstm.executeQuery();
                    if (rs.next()) {

                        sldSymb = rs.getString("sld_symbolizers");

                    }
                    rs.close();
                    if (sldSymb == null || sldSymb.trim().isEmpty()) {

                        sldSymb = mapDataUtil.getSLDSymbolizer("DEFAULT_POINT_LAYER");
                    }

                    styles.put(layerType, mapDataUtil.getStyleFromSymbolizer(sldSymb));
                    SimpleFeatureType featureType = DataUtilities.createType("TYPE", "the_geom:MultiPoint,name:\"\",tag:\"\"");
                    ListFeatureCollection collection = new ListFeatureCollection(featureType);
                    features.put(layerType, collection);
                }
                features.get(layerType).add(mapDataUtil.createFeature(obj, "the_geom:MultiPoint,name:\"\",tag:\"\""));
                //   layers.add(new FeatureLayer(features.get(layerType), styles.get(layerType)));
            }
            for (Map.Entry<String, Style> entry : styles.entrySet()) {
                layers.add(new FeatureLayer(features.get(entry.getKey()), entry.getValue()));
            }
            features.clear();
            styles.clear();

            for (Map<String, Object> map : lineList) {
                String sldSymb = null;
                Object[] obj = new Object[3];
                obj[0] = map.get("geometry");
                obj[1] = map.get("name");
                obj[2] = map.get("type");
                if (obj[0] != null && !obj[0].toString().isEmpty() && !(reader.read(map.get("geometry").toString())).isValid()) {
                    return layers;
                }
                if (obj[1] != null && !obj[1].toString().isEmpty() && !(CommonUtil.isValidCharacters(obj[1].toString()))) {
                    return layers;
                }
                if (obj[2] != null && !obj[2].toString().isEmpty() && !(CommonUtil.isValidCharacters(obj[2].toString()))) {
                    return layers;
                }

                String status = Objects.toString(map.get("status"));
                if (status.equalsIgnoreCase("D")) {
                    continue;
                }

                String layerType = Objects.toString(map.get("type"));

                if (!styles.containsKey(layerType)) {
                    pstm.setString(1, layerType);
                    pstm.setString(2, "MultiLineString");

                    ResultSet rs = pstm.executeQuery();
                    if (rs.next()) {
                        sldSymb = rs.getString("sld_symbolizers");
                    }
                    rs.close();
                    if (sldSymb == null || sldSymb.trim().isEmpty()) {
                        sldSymb = mapDataUtil.getSLDSymbolizer("DEFAULT_LINESTRING_LAYER");
                    }

                    styles.put(layerType, mapDataUtil.getStyleFromSymbolizer(sldSymb));
                    SimpleFeatureType featureType = DataUtilities.createType("TYPE", "the_geom:MultiLineString,name:\"\",tag:\"\"");
                    ListFeatureCollection collection = new ListFeatureCollection(featureType);
                    features.put(layerType, collection);
                }
                features.get(layerType).add(mapDataUtil.createFeature(obj, "the_geom:MultiLineString,name:\"\",tag:\"\""));
                //  layers.add(new FeatureLayer(features.get(layerType), styles.get(layerType)));
            }
            for (Map.Entry<String, Style> entry : styles.entrySet()) {
                layers.add(new FeatureLayer(features.get(entry.getKey()), entry.getValue()));
            }
            features.clear();
            styles.clear();

            for (Map<String, Object> map : polygonList) {
                String sldSymb = null;
                Object[] obj = new Object[3];
                obj[0] = map.get("geometry");
                obj[1] = map.get("name");
                obj[2] = map.get("type");
                if (obj[0] != null && !obj[0].toString().isEmpty() && !(reader.read(map.get("geometry").toString())).isValid()) {
                    return layers;
                }
                if (obj[1] != null && !obj[1].toString().isEmpty() && !(CommonUtil.isValidCharacters(obj[1].toString()))) {
                    return layers;
                }
                if (obj[2] != null && !obj[2].toString().isEmpty() && !(CommonUtil.isValidCharacters(obj[2].toString()))) {
                    return layers;
                }
                String layerType = Objects.toString(map.get("type"));

                if (!styles.containsKey(layerType)) {
                    pstm.setString(1, layerType);
                    pstm.setString(2, "MultiPolygon");

                    ResultSet rs = pstm.executeQuery();
                    if (rs.next()) {
                        sldSymb = rs.getString("sld_symbolizers");
                    }
                    rs.close();
                    if (sldSymb == null || sldSymb.trim().isEmpty()) {
                        sldSymb = mapDataUtil.getSLDSymbolizer("DEFAULT_POLYGON_LAYER");
                    }

                    styles.put(layerType, mapDataUtil.getStyleFromSymbolizer(sldSymb));
                    SimpleFeatureType featureType = DataUtilities.createType("TYPE", "the_geom:MultiPolygon,name:\"\",tag:\"\"");
                    ListFeatureCollection collection = new ListFeatureCollection(featureType);
                    features.put(layerType, collection);
                }
                features.get(layerType).add(mapDataUtil.createFeature(obj, "the_geom:MultiPolygon,name:\"\",tag:\"\""));
            }
            for (Map.Entry<String, Style> entry : styles.entrySet()) {
                layers.add(new FeatureLayer(features.get(entry.getKey()), entry.getValue()));
            }
            features.clear();
            styles.clear();
            pstm.close();
        } catch (SQLException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        return layers;
    }

    private List<Map<String, String>> getGisCodesAsList( WMSParms parms) {
        Polygon boundingBox = JTS.toGeometry(parms.getBbox());
        return  vvvvService.getGisCodesAsList(parms.getGisCodes(), parms.getSkipGisCodes(), parms.getVsrNos(),
                parms.getSkipGisCodes(), parms.getCqlFilter(), boundingBox.toText(), parms.getSrs());
    }

    private String getOverlayCodes(WMSParms parms) {

        if (!parms.isEmpty(parms.getOverlayCodes())) {
            String overlayCodes = "";
            for (String code : parms.getOverlayCodes().split(",")) {

                overlayCodes += "'" + code + "',";

            }
            overlayCodes += "'-1'";
            return overlayCodes;
        } else {
            return "";
        }

    }

    private MathTransform getGeotransform(WMSParms parms, String source) {
        MathTransform tr = null;
        if (parms.isEmpty(parms.getSrs())) {
            return null;
        }
        try {

            if (source.startsWith("EPSG")) {
                CoordinateReferenceSystem sourceCRS = CRS.decode(source);
                CoordinateReferenceSystem targetCRS = CRS.decode(parms.getSrs());
                tr = CRS.findMathTransform(sourceCRS, targetCRS);
            } else {
                CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
                CoordinateReferenceSystem targetCRS = CRS.decode(parms.getSrs());
                tr = mf.createFromWKT(source);
                tr = mf.createConcatenatedTransform(tr, CRS.findMathTransform(sourceCRS, targetCRS));
            }
        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tr;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public List<Object[]> fetchPlotMaps(WMSParms parms, List gisCodes) {

        try {
            Polygon bbox = JTS.toGeometry(parms.getBbox());
            List<Object[]> list = new ArrayList<Object[]>();
            List<Object[]> stmParms = new ArrayList<Object[]>();
            MathTransform tr = null;
            int[] subStrIdx = TablePartition.getSubstrRange4Java(stateDataProvider.getStateCode());
            for (int i=0; i<gisCodes.size(); i++) {
                Object[] mapObject = (Object[])  gisCodes.get(i);
                stmParms.clear();
                String gisCode = (String) mapObject[0];
                String srs = (String) mapObject[1];
                if (srs != null && !srs.isEmpty()) {
                    srs = srs.replace("EPSG:", "");
                    tr = getGeotransform(parms, srs);
                }

                List<Map<String, String>> resultList = khasramapService.fetchPlotMaps(stateDataProvider.getStateCode(),
                        gisCode, srs, parms.getSrs(), parms.getPlotIds(), parms.getPlotNos(), bbox.toText());

                for (Map<String, String> resultRow : resultList) {
                    Geometry geom = reader.read(resultRow.get("the_geom"));
                    if (!isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                        geom = JTS.transform(geom, tr);
                    }
                    Object[] obj = new Object[3];
                    obj[0] = geom;
                    obj[1] = resultRow.get("kide");
                    obj[2] = resultRow.get("bhucode");
                    list.add(obj);
                }
            }

            return list;
        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> dynaReferencedlayers(String gisCodes, String trWkt, WMSParms parms) {

        try {
            List<Object[]> data = new ArrayList<Object[]>();

            MathTransformFactory mf = ReferencingFactoryFinder.getMathTransformFactory(null);
            WKTReader reader = new WKTReader(JTSFactoryFinder.getGeometryFactory(null));
            MathTransform tr = mf.createFromWKT(trWkt);

            if (levelDecoder != null && gisCodes != null && !levelDecoder.isGisCodeValid(gisCodes)) {
                return null;
            }

            List<Khasramap> khasras = khasramapService.findAllByBhucode(stateDataProvider.getStateCode(), gisCodes);

            for (Khasramap khasramap : khasras) {
                Object[] obj = new Object[3];
                obj[0] = JTS.transform(reader.read(khasramap.getWkbGeometry()), tr);
                obj[1] = khasramap.getKide();
                obj[2] = "";
                data.add(obj);
            }

            return data;

        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Layer> createOverlayLayers(WMSParms parms, List<Map<String, String>> gisCodes) {
        List<Layer> layers = new ArrayList<Layer>();
   /*     MathTransform tr = null;
        try {

            String overlayCodes = getOverlayCodes(parms);
            Connection con = conMgr.getVectorConnection();
            String sql1 = "SELECT * FROM layer_master WHERE 1=1 ";
            if (overlayCodes != null && (!overlayCodes.isEmpty())) {
                sql1 += " AND id IN(" + overlayCodes + ") ";
            }
            QueryRunner qr = new QueryRunner();
            ResultSetHandler rsh = new MapListHandler();

//            int[] subStrIdx = TablePartition.getSubstrRange4Java(conMgr.getStateDataProvider().getStateCode());
            List<Map<String, Object>> layerMaster = (List<Map<String, Object>>) qr.query(con, sql1, rsh);
            for (Map<String, Object> masterRow : layerMaster) {

                String id = masterRow.get("id").toString();
                String style1 = masterRow.get("sld_symbolizers").toString();
                String extraAttributes = masterRow.get("extra_attributes").toString();
                String attributeNames[] = DataUtilities.attributeNames(DataUtilities.createType("TYPE", "the_geom:Point,name:\"\",tag:\"\"," + extraAttributes));

                for (Map<String, String> row : gisCodes) {
                    try {
                        String gisCode = row.get("gis_code");

                        String srs = row.get("srs");
                        if (srs != null && !srs.isEmpty()) {
                            srs = srs.replace("EPSG:", "");
                            tr = getGeotransform(parms, row.get("srs"));
                        }
                        String sql = "SELECT ";

                        if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                            String toSrs = parms.getSrs().replace("EPSG:", "");
                            sql += " st_astext(st_transform(st_setsrid(wkb_geometry," + srs + ")," + toSrs + ")) the_geom,";
                        } else {
                            sql += " st_astext(wkb_geometry) the_geom, ";
                        }
                        sql += " feature_name, attributes ";
                        int[] subStrIdx = TablePartition.getSubstrRange4Java(stateDataProvider.getStateCode());
                        if ("MultiPoint".equals(masterRow.get("geometry_type"))) {
                            if (style1 == null || style1.trim().isEmpty()) {
                                style1 = mapDataUtil.getSLDSymbolizer("DEFAULT_POINT_LAYER");
                            }

                            sql += " FROM   multipoint_layer";

                        } else if ("MultiLineString".equals(masterRow.get("geometry_type"))) {
                            if (style1 == null || style1.trim().isEmpty()) {
                                style1 = mapDataUtil.getSLDSymbolizer("DEFAULT_LINESTRING_LAYER");
                            }
                            sql += " FROM   multilinestring_layer";

                        } else if ("MultiPolygon".equals(masterRow.get("geometry_type"))) {
                            if (style1 == null || style1.trim().isEmpty()) {
                                style1 = mapDataUtil.getSLDSymbolizer("DEFAULT_POLYGON_LAYER");
                            }
                            sql += " FROM   multipolygon_layer";

                        }

                        sql += "_" + gisCode.substring(subStrIdx[0], subStrIdx[1])  + " WHERE bhucode =? AND layer_type=? ";
                        Polygon bbox = JTS.toGeometry(parms.getBbox());
                        if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                            sql += " AND st_setsrid(wkb_geometry," + srs + ") && st_transform(st_setsrid(ST_GeomFromText('" + bbox + "')," + parms.getSrs().replace("EPSG:", "") + ")," + srs + ")  ";
                        } else if (parms.isEmpty(parms.getSrs())) {
                            sql += " AND  wkb_geometry && ST_GeomFromText('" + bbox + "')  ";

                        }
                        sql += " ORDER BY bhucode";

                        List<Map<String, Object>> layerRows = (List<Map<String, Object>>) qr.query(con, sql, rsh, gisCode, Objects.toString(masterRow.get("layer_type")));

                        ArrayList<Object[]> rowList = new ArrayList<Object[]>();

                        for (Map<String, Object> layerRow : layerRows) {

                            String attributes = (String) layerRow.get("attributes");
                            ArrayList<String> list = new ArrayList<String>();
                            Geometry geom = reader.read((String) layerRow.get("the_geom"));
                            if (!isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                                geom = JTS.transform(geom, tr);
                            }
                            list.add(geom.toText());
                            list.add((String) layerRow.get("feature_name"));
                            list.add(id);
                            if (! stateDataProvider.getStateCode().equals("30"))
                                for (String attributeName : attributeNames) {
                                    if ("the_geom".equals(attributeName)
                                            || "name".equals(attributeName)
                                            || "tag".equals(attributeName)) {
                                        continue;
                                    }
                                    list.add(AttributeXPathParser.getValue(attributes, attributeName));
                                }

                            rowList.add(list.toArray());
                        }

                        if (rowList.size() > 0) {
                            Layer layer = new FeatureLayer(mapDataUtil.fetchFeatures(rowList, "the_geom:" + masterRow.get("geometry_type") + ",name:\"\",tag:\"\"," + extraAttributes), mapDataUtil.getStyleFromSymbolizer(style1));
                            layers.add(layer);
                        }
                    } catch (Exception x) {
                        Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, x);
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        return layers;
    }

    private List<Layer> createDerivedLayers(WMSParms parms) {
        List<Layer> layers = new ArrayList<Layer>();
/*
        try {

//            SimpleFeatureCollection features = null;
            ListFeatureCollection tempcol = null;

            String gisCodes = parms.getGisCodes();
            String plotId = parms.getplotId();
            String layerIds = parms.getlayercodes();

            Connection vCon = conMgr.getVectorConnection();

            PreparedStatement stm = vCon.prepareStatement("SELECT * FROM derived_layers WHERE id IN (SELECT * FROM unnest(?))");
            stm.setArray(1, vCon.createArrayOf("text", layerIds.split(",")));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String className = rs.getString("implemented_class");
                String symbolizer = rs.getString("sld_symbolizers");

                DerivedLayer devLyr = (DerivedLayer) conMgr.loadLocalClass(className);
                if (devLyr != null) {
                    List<Object[]> list = devLyr.fetchFeatures(conMgr, "public", gisCodes, plotId);
                    if (list != null && list.size() > 0) {
                        for (Object[] c : list) {
                            if (c != null && c.length > 1) {
                                tempcol = (ListFeatureCollection) c[0];

                            }

                        }
                    }
                }
                try {
                    if (tempcol != null) {
                        Layer layer = new FeatureLayer(tempcol, mapDataUtil.getStyleFromSymbolizer(symbolizer));
                        layers.add(layer);
                    }
                } catch (Exception x) {
                    Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, x);
                }

            }
            rs.close();
            stm.close();

        } catch (SQLException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        return layers;
    }

    private List<Layer> createThemeLayers(WMSParms parms, List<Map<String, String>> gisCodes) {
    /*    List<Layer> layers = new ArrayList<Layer>();

        try {

            Connection vCon = conMgr.getVectorConnection();
            int levelCount = levelDecoder.getVsrLevelCount() + levelDecoder.getMapLevelCount();

            PreparedStatement stm2 = null;
            PreparedStatement stm = vCon.prepareStatement("SELECT * FROM  themes WHERE id IN (SELECT * FROM unnest(?))");
            stm.setArray(1, vCon.createArrayOf("text", parms.getThemeCodes().split(",")));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String query = rs.getString("query");
                String sld = rs.getString("sld_symbolizers");
                String db = rs.getString("db");
                String geomType = rs.getString("geometry_type");
                if (sld == null || sld.trim().isEmpty()) {
                    if (geomType.equalsIgnoreCase("Polygon") || geomType.equalsIgnoreCase("MultiPolygon")) {
                        sld = mapDataUtil.getSLDSymbolizer("DEFAULT_POLYGON_LAYER");
                    } else if (geomType.equalsIgnoreCase("LineString") || geomType.equalsIgnoreCase("MultiLineString")) {
                        sld = mapDataUtil.getSLDSymbolizer("DEFAULT_LINE_LAYER");
                    } else if (geomType.equalsIgnoreCase("Point") || geomType.equalsIgnoreCase("MultiPoint")) {
                        sld = mapDataUtil.getSLDSymbolizer("DEFAULT_POINT_LAYER");
                    }
                }
                ArrayList<Object[]> rowList = new ArrayList<Object[]>();
                int[] subStrIdx = TablePartition.getSubstrRange4Java(stateDataProvider.getStateCode());
                MathTransform tr = null;
                for (Map<String, String> row : gisCodes) {

                    String gisCode = row.get("gis_code");
                    String srs = row.get("srs");
                    if (srs != null && !srs.isEmpty()) {
                        srs = srs.replace("EPSG:", "");
                        tr = getGeotransform(conMgr, parms, row.get("srs"));
                    }

                    if (gisCode != null && !gisCode.isEmpty() && !levelDecoder.isGisCodeValid(conMgr, gisCode)) {
                        return null;
                    }

                    String levels[] = levelDecoder.levelsFromGisCode(gisCode);
                    String vsrNo = levelDecoder.createVsrno(levels);
                    for (int j = 1; j <= levelCount; j++) {
                        query = query.replaceAll("\\$level" + j, levels[j - 1]);
                    }

                    query = query.replaceAll("\\$giscode", gisCode);
                    query = query.replaceAll("\\$vsrno", vsrNo);

                    PreparedStatement stm1 = null;

                    ResultSet rs1 = null;
                    if ("1".equals(db)) {
                        ArrayList<String> kideForTheme = null;
                        kideForTheme = stateDataProvider.getKideForTheme(query, gisCode, false);

                        if (!kideForTheme.isEmpty()) {

                            String sql = "SELECT ";
                            if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                                String toSrs = parms.getSrs().replace("EPSG:", "");
                                sql += "st_astext(st_transform(st_setsrid(st_simplify(wkb_geometry,0.5)," + srs + ")," + toSrs + ")) the_geom,";
                            } else {
                                sql += " st_astext(wkb_geometry) the_geom, ";
                            }
                            sql += " kide, bhucode FROM khasramap_" + gisCode.substring(subStrIdx[0], subStrIdx[1]) + "   WHERE bhucode= ? AND kide IN (SELECT * FROM unnest(?)) ";

                            Polygon bbox = JTS.toGeometry(parms.getBbox());
                            if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                                sql += " AND st_setsrid(wkb_geometry," + srs + ") && st_transform(st_setsrid(ST_GeomFromText('" + bbox + "')," + parms.getSrs().replace("EPSG:", "") + ")," + srs + ")  ";
                            } else if (parms.isEmpty(parms.getSrs())) {
                                sql += " AND  wkb_geometry && ST_GeomFromText('" + bbox + "')  ";

                            }

                            stm2 = vCon.prepareStatement(sql);

                            Array array = vCon.createArrayOf("text", kideForTheme.toArray());
                            stm2.setString(1, gisCode);
                            stm2.setArray(2, array);
                            rs1 = stm2.executeQuery();
                        }
                    } else {
                        stm1 = vCon.prepareStatement(query);
                        rs1 = stm1.executeQuery();
                        if (rs1.getMetaData().getColumnCount() == 1) {
                            String ins = "";
                            while (rs1.next()) {
                                ins += rs1.getString(1) + ",";
                            }
                            ins += "-1";
                            rs1.close();
                            stm1.close();

                            String sql = "SELECT ";
                            if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                                String toSrs = parms.getSrs().replace("EPSG:", "");
                                sql += "st_astext(st_transform(st_setsrid(st_simplify(wkb_geometry,0.5)," + srs + ")," + toSrs + ")) the_geom,";
                            } else {
                                sql += " st_astext(wkb_geometry) the_geom, ";
                            }
                            sql += " kide, bhucode FROM khasramap_" + gisCode.substring(subStrIdx[0], subStrIdx[1]) + "   WHERE bhucode= ? AND kide IN (SELECT * FROM unnest(?)) ";

                            Polygon bbox = JTS.toGeometry(parms.getBbox());
                            if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                                sql += " AND st_setsrid(wkb_geometry,?) && st_transform(st_setsrid(ST_GeomFromText(?),? ),?)  ";
                            } else if (parms.isEmpty(parms.getSrs())) {
                                sql += " AND  wkb_geometry && ST_GeomFromText(?)  ";

                            }

                            stm2 = vCon.prepareStatement(sql);

                            stm2.setString(1, gisCode);
                            stm2.setArray(2, vCon.createArrayOf("text", ins.split(",")));
                            if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                                stm2.setInt(3, Integer.parseInt(srs));
                                stm2.setString(4, bbox.toString());
                                stm2.setInt(5, Integer.parseInt(parms.getSrs().replace("EPSG:", "")));
                                stm2.setInt(6, Integer.parseInt(srs));

                            } else if (parms.isEmpty(parms.getSrs())) {
                                stm2.setString(3, bbox.toString());

                            }
                            rs1 = stm2.executeQuery();

                        }

                    }

                    if (null != rs1) {
                        while (rs1.next()) {
                            Geometry geom = reader.read((String) rs1.getString(1));
                            if (!isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                                geom = JTS.transform(geom, tr);
                            }
                            rowList.add(new Object[]{geom, rs1.getString(1), rs1.getString(2)});
                        }
                        rs1.close();
                    }
                    if (null != stm2) {
                        stm2.close();
                    }

                    if (rowList.size() > 0) {
                        Layer layer = new FeatureLayer(mapDataUtil.fetchFeatures(rowList, "the_geom:" + geomType + ",name:\"\",tag:\"\""), mapDataUtil.getStyleFromSymbolizer(conMgr, sld));
                        layers.add(layer);

                    }
                }
            }
            rs.close();
            stm.close();

        } catch (SQLException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        return null;
    }

    public List<Object[]> fetchSameOwnerPlotMaps(WMSParms parms, List<Map<String, String>> gisCodes) {

    /*    try {

            List<Object[]> list = new ArrayList<Object[]>();
            QueryRunner qr = new QueryRunner();
            ResultSetHandler rsh = new MapListHandler();
            MathTransform tr = null;
            int[] subStrIdx = TablePartition.getSubstrRange4Java(stateDataProvider.getStateCode());

            for (Map<String, String> row : gisCodes) {

                String gisCode = row.get("gis_code");

                String srs = row.get("srs");
                if (srs != null && (!srs.isEmpty())) {
                    srs = srs.replace("EPSG:", "");
                    tr = getGeotransform(parms, row.get("srs"));
                }
                String sql = "SELECT ";
                if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                    String toSrs = parms.getSrs().replace("EPSG:", "");
                    sql += "st_astext(st_transform(st_setsrid(wkb_geometry," + srs + ")," + toSrs + ")) the_geom,";
                } else {
                    sql += " st_astext(wkb_geometry) the_geom, ";
                }
                sql += " kide, bhucode FROM khasramap_" + gisCode.substring(subStrIdx[0], subStrIdx[1]) + " WHERE bhucode= '" + gisCode + "' ";

                if (!parms.isEmpty(parms.getPlotNos())) {

                    String[] levels = levelDecoder.levelsFromGisCode(gisCode);
                    String vsrNo = levelDecoder.createVsrno(levels);

                    String[] plots = stateDataProvider.getAllOwnerKhasras(null, vsrNo, parms.getPlotNos());

                    String plotNos = "";
                    if (plots != null) {
                        for (String code : plots) {
                            if (!Objects.equals(code, parms.getPlotNos())) {
                                plotNos += "'" + code + "',";
                            }
                        }
                    }
                    plotNos += "'-1'";
                    sql += " AND kide  IN(" + plotNos + ")  ";
                }

                Polygon bbox = JTS.toGeometry(parms.getBbox());
                if (isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                    sql += " AND st_setsrid(wkb_geometry," + srs + ") && st_transform(st_setsrid(ST_GeomFromText('" + bbox + "')," + parms.getSrs().replace("EPSG:", "") + ")," + srs + ")  ";
                } else {
                    sql += " AND  wkb_geometry && ST_GeomFromText('" + bbox + "')  ";

                }

                sql += " ORDER BY bhucode, kide";

                List<Map<String, Object>> data = (List<Map<String, Object>>) qr.query(conMgr.getVectorConnection(), sql, rsh);
                for (Map<String, Object> map : data) {
                    Geometry geom = reader.read((String) map.get("the_geom"));

                    if (!isInteger(srs) && !parms.isEmpty(parms.getSrs())) {
                        geom = JTS.transform(geom, tr);
                    }

                    Object[] obj = new Object[3];
                    obj[0] = geom;
                    obj[1] = map.get("kide");
                    obj[2] = map.get("bhucode");
                    list.add(obj);
                }

            }

            return list;

        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return null;

    }

    public List<Layer> createFMBLayers(WMSParms parms) {
        return null;
      /*  List<Layer> layers = new ArrayList<Layer>();

        try {
            FMBLayerBuilder layerBuilder = null;
            if (!parms.isEmpty(parms.getData())) {
                layerBuilder = new FMBLayerBuilder(conMgr, parms.getData(), true);
            } else if (parms.getGisCodes().startsWith("S")) {
                layerBuilder = new FMBLayerBuilder(conMgr, parms.getGisCodes());
            }
            List<Object[]> layerObjects = layerBuilder.getAllLayers();
            if (layerObjects != null && layerObjects.size() > 0) {
                for (Object[] c : layerObjects) {
                    if (c != null && c.length > 1) {
                        Layer layer = new FeatureLayer((ListFeatureCollection) c[0], (Style) c[1]);
                        layers.add(layer);
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return layers;*/
    }

    public List<Object[]> createMosaicedSubDivLayers(WMSParms parms) {
        return null;
      /*MosaicedSubdivLayerBuilder mosaicSubdivBuilder = new MosaicedSubdivLayerBuilder(conMgr, parms.getGisCodes(), parms.getSrs());
        return mosaicSubdivBuilder.getSubdivLayers();*/
    }

    public List<Object[]> fetchVillageBoundaryMaps(WMSParms parms) {
     /*   try {
            List<Object[]> list = new ArrayList<Object[]>();

            String sql = "SELECT gis_code, vsrno,village_hi, ";

            if (!parms.isEmpty(parms.getSrs())) {
                sql += " st_astext(st_transform(st_setsrid(wkb_geometry,4326)," + parms.getSrs().replace("EPSG:", "") + ")) the_geom,";
            } else {
                sql += " st_astext(wkb_geometry) the_geom, ";
            }

            sql += " geotransform FROM vvvv WHERE 1=1";

            if (!parms.isEmpty(parms.getGisCodes())) {
                String gisCodes = "";
                for (String code : parms.getGisCodes().split(",")) {
                    gisCodes += "'" + code + "',";
                }
                gisCodes += "'-1'";
                sql += " AND gis_code  IN(" + gisCodes + ")  ";
            }

            if (!parms.isEmpty(parms.getSkipGisCodes())) {
                String skipCodes = "";
                for (String code : parms.getSkipGisCodes().split(",")) {
                    skipCodes += "'" + code + "',";
                }
                skipCodes += "'-1'";
                sql += " AND gis_code NOT IN(" + skipCodes + ")  ";
            }

            if (!parms.isEmpty(parms.getVsrNos())) {
                String vsrNos = "";
                for (String code : parms.getVsrNos().split(",")) {
                    vsrNos += "'" + code + "',";
                }
                vsrNos += "'-1'";
                sql += " AND vsrno  IN(" + vsrNos + ")  ";
            }

            if (!parms.isEmpty(parms.getSkipVsrNos())) {
                String skipCodes = "";
                for (String code : parms.getSkipVsrNos().split(",")) {
                    skipCodes += "'" + code + "',";
                }
                skipCodes += "'-1'";
                sql += " AND vsrno NOT IN(" + skipCodes + ")  ";
            }

            if (!parms.isEmpty(parms.getCqlFilter())) {
                sql += " AND " + parms.getCqlFilter();
            }

            String cql =
                    Settings.getSettingsValue(conMgr, "VILLAGE_BOUNDARY_EXTRA_FILTER_CQL");

            if (cql != null && isValidFilter(cql)) {

                cql = cql.replaceAll("(?i)strsubstring", "substring");
                sql += " AND " + cql;
            }

            Polygon bbox = JTS.toGeometry(parms.getBbox());
            if (parms.isEmpty(parms.getSrs())) {
                sql += " AND  wkb_geometry && ST_GeomFromText('" + bbox + "')  ";
            } else {
                sql += " AND st_transform(st_setsrid(wkb_geometry,4326)," + parms.getSrs().replace("EPSG:", "") + ") && ST_GeomFromText('" + bbox + "')  ";
            }

            QueryRunner qr = new QueryRunner();
            ResultSetHandler rsh = new MapListHandler();
            List<Map<String, Object>> data = (List<Map<String, Object>>) qr.query(conMgr.getVectorConnection(), sql, rsh);
            for (Map<String, Object> map : data) {

                if (map.get("village_hi") == null || Objects.toString(map.get("village_hi"), "").isEmpty()) {
                    CodeValueObj vilName = null;
                    try {
                        vilName = levelReader.fetchLevelValue(levelDecoder.getVsrLevelCount(), levelDecoder.levelsFromGisCode((String) map.get("gis_code")));
                        map.put("village_hi", vilName.getValue());

                        conMgr.getVectorConnection().setAutoCommit(false);
                        PreparedStatement pstm = conMgr.getVectorConnection().prepareStatement("UPDATE vvvv SET village_hi=? WHERE gis_code=?");
                        pstm.setString(1, vilName.getValue());
                        pstm.setString(2, Objects.toString(map.get("gis_code")));
                        pstm.executeUpdate();
                        conMgr.getVectorConnection().commit();
                    } catch (Exception x) {
                        Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, x);
                    }
                }
                Object[] obj = new Object[3];
                obj[0] = map.get("the_geom");
                if (map.get("village_hi") != null) {
                    obj[1] = map.get("village_hi");
                } else {
                    obj[1] = map.get("gis_code");
                }

                obj[2] = map.get("gis_code");

                list.add(obj);

            }

            return list;

        } catch (Exception ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return null;
    }

    public static boolean isValidFilter(String filter) {

        try {
            ECQL.toFilter(filter);
            return true;
        } catch (CQLException ex) {
            Logger.getLogger(WMSLayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
