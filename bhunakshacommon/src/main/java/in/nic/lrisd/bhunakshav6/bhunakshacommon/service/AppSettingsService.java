package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.AppSettingsRepository;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppSettingsService {

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    public AppSettings findByCode(String code) {
        return appSettingsRepository.findByCode(code);
    }

    public List<AppSettings> findAll() {
        return appSettingsRepository.findAll();
    }
}
