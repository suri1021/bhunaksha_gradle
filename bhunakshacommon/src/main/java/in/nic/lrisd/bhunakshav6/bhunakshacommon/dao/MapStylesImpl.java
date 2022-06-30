package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.MapStyles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
@AllArgsConstructor
public class MapStylesImpl {

    private EntityManager entityManager;

    public MapStyles findByCode(String code) {
        String sqlQuery = "Select *from map_styles where code=:code";

        Query query = entityManager.createNativeQuery(sqlQuery, MapStyles.class);
        return (MapStyles) query.getSingleResult();
    }
}
