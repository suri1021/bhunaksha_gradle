package in.nic.lrisd.bhunakshav6.bhunakshacommon.globalsettings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TablePartition {

    private static final Map<String, String> partitions;
    private static final Map<String, String> mapPartitions;

    static {
        Map<String, String> aMap = new HashMap<String, String>();
        Map<String, String> pMap = new HashMap<String, String>();

        aMap.put("00", "0,0"); //Demo        
        aMap.put("02", "1,4"); //Himachal Pradesh
        aMap.put("05", "1,8"); //UK
        aMap.put("06", "1,5"); //Haryana
        aMap.put("08", "1,5"); //Rajasthan
        aMap.put("09", "1,8"); //UP        
        aMap.put("10", "1,6"); //Bihar - WRONG
        aMap.put("11", "1,2"); //Sikkim
        aMap.put("14", "1,5"); //Manipur
        aMap.put("15", "1,4"); //Mizoram
        aMap.put("16", "1,3"); //Tripura
        aMap.put("18", "1,5"); //Assam
        aMap.put("19", "1,2"); //West Bengal - MAY BE WRONG
        aMap.put("20", "1,4"); //Jharkhand
        aMap.put("21", "1,4"); //Orissa
        aMap.put("22", "1,4"); //Chhattisgarh
        aMap.put("23", "1,4"); //Madhya Pradesh
        aMap.put("24", "1,4"); //GUjarat
        aMap.put("27", "1,5"); //Maharashtra
        aMap.put("28", "1,5");  //Andhra Pradesh
        aMap.put("29", "1,2"); //Karnataka - MAY BE WRONG
        aMap.put("30", "1,18"); //Goa
        aMap.put("31", "1,5"); //Lakshadweep
        aMap.put("32", "1,2"); //Kerala
        aMap.put("35", "1,4");  //Andaman
        aMap.put("36", "1,7"); //Telengana 
        aMap.put("33", "1,5"); //Tamil Nadu      
        aMap.put("34", "1,5"); //Puducherry
        aMap.put("35", "1,4");  //Andaman
        aMap.put("36", "1,7"); //Telengana 
        aMap.put("26", "1,4"); //THE DADRA AND NAGAR HAVELI AND DAMAN AND DIU

        pMap.put("00", "Demo"); //Demo
        pMap.put("01", "JK"); //Jammu & Kashmir
        pMap.put("02", "HP"); //Himachal Pradesh
        pMap.put("03", "PB"); //Punjab
        pMap.put("04", "CH"); //Chandigarh
        pMap.put("05", "UK"); //Uttarakhad
        pMap.put("06", "HR"); //Haryana
        pMap.put("07", "DL"); //NCT of Delhi
        pMap.put("08", "RJ"); //Rajasthan
        pMap.put("09", "UP"); //UP
        pMap.put("10", "BH"); //Bihar - WRONG
        pMap.put("11", "SK"); //Sikkim
        pMap.put("12", "AR"); //Arunachal Pradesh
        pMap.put("13", "NL"); //Nagaland
        pMap.put("14", "MN"); //Manipur
        pMap.put("15", "MZ"); //Mizoram
        pMap.put("16", "TP"); //Tripura
        pMap.put("17", "ML"); //Meghalaya
        pMap.put("18", "AS"); //Assam
        pMap.put("19", "WB"); //West Bengal - MAY BE WRONG
        pMap.put("20", "JH"); //Jharkhand
        pMap.put("21", "OR"); //Orissa
        pMap.put("22", "CG"); //Chhattisgarh
        pMap.put("23", "MP"); //Madhya Pradesh
        pMap.put("24", "GJ"); //Gujarat
        pMap.put("25", "DD"); //Daman and Diu
        pMap.put("26", "DN"); //Dadra and Nagar Haveli
        pMap.put("27", "MH"); //Maharashtra
        pMap.put("28", "AP");  //Andhra Pradesh
        pMap.put("29", "KA"); //Karnataka - MAY BE WRONG
        pMap.put("30", "GA"); //Goa
        pMap.put("31", "LD"); //Lakshadweep
        pMap.put("32", "KL"); //Kerala
        pMap.put("33", "TN"); //Tamil Nadu
        pMap.put("34", "PC"); //Puducherry
        pMap.put("35", "AN");  //Andaman
        pMap.put("36", "TL"); //Telengana 
        //   pMap.put("38", "DN"); //THE DADRA AND NAGAR HAVELI AND DAMAN AND DIU

        mapPartitions = Collections.unmodifiableMap(pMap);

        partitions = Collections.unmodifiableMap(aMap);
    }

    public static String getTablePatitionRange(String state) {
        return partitions.get(state);
    }

    public static int[] getSubstrRange4Java(String state) {
        try {
            if (partitions.containsKey(state)) {
                String[] sPartitions = partitions.get(state).split(",");
                int p1 = Integer.parseInt(sPartitions[0]);
                int p2 = Integer.parseInt(sPartitions[1]);
                return new int[]{p1 - 1, p2};
            }
        } catch (Exception x) {
            Logger.getLogger(TablePartition.class.getName()).log(Level.SEVERE, null, x);
        }
        return null;
    }

    public static int[] getSubstrRange4Postgres(String state) {
        try {
            if (partitions.containsKey(state)) {
                String[] sPartitions = partitions.get(state).split(",");
                int p1 = Integer.parseInt(sPartitions[0]);
                int p2 = Integer.parseInt(sPartitions[1]);
                return new int[]{p1, p2};
            }
        } catch (Exception x) {
            Logger.getLogger(TablePartition.class.getName()).log(Level.SEVERE, null, x);
        }
        return null;
    }

    public static String getTablePartitionRange(String state) {
        return mapPartitions.get(state);
    }

    public static int[] getSubstringRange4Java(String state) {
        try {
            if (mapPartitions.containsKey(state)) {
                String[] sPartitions = mapPartitions.get(state).split(",");
                int p1 = Integer.parseInt(sPartitions[0]);
                int p2 = Integer.parseInt(sPartitions[1]);
                return new int[]{p1 - 1, p2};
            }
        } catch (Exception x) {
            Logger.getLogger(TablePartition.class.getName()).log(Level.SEVERE, null, x);
        }
        return null;
    }

    public static int[] getSubstringRange4Postgres(String state) {
        try {
            if (mapPartitions.containsKey(state)) {
                String[] sPartitions = mapPartitions.get(state).split(",");
                int p1 = Integer.parseInt(sPartitions[0]);
                int p2 = Integer.parseInt(sPartitions[1]);
                return new int[]{p1, p2};
            }
        } catch (Exception x) {
            Logger.getLogger(TablePartition.class.getName()).log(Level.SEVERE, null, x);
        }
        return null;
    }

    public static String getTablePartitionId(String tablename, String state, String bhucode) {
        int[] subStrIdx = TablePartition.getSubstrRange4Java(state);
        return tablename + "_" + bhucode.substring(subStrIdx[0], subStrIdx[1]);
    }
}
