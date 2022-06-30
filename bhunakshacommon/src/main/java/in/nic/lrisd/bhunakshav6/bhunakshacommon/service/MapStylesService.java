package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.MapStylesDAO;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.MapStyles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapStylesService {

    @Autowired
    MapStylesDAO mapStylesDAO;

    public Optional<MapStyles> findByid(String id) {
        return mapStylesDAO.findById(id);
    }

    public List<MapStyles> findAll() {
        return mapStylesDAO.findAll();
    }

    public MapStyles findByCode(String code) {
        return mapStylesDAO.findByCode(code);
    }

}
