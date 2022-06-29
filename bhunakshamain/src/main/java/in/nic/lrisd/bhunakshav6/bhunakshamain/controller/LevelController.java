package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.LevelReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/level")
public class LevelController {

    @Autowired
    private LevelReaderService levelReaderService;

    @GetMapping("/getlist/{level}/{levellist}")
    public List<CodeValueObj> getLevelValues(@PathVariable int level, @PathVariable String[] levellist) {
        return levelReaderService.fetchListForLevel(level, levellist);
    }
}