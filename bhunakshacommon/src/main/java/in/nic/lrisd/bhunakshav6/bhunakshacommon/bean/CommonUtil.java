package in.nic.lrisd.bhunakshav6.bhunakshacommon.bean;

import com.jamesmurty.utils.XMLBuilder;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.globalsettings.TablePartition;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sunish
 */
public class CommonUtil {

    public static  SSLContext sSSLContext = null;

    public static Client getWebClient(String uname, String password) throws NoSuchAlgorithmException, KeyManagementException
    {
        if (sSSLContext == null)
        {
            TrustManager[] noopTrustManager = new TrustManager[]{
                    new X509TrustManager() {

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            sSSLContext = SSLContext.getInstance("ssl");
            sSSLContext.init(null, noopTrustManager, null);
        }

        return ClientBuilder.newBuilder().register(HttpAuthenticationFeature.basic(uname, password))
                .sslContext(sSSLContext).build();
    }

    public static String arrayToString(String[] array, String delimiter, String wrapIn) {
        StringBuilder result = new StringBuilder();
        for (String string : array) {
            result.append(wrapIn).append(string).append(wrapIn);
            result.append(delimiter);
        }
        return result.substring(0, result.length() - delimiter.length());

    }

    public static String arrayListToString(ArrayList<String> array, String delimiter, String wrapIn) {
        StringBuilder result = new StringBuilder();
        for (String string : array) {
            result.append(wrapIn).append(string).append(wrapIn);
            result.append(delimiter);
        }
        return result.substring(0, result.length() - delimiter.length());

    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.length() > 20) {
            ip = ip.substring(ip.length() - 20);
        }

        return ip;
    }

    public static boolean isValidStateCode(String code) {
        //01 - 38
        return code.matches("(0[1-9]|[1-2][0-9]|3[0-8])");
    }

    public static boolean isValidId(String code) {
        if (code.contains(",")) {
            String[] tempIds = code.split(",");
            boolean result = true;
            for (String tempId : tempIds) {
                if (tempId.length() == 22) {
                    if (!(tempId.matches("^[a-zA-Z0-9_-]*$"))) {
                        result = false;
                        break;
                    }
                } else if (tempId.length() == 23) {
                    if (!(tempId.matches("^[a-zA-Z0-9.-]*$"))) {
                        result = false;
                        break;
                    }
                } else {
                    result = false;
                    break;
                }
            }
            return result;
        } else {
            if (code.length() == 22) {
                return code.matches("^[a-zA-Z0-9_-]*$");
            } else if (code.length() == 23) {
                return code.matches("^[a-zA-Z0-9.-]*$");
            } else {
                return false;
            }
        }
    }

    public static boolean isValidlevels(String code) {
        if (code != null) {
            return code.matches("^[a-zA-Z0-9._,-/]*$");
        }
        return false;
    }

    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static boolean isInteger(String s) {
        if (s == null) {
            return false;
        }
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isValidCharacters(String input) { // returns false if invalid characters(&<>/'" are present in input)
        String patternString = "[^<>/&\\'\"]*";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(input);

        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Map sort on the basis of value
     *
     * @param <K>
     * @param <V>
     * @param map
     * @return
     */
    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public static Coordinate pointOnVectorAtDistance(Coordinate startCord, Coordinate endCord, double distance) {

        double x3 = endCord.x - startCord.x;
        double y3 = endCord.y - startCord.y;
        double d3 = Math.sqrt(x3 * x3 + y3 * y3);

        x3 /= d3;
        y3 /= d3;

        x3 *= distance;
        y3 *= distance;

        return new Coordinate(startCord.x + x3, startCord.y + y3);

    }

    public static void safeCloseStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception ex) {
                Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void safeCloseResultset(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
                Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void safeCloseFileInputStream(FileInputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public static boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9-]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    public static Boolean checkFilePath(String filepath) {
        // String regularExpression = "(?:[\\w]\\:|\\\\)(\\\\[a-z_\\-\\s0-9\\.]+)+\\.(?i)(pdf|xls|xlsx)$";
        // String regularExpression="^/$|^(/[a-zA-Z0-9_-\\.]+)+$";

        //String regularExpression = "(?:[\\w]\\:|\\\\)(\\\\[a-z_\\-\\s0-9\\.]+)+\\.(pdf|xls|xlsx)";
        boolean isMatched = false;
        //String path = "D:\\directoryname\\testing\\abc.txt";
        String regularExpression = "^(?:[\\w]\\:|\\\\)(\\\\[a-zA-Z_\\-\\s0-9\\.]+)+\\.(pdf|xml|properties|zip)$";
        Pattern pattern = Pattern.compile(regularExpression);
        if (filepath != null) {
            isMatched = pattern.matcher(filepath).matches();
            //isMatched = Pattern.matches(regularExpression, filepath);
            // System.out.println(filepath);
            //System.out.println(pattern.pattern());
            // System.out.println(isMatched);

        }
        return isMatched;
    }
    public static String createAttributeXML(SimpleFeature feature) {
        String xml = "";
        String extraAttributes = "";
        try {

            XMLBuilder builder = XMLBuilder.create("attributes");

            for (int i = 0; i < feature.getFeatureType().getAttributeCount(); i++) {
                String attribName = (String) feature.getFeatureType().getDescriptor(i).getName().getLocalPart().toString();

//                if (("the_geom".equals(attribName))) {
                if (Geometry.class.isAssignableFrom(feature.getFeatureType().getType(i).getBinding())) {
                    continue;
                }

                String attribValue = "";
                if (null != feature.getAttribute(attribName.toString())) {
                    attribValue = feature.getAttribute(attribName.toString()).toString();
                }
                builder.e("attribute").a("key", attribName.toString()).a("value", attribValue).up();
                extraAttributes += attribName.toString() + ":String,";
            }
            extraAttributes = extraAttributes.substring(0, extraAttributes.length() - 1);
            builder.up();
            xml = builder.asString(null);
        } catch (FactoryConfigurationError ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xml;
    }

    public static String getPTable(String state, String bhucode)
    {
        int[] subStrIdx = TablePartition.getSubstrRange4Java(state);
        return "_" + bhucode.substring(subStrIdx[0], subStrIdx[1]);
    }
}
