package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Vvvv;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class VvvvRepository implements VvvvDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Vvvv> findAll() {
        String sqlQuery = "SELECT gis_code, village_en, village_hi, vsrno, mapscale, geotransform, georefpoints,"
                + " st_astext(wkb_geometry) wkb_geometry, scale_units, fmbstatus, default_scale, fmbswversion, ulpin_generated"
                + "	FROM vvvv";

        Query query = entityManager.createNativeQuery(sqlQuery, Vvvv.class);
        return query.getResultList();
    }

    @Override
    public Vvvv findByBhucode(String gis_code) {
        String sqlQuery = "SELECT gis_code, village_en, village_hi, vsrno, mapscale, geotransform, georefpoints,"
                + " st_astext(wkb_geometry) wkb_geometry, scale_units, fmbstatus, default_scale, fmbswversion, ulpin_generated"
                + "	FROM vvvv where gis_code=:gis_code";

        return (Vvvv) entityManager.createNativeQuery(sqlQuery, Vvvv.class).setParameter("gis_code", gis_code)
                .getSingleResult();
    }

    @Override
    public List<Vvvv> findByPattern(String pattern) {
        String sqlQuery = "SELECT gis_code, village_en, village_hi, vsrno, mapscale, geotransform, georefpoints,"
                + " st_astext(wkb_geometry) wkb_geometry, scale_units, fmbstatus, default_scale, fmbswversion, ulpin_generated"
                + "	FROM vvvv where gis_code like :gis_code";

        return entityManager.createNativeQuery(sqlQuery, Vvvv.class).setParameter("gis_code", pattern + "%").getResultList();
    }

    @Override
    public List<Vvvv> findByVsrno(String vsrno) {
        String sqlQuery = "SELECT gis_code, village_en, village_hi, vsrno, mapscale, geotransform, georefpoints,"
                + " st_astext(wkb_geometry) wkb_geometry, scale_units, fmbstatus, default_scale, fmbswversion, ulpin_generated"
                + "	FROM vvvv where vsrno=:vsrno";

        return entityManager.createNativeQuery(sqlQuery, Vvvv.class).setParameter("vsrno", vsrno).getResultList();
    }

    @Override
    public List<Vvvv> findByPointInterSection(String ptText, String srs) {
        String sqlQuery = "";
        Query query = null;
        if (srs != null && (!srs.isEmpty())) {
            sqlQuery = "SELECT gis_code, geotransform FROM vvvv WHERE  geotransform IS NOT NULL AND st_intersects( st_transform(st_setsrid(wkb_geometry,4326),:srs), st_setsrid(st_geomfromtext(:ptText),:srs)) ";
            query = entityManager.createNativeQuery(sqlQuery);
            query.setParameter("srs", srs);
            query.setParameter("ptText", ptText);
        } else {
            sqlQuery = "SELECT gis_code, geotransform FROM vvvv WHERE  geotransform IS NOT NULL AND st_intersects(wkb_geometry, st_geomfromtext(:ptText)) ";
            query = entityManager.createNativeQuery(sqlQuery);
            query.setParameter("ptText", ptText);
        }

        if (query != null)
            return query.getResultList();

        return null;
    }

    @Override
    public List getGisCodesAsList(List gisCodes, List skipGisCodes, List vsrNos, List skipCodes, String cqlFilter, String bboxText, String srs) {
        List<Map<String, String>> gisCodesList = new ArrayList<>();
        String sqlQuery = "SELECT gis_code,geotransform srs FROM vvvv WHERE 1=1 " +
                (CollectionUtils.isEmpty(gisCodes) ? StringUtils.EMPTY : "AND gis_code in (:gisCodes) ") +
                (CollectionUtils.isEmpty(skipGisCodes) ? StringUtils.EMPTY : "AND gis_code NOT IN(:skipGisCodes) ")+
                (CollectionUtils.isEmpty(vsrNos) ? StringUtils.EMPTY : "AND vsrno  IN (:vsrNos) ") +
                (CollectionUtils.isEmpty(skipCodes) ?  StringUtils.EMPTY : "AND vsrno NOT IN (:skipCodes) ") +
                (StringUtils.isEmpty(cqlFilter) ? StringUtils.EMPTY : "And " + cqlFilter) +
                (StringUtils.isEmpty(srs) ?  StringUtils.EMPTY : " AND  COALESCE(geotransform,'') <> '' AND st_transform(st_setsrid(wkb_geometry,4326),:srs) && ST_GeomFromText(:bbox)  ");



        Query query = entityManager.createNativeQuery(sqlQuery);
        if (! CollectionUtils.isEmpty(gisCodes)) {
            query.setParameter("gisCodes" , gisCodes);
        }
        if (! CollectionUtils.isEmpty(skipGisCodes)) {
            query.setParameter("skipGisCodes" , skipGisCodes);
        }
        if (! CollectionUtils.isEmpty(vsrNos)) {
            query.setParameter("vsrNos" , vsrNos);
        }
        if (! CollectionUtils.isEmpty(skipCodes)) {
            query.setParameter("skipCodes" , skipCodes);
        }
        if (! StringUtils.isEmpty(srs)) {
            query.setParameter("srs",  Integer.valueOf(srs.replace("EPSG:", "")));
            query.setParameter("bbox", bboxText);
        }


        return query.getResultList();
    }

    @Override
    public Map getExtentGiscode(String gisCode, int srs) {

        String sqlQuery = "SELECT st_xmin(env) AS xmin , st_ymin(env) AS ymin, st_xmax(env) AS xmax, st_ymax(env) AS ymax FROM " +
                "(SELECT ST_Extent(st_transform(st_setsrid(wkb_geometry,4326), :srs)) AS env FROM vvvv WHERE  gis_code=:gisCode " +
                "AND coalesce(TRIM(geotransform), '') <> '') as sub ";

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("gisCode", gisCode);
        query.setParameter("srs", srs);
        return DaoUtil.convertTupleToMap((Tuple) query.getSingleResult());
    }

    @Override
    public Map getExtentVsrNo(String vsrNo, int srs) {

        String sqlQuery = "SELECT st_xmin(env) AS xmin , st_ymin(env) AS ymin, st_xmax(env) AS xmax, st_ymax(env) AS ymax FROM " +
                "( SELECT ST_Extent(st_transform(st_setsrid(wkb_geometry,4326),:srs)) AS env FROM vvvv WHERE  vsrno=:vsrno AND " +
                "coalesce(TRIM(geotransform), '') <> '') as sub ";

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("srs", srs);
        query.setParameter("vsrno", vsrNo);

        return DaoUtil.convertTupleToMap((Tuple) query.getSingleResult());
    }

    @Override
    public Map getExtentSrs(int srs) {
        String sqlQuery = "SELECT st_xmin(env) AS xmin , st_ymin(env) AS ymin, st_xmax(env) AS xmax, st_ymax(env) " +
                "AS ymax FROM ( SELECT ST_Extent(st_transform(st_setsrid(wkb_geometry,4326),:srs)) AS env FROM vvvv " +
                "WHERE   coalesce(TRIM(geotransform), '') <> '') as sub ";

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("srs", srs);

        return DaoUtil.convertTupleToMap((Tuple) query.getSingleResult());
    }

    public List<Vvvv> findByPlotAtXYGeoref(int srs, String ptText, String pVsrno, String pGiscode, List skipCodes, String cql) {
        String sqlQuery = "SELECT * FROM vvvv WHERE 1=1";
        if (! StringUtils.isEmpty(pGiscode)) sqlQuery += " AND gis_code =:giscode  ";
        if (! StringUtils.isEmpty(pVsrno)) sqlQuery += " AND vsrno =:vsrno  ";
        if (! CollectionUtils.isEmpty(skipCodes)) sqlQuery += " AND   gis_code NOT IN(:skipCodes)";
        if (! StringUtils.isEmpty(cql)) sqlQuery += " AND " + cql;
        if (srs != 0) sqlQuery += " AND st_intersects( st_transform(st_setsrid(wkb_geometry,4326),  :srs),st_setsrid(st_geomfromtext(:ptText),:srs)) ";
        else sqlQuery += " AND st_intersects( wkb_geometry,st_geomfromtext(?)) ";

        Query query = entityManager.createNativeQuery(sqlQuery, Vvvv.class);
        if (! StringUtils.isEmpty(pGiscode)) query.setParameter("giscode", pGiscode);
        if (! StringUtils.isEmpty(pVsrno)) query.setParameter("vsrno", pVsrno);
        if (! CollectionUtils.isEmpty(skipCodes)) query.setParameter("skipCodes", skipCodes);
        if (srs != 0) query.setParameter("srs", srs);
        if (StringUtils.isEmpty(ptText)) query.setParameter("ptText", ptText);

        return  query.getResultList();
    }
}
