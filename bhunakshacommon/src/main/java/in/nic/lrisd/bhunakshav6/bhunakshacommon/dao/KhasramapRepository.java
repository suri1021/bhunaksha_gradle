package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.CommonUtil;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Khasramap;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.globalsettings.TablePartition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class KhasramapRepository implements KhasramapDAO {

    private EntityManager entityManager;

    @Autowired
    public KhasramapRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Khasramap> findAllByBhucode(String state, String bhucode) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);
        String sqlQuery = "Select Cast(id as varchar) id" + ","
                + "st_astext(wkb_geometry) wkb_geometry, bhucode, kide, Cast(div_id as varchar) div_id, attributes,"
                + "last_updated, attributes_json, pniu, pnil, interior_point, ulpin_generation_date, ulpin_mode,"
                + "signature,signkey,dscno,signdate,attributesjson,attributejson"
                + " from " + tableid + " where bhucode=:bhucode order by kide";

        Query query = entityManager.createNativeQuery(sqlQuery, Khasramap.class);
        query.setParameter("bhucode", bhucode);
        List<Khasramap> theList = query.getResultList();
        return theList;
    }

    @Override
    public Khasramap findByBhucodeKide(String state, String bhucode, String kide) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);
        String sqlQuery = "Select Cast(id as varchar) id" + ","
                + "st_astext(wkb_geometry) wkb_geometry, bhucode, kide, Cast(div_id as varchar) div_id, attributes,"
                + "last_updated, attributes_json, pniu, pnil, interior_point, ulpin_generation_date, ulpin_mode,"
                + "signature,signkey,dscno,signdate,attributesjson,attributejson"
                + " from " + tableid + " where bhucode=:bhucode and kide=:kide order by kide";

        Query query = entityManager.createNativeQuery(sqlQuery, Khasramap.class);
        query.setParameter("bhucode", bhucode);
        query.setParameter("kide", kide);
        List<Khasramap> theList = query.getResultList();
        if (theList.size() > 0) return theList.get(0);
        return null;
    }

    @Override
    public Khasramap findById(String id) {
        Khasramap khasramap = entityManager.find(Khasramap.class, id);

        return khasramap;
    }

    @Override
    public Khasramap findByBhucodeId(String state, String bhucode, String id) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);
        String sqlQuery = "Select Cast(id as varchar) id" + ","
                + "st_astext(wkb_geometry) wkb_geometry, bhucode, kide, Cast(div_id as varchar) div_id, attributes,"
                + "last_updated, attributes_json, pniu, pnil, interior_point, ulpin_generation_date, ulpin_mode,"
                + "signature,signkey,dscno,signdate,attributesjson,attributejson"
                + " from " + tableid + " where bhucode=:bhucode and id=:id order by kide";

        Query query = entityManager.createNativeQuery(sqlQuery, Khasramap.class);
        query.setParameter("bhucode", bhucode);
        query.setParameter("id", id);
        List<Khasramap> theList = query.getResultList();
        if (theList.size() > 0) return theList.get(0);
        return null;
    }

    @Override
    public Khasramap findByIntersectionPoint(String state, String bhucode, String ptText) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);
        String sqlQuery = "Select Cast(id as varchar) id" + ","
                + "st_astext(wkb_geometry) wkb_geometry, bhucode, kide, Cast(div_id as varchar) div_id, attributes,"
                + "last_updated, attributes_json, pniu, pnil, interior_point, ulpin_generation_date, ulpin_mode,"
                + "signature,signkey,dscno,signdate,attributesjson,attributejson"
                + " from " + tableid + " where bhucode=:bhucode and  st_intersects(wkb_geometry, st_geomfromtext(:ptText)) ";

        Query query = entityManager.createNativeQuery(sqlQuery, Khasramap.class);
        query.setParameter("bhucode", bhucode);
        query.setParameter("ptText", ptText);
        List<Khasramap> theList = query.getResultList();
        if (theList.size() > 0) return theList.get(0);
        return null;
    }

    @Override
    public Map findBYInnerPointMapData(String state, String bhucode, String pointText) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);
        String sqlQuery = "SELECT id,CASE WHEN kide IS NULL THEN '' ELSE kide END, attributes_json || ('{\"_kide\":\"' || kide || '\"}' )::jsonb jsonbdata, pniu FROM khasramap"
                +tableid+" where bhucode=:bhucode AND  st_intersects(wkb_geometry, st_geomfromtext(:pointText)) ";

        Query query = entityManager.createNativeQuery(sqlQuery,Tuple.class);
        query.setParameter("bhucode", bhucode);
        query.setParameter("pointText", pointText);
        return DaoUtil.convertTupleToMap((Tuple) query.getSingleResult());
    }

    @Override
    public Map findMapDataCentroidByBhucodeId(String state, String bhucode, String kide, String id) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);

        String sqlQuery = "SELECT id, kide, st_astext(st_centroid(wkb_geometry)) centroid," +
                "attributes_json || ('{\"_kide\":\"' || kide || '\"}' )::jsonb jsonbdata " +
                "as attrs,pniu FROM khasramap"+ tableid +" WHERE bhucode=:bhucode AND kide=:kide";

        if (id != null && !id.isEmpty()) {
            sqlQuery += "and id=:id";
        }

        Query query = entityManager.createNativeQuery(sqlQuery, Tuple.class);
        query.setParameter("bhucode", bhucode);
        query.setParameter("kide", kide);

        if (id != null && !id.isEmpty()) {
            query.setParameter("id", id);
        }

        return DaoUtil.convertTupleToMap((Tuple) query.getSingleResult());
    }

    @Override
    public Timestamp findMaxLastUpdatedByBhucode(String state, String bhucode) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);

        String sqlQuery = "Select MAX(last_updated) as date from khasramap" + tableid + " WHERE bhucode=:bhucode";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("bhucode", bhucode);

        return (Timestamp) query.getSingleResult();
    }

    @Override
    public Double getArea(String state, String bhucode, String id) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);

        String sqlQuery = "Select st_area(wkb_geometry)"
                + " from " + tableid + " where bhucode=:bhucode and id=:id";

        Query query = entityManager.createQuery(sqlQuery);
        query.setParameter("bhucode", bhucode);
        query.setParameter("id", id);

        try {
            Double area = (Double) query.getSingleResult();
            return area;
        }
        catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Map getExtent(String state, String dbSchema, String bhucode, String id) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);
        String sqlQuery = "SELECT st_xmin(env) AS xmin , st_ymin(env) AS ymin, st_xmax(env) AS xmax, st_ymax(env) AS ymax FROM ( SELECT ST_Extent(wkb_geometry) AS env FROM "
                + dbSchema + ".khasramap" + tableid +" WHERE bhucode=:bhucode ";
        if (null != id && (!"".equals(id))) {
            sqlQuery += " AND id=:id";
        }
        sqlQuery += ") AS sub ";
        Query query = entityManager.createQuery(sqlQuery, Tuple.class);

        return (Map) query.getSingleResult();
    }

    @Override
    public List fetchPlotMaps(String state, String bhucode, String sourceSrs, String destSrs, List plotIds, List plotNos, String bboxText) {

        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);
        String toSrs = "";
        String sql = "SELECT ";
        if (CommonUtil.isInteger(sourceSrs) && !StringUtils.isEmpty(destSrs)) {
            toSrs = destSrs.replace("EPSG:", "");
            sql += " st_astext(st_transform(st_setsrid(wkb_geometry, :srs),:tosrs)) the_geom,";
        } else {
            sql += " st_astext(wkb_geometry) the_geom, ";
        }

        sql += " kide, bhucode FROM " + tableid + " WHERE bhucode= :bhucode ";

        if (! CollectionUtils.isEmpty(plotIds)) {
            sql += " AND id  IN (:plotIds)   ";
        }

        if (! CollectionUtils.isEmpty(plotNos)) {
            sql += " AND kide  IN (:plotNos) ) ";
        }

        if (CommonUtil.isInteger(sourceSrs) && !StringUtils.isEmpty(destSrs)) {
            sql += " AND st_setsrid(wkb_geometry,:srs ) && st_transform(st_setsrid(ST_GeomFromText(:bboxtext),:tosrs),:srs)  ";
        }
        else {
            sql += " AND  wkb_geometry && ST_GeomFromText(:bboxtext)  ";
        }

        sql += " ORDER BY bhucode, kide";

        Query query = entityManager.createNativeQuery(sql);
        if (CommonUtil.isInteger(sourceSrs) && !StringUtils.isEmpty(destSrs)) {
            query.setParameter("srs", Integer.valueOf(sourceSrs));
            query.setParameter("tosrs", toSrs);
        }

        query.setParameter("bboxtext", bboxText);
        query.setParameter("bhucode", bhucode);
        if (! CollectionUtils.isEmpty(plotIds)) {
           query.setParameter("plotIds", plotIds);
        }

        if (! CollectionUtils.isEmpty(plotNos)) {
            query.setParameter("plotNos", plotNos);
        }

        return DaoUtil.convertTuplesToMap(query.getResultList());
    }

    @Override
    public Map getExtentGisCode(String state, String bhucode) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, bhucode);
        String sqlQuery = "SELECT st_xmin(env) AS xmin , st_ymin(env) AS ymin, st_xmax(env) AS xmax, st_ymax(env) AS ymax FROM " +
                "( SELECT ST_Extent(wkb_geometry) AS env FROM " + tableid + " WHERE    bhucode=:bhucode ) as sub ";

        Query query = entityManager.createNativeQuery(sqlQuery, Tuple.class);
        query.setParameter("bhucode", bhucode);
        return DaoUtil.convertTupleToMap((Tuple) query.getSingleResult());
    }

    @Override
    public Map getExtentVsrNo(String state, String vsrNo) {
        String tableid = TablePartition.getTablePartitionId("Khasramap", state, vsrNo);
        String sqlQuery = "SELECT st_xmin(env) AS xmin , st_ymin(env) AS ymin, st_xmax(env) AS xmax, st_ymax(env) AS ymax FROM " +
                "( SELECT ST_Extent(wkb_geometry) AS env FROM "+tableid+" WHERE  bhucode=(select gis_code from vvvv where vsrno=:vsrno) ) as sub ";

        Query query = entityManager.createNativeQuery(sqlQuery, Tuple.class);
        query.setParameter("vsrno", vsrNo);

        return DaoUtil.convertTupleToMap((Tuple) query.getSingleResult());
    }
}
