package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.SymbolMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolMasterDAO extends JpaRepository<SymbolMaster, String> {

    public SymbolMaster findBySymbolCode(String code);
}
