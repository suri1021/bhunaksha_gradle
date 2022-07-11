package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.LevelDecoder;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.LevelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/level")
public class LevelController {

    @Autowired
    private LevelReader levelReader;

    @Autowired
    private LevelDecoder levelDecoder;

    @GetMapping("/getlist/{level}/{levellist}")
    public List<CodeValueObj> getLevelValues(@PathVariable int level, @PathVariable String levellist[]) {
        return levelReader.fetchListForLevel(level, levellist);
    }

    @GetMapping("/getlevellables")
    public Map getLevelLables() {
        Map map = new HashMap();
        List list = levelDecoder.getAllLevelLablesEn();
        map.put("lables", list);
        return map;
    }
}