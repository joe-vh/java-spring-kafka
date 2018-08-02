package project.services;

import project.models.Cfd;
import project.models.CfdTick;
import project.repositories.CfdRepository;
import project.repositories.CfdTickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CfdTickServiceImpl implements CfdTickService {

	@Autowired
	private CfdRepository cfdRepository;

	@Autowired
	private CfdTickRepository cfdTickRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public List<CfdTick> getCfdTicks(Long cfdId) {
		return cfdTickRepository.findByCfdId(cfdId);
	}

	public List<CfdTick> getAllCfdTicksForToday() {
		return cfdTickRepository.findAllToday();
	}

	public CfdTick getCfdTick(Long id) {
		return cfdTickRepository.findById(id).orElse(null);
	}

	public CfdTick getLatestCfdTick(Long cfdId) {
		return cfdTickRepository.findFirstByCfdIdOrderByTimestampDesc(cfdId).orElse(null);
	}

	public CfdTick addCfdTick(
		Long cfdId,
		BigDecimal price,
		BigDecimal buy,
		BigDecimal sell,
		BigDecimal spread,
		Date timestamp
	) {
		CfdTick tick = new CfdTick();
		Cfd cfd = cfdRepository.findById(cfdId).orElse(null);

		tick.setCfd(cfd);
		tick.setBuy(buy);
		tick.setSell(sell);
		tick.setSpread(spread);
		tick.setPrice(price);
		tick.setTimestamp(timestamp);

		cfdTickRepository.save(tick);

		return tick;
	}

	public void deleteCfdTick(Long id) {
		CfdTick cfdTick = cfdTickRepository.findById(id).orElse(null);
		cfdTickRepository.delete(cfdTick);
	}

}
