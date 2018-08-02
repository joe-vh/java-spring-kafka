package project.services;

import project.models.CfdTick;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CfdTickService {
	public List<CfdTick> getCfdTicks(Long cfdId);
	public List<CfdTick> getAllCfdTicksForToday();
	public CfdTick getCfdTick(Long id);
	public CfdTick getLatestCfdTick(Long cfdId);
	public CfdTick addCfdTick(
            Long cfdId,
            BigDecimal price,
            BigDecimal buy,
            BigDecimal sell,
            BigDecimal spread,
			Date timestamp
    );
	public void deleteCfdTick(Long id);
}
