package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoUtil {
    public static List<Map<String, Object>> convertTuplesToMap(List<Object> tuples) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i<tuples.size(); i++) {
            Object obj[] = (Object[]) tuples.get(i);
            Map map = new HashMap();
            map.put("the_geom", obj[0]);
            map.put("kide", obj[1]);
            map.put("bhucode", obj[2]);
            result.add(map);
        }

        return result;
    }

    public static Map<String, Object> convertTupleToMap(Tuple tuple) {
        Map<String, Object> tempMap = new HashMap<>();
        for (TupleElement<?> key : tuple.getElements()) {
            tempMap.put(key.getAlias(), tuple.get(key));
        }

        return tempMap;
    }
}
