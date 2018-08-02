package project.services;

import project.models.FutureTick;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface FutureTickService {
	public List<FutureTick> getFutureTicks(Long futureId);
	public List<FutureTick> getAllFutureTicksForToday();
	public FutureTick getFutureTick(Long id);
	public FutureTick getLatestFutureTick(Long futureId);
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
	);
	public void deleteFutureTick(Long id);
}
