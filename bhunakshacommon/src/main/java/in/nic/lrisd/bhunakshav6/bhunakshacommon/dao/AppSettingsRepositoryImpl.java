package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.AppSettings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
@AllArgsConstructor
public class AppSettingsRepositoryImpl {

    private EntityManager entityManager;

    public AppSettings findByCode(String code) {
        String sqlString = "Select id, code, value, comments, last_updated, isvisible" +
                " from app_settings where code=:code";

        Query query = entityManager.createNativeQuery(sqlString, AppSettings.class);
        query.setParameter("code", code);
        return (AppSettings) query.getSingleResult();
    }
}
