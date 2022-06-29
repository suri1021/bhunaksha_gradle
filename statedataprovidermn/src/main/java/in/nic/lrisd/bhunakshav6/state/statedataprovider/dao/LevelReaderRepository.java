package in.nic.lrisd.bhunakshav6.state.statedataprovider.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.LevelReaderDAO;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.VvvvService;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class LevelReaderRepository implements LevelReaderDAO {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private VvvvService vvvvService;

	private ObjectMapper objectMapper =  new ObjectMapper();

	@Override
	public List<CodeValueObj> fetchListForLevel(int level, String... codes) {
		switch(level) {
			case 1: {
				Query query = entityManager.createNativeQuery("select distcode, eng_distdesc from district order by distcode");
				return fetchCodeValueObject(query, null);
			}
			case 2: {
				if (codes == null) return null;
				Query query = entityManager.createNativeQuery("select subcode, eng_subdesc from unisubdiv where distcode = :distcode  order by subcode");
				query.setParameter("distcode", codes[0]);
				return fetchCodeValueObject(query, String.join("", codes));
			}
			case 3: {
				if (codes == null) return null;
				Query query = entityManager.createNativeQuery("select circode, eng_cirdesc from unicircle where distcode = :distcode " +
						"and subcode = :subcode order by circode");
				query.setParameter("distcode", codes[0]);
				query.setParameter("subcode", codes[1]);
				return fetchCodeValueObject(query, String.join("", codes));
			}
			case 4: {
				if (codes == null) return null;
				Query query = entityManager.createNativeQuery("select substr(loccd, 8) vcode, eng_locdesc vname from unilocation " +
						"where loccd like :loccd order by loccd");
				query.setParameter("loccd", codes[0] + codes[1] + codes[2] + "%");
				return fetchCodeValueObject(query, String.join("", codes));
			}
			case 5: {
				if (codes == null) return null;
				String sql = "SELECT substring(gis_code, 11) sheetno FROM vvvv WHERE substring(gis_code, 1,10) =:pcode order by sheetno";
				Query query = entityManager.createNativeQuery(sql);
				query.setParameter("pcode", codes[0] + codes[1] + codes[2] + codes[3]);

				NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
				return (List<CodeValueObj>) nativeQuery.getResultList();
			}
		}
		return null;
	}

	@Override
	public CodeValueObj fetchLevelValue(int level, String... code) {
		return null;
	}

	@Override
	public String fetchVsrInfo(String vsrNo) {
		return null;
	}

	@Override
	public String fetchGisInfo(String giscode) {
		return null;
	}

	private final List<CodeValueObj> fetchCodeValueObject(Query query, String patternString) {
		NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
		nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<CodeValueObj> resultList = (List<CodeValueObj>) nativeQuery.getResultList().stream()
				.map(o -> {
					try {
						CodeValueObj obj =  objectMapper.readValue(objectMapper.writeValueAsString(o),CodeValueObj.class);
						if ((patternString == null && vvvvService.findByPattern(obj.getCode()).size() != 0) ||
								(patternString != null && vvvvService.findByPattern(patternString).size() != 0)) {
							obj.setExtraParm("hasData", true);
						}
						return obj;
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}).collect(Collectors.toList());
		return resultList;		
	}
}