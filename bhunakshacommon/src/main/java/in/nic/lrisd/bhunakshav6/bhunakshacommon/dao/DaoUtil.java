package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoUtil {
    public static List<Map<String, Object>> convertTuplesToMap(List<Tuple> tuples) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Tuple single : tuples) {
            Map<String, Object> tempMap = new HashMap<>();
            for (TupleElement<?> key : single.getElements()) {
                tempMap.put(key.getAlias(), single.get(key));
            }
            result.add(tempMap);
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
