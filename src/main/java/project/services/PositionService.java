package project.services;

import project.enums.EInstrument;
import project.models.Position;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PositionService {
	public List<Position> getPositions();
	public List<Position> getAllPositionsByBasket(Long basketId);
	public Position getPosition(Long id);
	public void createPosition(Long userId, Long tickId, Long intrumentId, Long basketId, Integer quantity, BigDecimal price, Boolean side, String type, String kind, Date maturity) throws JSONException;
	public void closePosition(Long userId, Long basketId, Integer quantity, Boolean side, EInstrument eInstrument, Long futureId, Long optionId, Long cfdId, String kind, Date maturity) throws JSONException;
//	public void updatePosition(Long id, Integer quantity);
	public void deletePosition(Long id) throws JSONException;
}
