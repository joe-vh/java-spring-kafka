package project.services;

import project.models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
	public List<Account> getAllAccounts();
	public Account getAccount();
	public void addAccount(BigDecimal funds);
	public void updateAccount(BigDecimal funds);
	public void deleteAccount();
}
