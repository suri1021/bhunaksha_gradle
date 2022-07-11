package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.SymbolMasterDAO;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.SymbolMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SymbolServletService {

    @Autowired
    private SymbolMasterDAO symbolMasterDAO;

    public SymbolMaster getSymbolImage(String code) {
       return symbolMasterDAO.findBySymbolCode(code);
    }
}
