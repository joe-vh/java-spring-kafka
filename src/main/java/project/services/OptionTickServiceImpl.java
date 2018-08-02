package project.services;

import project.models.Option;
import project.models.OptionTick;
import project.repositories.OptionTickRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OptionTickServiceImpl implements OptionTickService {
	private static final Logger LOG = LoggerFactory.getLogger(OptionTickServiceImpl.class);

	@Autowired
	private OptionTickRepository optionTickRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public List<OptionTick> getOptionTicks(Long optionId) {
		return optionTickRepository.findByOptionId(optionId);
	}

	public List<OptionTick> getAllOptionTicksForToday() {
		return optionTickRepository.findAllToday();
	}

	public OptionTick getOptionTick(Long id) {
		return optionTickRepository.findById(id).orElse(null);
	}

	public OptionTick getLatestOptionTick(Long optionId) {
		return optionTickRepository.findFirstByOptionIdOrderByTimestampDesc(optionId).orElse(null);
	}

	public OptionTick addOptionTick(
		String type,
		Long optionId,
		Date maturity,
		BigDecimal t,
		Date timestamp,
		BigDecimal spot,
		BigDecimal strike,
		BigDecimal r,
		BigDecimal vol,
		BigDecimal price,
		BigDecimal delta,
		BigDecimal gamma,
		BigDecimal vega,
		BigDecimal theta,
		BigDecimal rho
	) {
		EntityManager em = entityManagerFactory.createEntityManager();
		OptionTick tick = new OptionTick();
		Option option = em.getReference(Option.class, optionId);

		LOG.info("adding option tick");

		tick.setType(type);
		tick.setOption(option);
		tick.setMaturity(maturity);
		tick.setStrike(strike);
		tick.setVol(vol);
		tick.setSpot(spot);
		tick.setPrice(price);
		tick.setStrike(strike);
		tick.setT(t);
		tick.setR(r);
		tick.setGamma(gamma);
		tick.setDelta(delta);
		tick.setVega(vega);
		tick.setRho(rho);
		tick.setTheta(theta);
		tick.setTimestamp(timestamp);

		em.persist(tick);
		optionTickRepository.save(tick);

		return tick;
	}

	public void deleteOptionTick(Long id) {
		OptionTick optionTick = optionTickRepository.findById(id).orElse(null);
		optionTickRepository.delete(optionTick);
	}

}
