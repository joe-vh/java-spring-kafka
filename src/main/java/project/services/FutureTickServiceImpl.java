package project.services;

import project.models.Future;
import project.models.FutureTick;
import project.repositories.FutureTickRepository;
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
public class FutureTickServiceImpl implements FutureTickService {

	@Autowired
	private FutureTickRepository futureTickRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public List<FutureTick> getFutureTicks(Long futureId) {
		return futureTickRepository.findByFutureId(futureId);
	}

	public List<FutureTick> getAllFutureTicksForToday() {
		return futureTickRepository.findAllToday();
	}

	public FutureTick getFutureTick(Long id) {
		return futureTickRepository.findById(id).orElse(null);
	}

	public FutureTick getLatestFutureTick(Long futureId) {
		return futureTickRepository.findFirstByFutureIdOrderByTimestampDesc(futureId).orElse(null);
	}

	public FutureTick addFutureTick(
		String type,
		Long futureId,
		Date maturity,
		BigDecimal t,
		Date timestamp,
		BigDecimal futurePrice,
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
		Future future = em.getReference(Future.class, futureId);
		FutureTick tick = new FutureTick();

		tick.setType(type);
		tick.setFuture(future);
		tick.setMaturity(maturity);
		tick.setStrike(strike);
		tick.setVol(vol);
		tick.setFuturePrice(futurePrice);
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
		futureTickRepository.save(tick);

		return tick;
	}

	public void deleteFutureTick(Long id) {
		FutureTick futureTick = futureTickRepository.findById(id).orElse(null);
		futureTickRepository.delete(futureTick);
	}

}
