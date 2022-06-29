package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.AppSettings;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.AppSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appsettings")
public class AppSettingsController {

    @Autowired
    private AppSettingsService appSettingsService;

    @GetMapping("/code/{codevalue}")
    public AppSettings findByCode(@PathVariable String codevalue)
    {
        return appSettingsService.findByCode(codevalue);
    }
}
