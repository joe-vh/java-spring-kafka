package project.services;

import project.models.OptionTick;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OptionTickService {
	public List<OptionTick> getOptionTicks(Long optionId);
	public OptionTick getOptionTick(Long id);
	public OptionTick getLatestOptionTick(Long optionId);
	public List<OptionTick> getAllOptionTicksForToday();
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
    );
	public void deleteOptionTick(Long id);
}
