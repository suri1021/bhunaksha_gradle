package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.LayerMasterDAO;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.LayerMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LayerMasterService {

    @Autowired
    LayerMasterDAO layerMasterDAO;

    public List<LayerMaster> findAll() {
        return layerMasterDAO.findAll();
    }

}
