package in.nic.lrisd.bhunakshav6.bhunakshacommon.bean;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTS;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.styling.Style;
import org.geotools.util.factory.GeoTools;
import org.jvnet.hk2.annotations.Service;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FeatureCollectionHelper {

    @Autowired
    private DefaultFeatureCollection fc;

    private SimpleFeatureType featureType = null;
    private SimpleFeatureBuilder featureBuilder = null;

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

    public FeatureCollectionHelper(Geometries geom) {
        try {
            featureType = DataUtilities.createType("FeatureType",
                    "the_geom:" + geom.getName() + ","
                            + "name:String,"
                            + "feature_name:String,"
                            //  + "link:String," //Can be used to identify parent eg: a point can have base line as parent
                            + "category:String"
            );
            featureBuilder = new SimpleFeatureBuilder(featureType);
        } catch (SchemaException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public SimpleFeature createFeature(Geometry geom, String label, String featureName, String category) {

//        featureBuilder.reset();
//        featureBuilder.add(geom);
//        featureBuilder.add(label);
//        featureBuilder.add(featureName);
//        //featureBuilder.add(linkName);
//        featureBuilder.add(category);
//        SimpleFeature f = featureBuilder.buildFeature(null);
        SimpleFeature f = DataUtilities.template(featureType, new Object[]{geom, label, featureName, category});
        return f;
    }

    public synchronized void addFeature(SimpleFeature feature, boolean replace) {
        if (replace) {
            removeFeatureByName((String) feature.getAttribute("feature_name"));
        }
        fc.add(feature);

    }

    public synchronized String addFeature(Geometry geom, String label, String featureName, String category, boolean replace) {
        if (replace) {
            removeFeatureByName(featureName);
        }
        //System.out.println();
        featureBuilder.reset();
        featureBuilder.add(geom);
        featureBuilder.add(label);
        featureBuilder.add(featureName);
        //featureBuilder.add(linkName);
        featureBuilder.add(category);
        SimpleFeature f = featureBuilder.buildFeature(null);
        //Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, "ADDED : " + f);
        fc.add(f);

        return f.getID();
    }

    public synchronized String replaceFeture(String fid, Geometry geom, String label, String featureName, String category) {
        if(fid==null) fid="";
        try {
            Set<FeatureId> fids = new HashSet<FeatureId>();
            fids.add(new FeatureIdImpl(fid));
            Filter filter1 = ff.id(fids);
            SimpleFeatureCollection subFc = fc.subCollection(filter1);
            SimpleFeatureIterator it = subFc.features();
            if (it.hasNext()) {
                SimpleFeature f = it.next();
                f.setDefaultGeometry(geom);
                f.setAttribute("name", label);
                f.setAttribute("feature_name", featureName);
                f.setAttribute("category", category);
                Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, "Changed : " + f.getAttribute("feature_name") + " : " + f.getAttribute("category"));
                it.close();
                return fid;
            } else {
                featureBuilder.reset();
                featureBuilder.add(geom);
                featureBuilder.add(label);
                featureBuilder.add(featureName);
                //featureBuilder.add(linkName);
                featureBuilder.add(category);
                SimpleFeature f = featureBuilder.buildFeature(null);
                Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, "ADDED : " + f.getAttribute("feature_name") + " : " + f.getAttribute("category"));
                fc.add(f);

                return f.getID();
            }

        } catch (Exception ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public synchronized String replaceFeture(SimpleFeature f) {

        try {
            String fid = f.getID();
            Set<FeatureId> fids = new HashSet<FeatureId>();
            fids.add(new FeatureIdImpl(fid));
            Filter filter1 = ff.id(fids);
            SimpleFeatureCollection subFc = fc.subCollection(filter1);
            SimpleFeatureIterator it = subFc.features();
            if (it.hasNext()) {
                SimpleFeature f1 = it.next();
                f1.setDefaultGeometry(f.getDefaultGeometry());
                f1.setAttribute("name", f.getAttribute("name"));
                f1.setAttribute("feature_name", f.getAttribute("feature_name"));
                f1.setAttribute("category", f.getAttribute("category"));

                //  System.out.println("Changed : " + f.getAttribute("feature_name") + " : " + f.getAttribute("category")+"=="+f.getID()+" == "+f1.getID());
                it.close();
                return fid;
            }

        } catch (Exception ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }


    public synchronized void removeFeatureByFilter(String filter) {
        try {
            Filter filter1 = CQL.toFilter(filter);
            SimpleFeatureCollection subFc = fc.subCollection(filter1);
            fc.removeAll(new DefaultFeatureCollection(subFc));

        } catch (CQLException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void removeFeatureByName(String featureName) {
        try {
            Filter filter1 = CQL.toFilter("feature_name='" + featureName + "'");
            SimpleFeatureCollection subFc = fc.subCollection(filter1);
            fc.removeAll(new DefaultFeatureCollection(subFc));

//        SimpleFeatureIterator it = fc.features();
//        while (it.hasNext()) {
//            SimpleFeature f = it.next();
//            if (f.getAttribute("feature_name").equals(featureName)) {
//                System.out.println("Removing : " + f);
//                fc.remove(f);
//                break;
//            }
//        }
//        it.close();
        } catch (CQLException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //    public synchronized void removeFeatureByID(String id) {
//        try {
//           Set<FeatureId> fids = new HashSet<FeatureId>();
//            fids.add(new FeatureIdImpl(id));
//            Filter filter1 = ff.id(fids);
//            SimpleFeatureCollection subFc = fc.subCollection(filter1);
//            fc.removeAll(new DefaultFeatureCollection(subFc));
//
//        } catch (Exception ex) {
//            Logger.getLogger(FMBFeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public synchronized void changeFeatureGeometry(String id, Geometry geom) {
        try {
            Set<FeatureId> fids = new HashSet<FeatureId>();
            fids.add(new FeatureIdImpl(id));
            Filter filter1 = ff.id(fids);
            SimpleFeatureCollection subFc = fc.subCollection(filter1);
            SimpleFeatureIterator it = subFc.features();
            if (it.hasNext()) {
                it.next().setDefaultGeometry(geom);
            }
            it.close();

        } catch (Exception ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Layer getLayer(String filter, Style style, String layerName) {
        try {
            SimpleFeatureCollection subFc = null;
            if (filter == null || filter.isEmpty()) {
                subFc = fc.collection();
            } else {
                Filter filter1 = CQL.toFilter(filter);
                subFc = fc.subCollection(filter1);
            }
            return new FeatureLayer(subFc, style, layerName);

        } catch (CQLException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public SimpleFeatureCollection featuresWithinDistance(Geometry fromGeom, double distance) {
        try {
            Filter filter = CQL.toFilter("DWITHIN(the_geom, " + fromGeom + ", " + distance + ")");
            return fc.subCollection(filter);
        } catch (CQLException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void clear() {
        fc.clear();

    }

    public SimpleFeature getFeatureById(String fid) {

        SimpleFeature result = null;
        try {
            Set<FeatureId> fids = new HashSet<FeatureId>();
            fids.add(new FeatureIdImpl(fid));
            Filter filter1 = ff.id(fids);
            SimpleFeatureCollection col = fc.subCollection(filter1);
            SimpleFeatureIterator it = col.features();

            if (it.hasNext()) {
                result = it.next();
            }
            it.close();

        } catch (Exception ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public Geometry getGeometryById(String fid) {

        SimpleFeature result = null;
        try {
            Set<FeatureId> fids = new HashSet<FeatureId>();
            fids.add(new FeatureIdImpl(fid));
            Filter filter1 = ff.id(fids);
            SimpleFeatureCollection col = fc.subCollection(filter1);
            SimpleFeatureIterator it = col.features();

            if (it.hasNext()) {
                result = it.next();
            }
            it.close();

        } catch (Exception ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (result != null) {

            return (Geometry) result.getDefaultGeometry();
        } else {
            return null;
        }
    }

    public SimpleFeature getFeatureByName(String featureName) {

        SimpleFeature result = null;
        try {
            Filter filter1 = CQL.toFilter("feature_name='" + featureName + "'");
            SimpleFeatureCollection col = fc.subCollection(filter1);
            SimpleFeatureIterator it = col.features();

            if (it.hasNext()) {
                result = it.next();
            }
            it.close();

        } catch (CQLException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public SimpleFeature getFeatureByGeometry(Geometry geom) {

        SimpleFeature result = null;
        SimpleFeatureIterator it = fc.features();
        while (it.hasNext()) {
            SimpleFeature f = it.next();
            try {
                if (geom.equals(f.getDefaultGeometry())) {
                    result = f;
                    break;
                }

            } catch (MismatchedDimensionException ex) {
                Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        it.close();
        return result;
    }

    public Geometry getGeometryByName(String featureName) {

        SimpleFeature result = null;
        try {
            Filter filter1 = CQL.toFilter("feature_name='" + featureName + "'");
            SimpleFeatureCollection col = fc.subCollection(filter1);
            SimpleFeatureIterator it = col.features();

            if (it.hasNext()) {
                result = it.next();
            }
            it.close();

        } catch (CQLException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (result != null) {

            return (Geometry) result.getDefaultGeometry();
        } else {
            return null;
        }
    }

    public SimpleFeatureCollection filteredSubList(String filter) {
        try {
//            SimpleFeatureIterator it = fc.features();
//            while(it.hasNext()){
//                System.out.println(it.next());
//            }
//            it.close();

            Filter filter1 = CQL.toFilter(filter);

            return fc.subCollection(filter1);
        } catch (CQLException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public SimpleFeatureCollection getAllFeatures() {
        try {
            return fc.collection();
        } catch (IOException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public int getCount() {
        try {

            return fc.getCount();
        } catch (IOException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public void transform(MathTransform mathTransform) {
        try {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, "COUNT {0}", fc.getCount());
        } catch (IOException ex) {
            Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        SimpleFeatureIterator it = fc.features();

        while (it.hasNext()) {
            try {
                SimpleFeature f = it.next();

                f.setDefaultGeometry(JTS.transform((Geometry) f.getDefaultGeometry(), mathTransform));


            } catch (MismatchedDimensionException ex) {
                Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformException ex) {
                Logger.getLogger(FeatureCollectionHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        it.close();

    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public SimpleFeatureBuilder getFeatureBuilder() {
        return featureBuilder;
    }

}
