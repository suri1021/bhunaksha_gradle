package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.SymbolMaster;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
@AllArgsConstructor
public class SymbolMasterImpl {

    private EntityManager entityManager;

    public SymbolMaster findBySymbolCode(String code) {
        String sqlQuery = "select * from symbol_master where symbol_code=:code";
        Query query = entityManager.createNativeQuery(sqlQuery, SymbolMaster.class);
        query.setParameter("code", code);
        return (SymbolMaster) query.getSingleResult();
    }
}
