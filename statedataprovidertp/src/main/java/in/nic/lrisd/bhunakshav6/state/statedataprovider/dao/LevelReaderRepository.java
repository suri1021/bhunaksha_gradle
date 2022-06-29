package in.nic.lrisd.bhunakshav6.state.statedataprovider.dao;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.LevelReaderDAO;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.VvvvService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;


@Repository
public class LevelReaderRepository implements LevelReaderDAO {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private VvvvService vvvvService;

	private ObjectMapper objectMapper =  new ObjectMapper();

	@Override
	public List<CodeValueObj> fetchListForLevel(int level, String... codes) {
		switch(level)
		{
		case 1: {
			Query query = entityManager.createNativeQuery("select distinct lgd_district_code as code"
					+ ", district_name_eng as value from tpmaster");
			
			return fetchCodeValueObject(query, null);
		}
		case 2: {
			if (codes == null) return null;
			Query query = entityManager.createNativeQuery("select distinct lgd_subdiv_code as code, subdivision_name_eng as value"
					+ " from tpmaster where lgd_district_code=:lgd_district_code");
			query.setParameter("lgd_district_code", codes[0]);
			return fetchCodeValueObject(query, String.join("", codes));
		}
		case 3: {
			if (codes == null) return null;
			Query query = entityManager.createNativeQuery("select distinct lpad(lgd_circle_code,3,'0') as code, circle_name_eng as value "
					+ "from tpmaster where lgd_district_code=:lgd_district_code and lgd_subdiv_code = :lgd_subdiv_code");
			query.setParameter("lgd_district_code", codes[0]);
			query.setParameter("lgd_subdiv_code", codes[1]);
			return fetchCodeValueObject(query, String.join("", codes));
		}
		case 4: {
			if (codes == null) return null;
			Query query = entityManager.createNativeQuery("select distinct lpad(lgd_tehsil_code,5, '0') as code,"
					+ " tehsil_name_eng as value from tpmaster "
                    + "where lgd_district_code=:lgd_district_code and lgd_subdiv_code = :lgd_subdiv_code and lpad(lgd_circle_code,3,'0') = :lgd_circle_code");
			query.setParameter("lgd_district_code", codes[0]);
			query.setParameter("lgd_subdiv_code", codes[1]);
			query.setParameter("lgd_circle_code", codes[2]);
			return fetchCodeValueObject(query, String.join("", codes));
		}
		case 5: {
			if (codes == null) return null;
			Query query = entityManager.createNativeQuery("select lgd_village_code as code, village_name_eng as value from tpmaster "
                    + "where lgd_district_code=:lgd_district_code and lgd_subdiv_code = :lgd_subdiv_code and lpad(lgd_circle_code,3,'0') = :lgd_circle_code and lpad(lgd_tehsil_code,5, '0') = :lgd_tehsil_code");
			query.setParameter("lgd_district_code", codes[0]);
			query.setParameter("lgd_subdiv_code", codes[1]);
			query.setParameter("lgd_circle_code", codes[2]);
			query.setParameter("lgd_tehsil_code", codes[3]);
			return fetchCodeValueObject(query, String.join("", codes));
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