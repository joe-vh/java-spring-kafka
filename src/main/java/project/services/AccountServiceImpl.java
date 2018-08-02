package project.services;

import project.models.Account;
import project.security.UserDetailsFactory;
import project.models.User;
import project.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}

	public Account getAccount() {
		Long id = UserDetailsFactory.getId();
		return accountRepository.findByUserId(id).orElse(null);
	}

	public void addAccount(BigDecimal funds) {
		Long userId = UserDetailsFactory.getId();
		EntityManager em = entityManagerFactory.createEntityManager();
		User user = em.getReference(User.class, userId);

		Account newAccount = new Account();
		newAccount.setUser(user);
		newAccount.setFunds(funds);

		em.persist(newAccount);
		accountRepository.save(newAccount);
	}

	public void updateAccount(BigDecimal funds) {
		Long id = UserDetailsFactory.getId();
		Account account = accountRepository.findByUserId(id).orElse(null);
		account.setFunds(funds);
		accountRepository.save(account);
	}

	public void deleteAccount() {
		Long id = UserDetailsFactory.getId();
		Account account = accountRepository.findByUserId(id).orElse(null);
		accountRepository.delete(account);
	}

}
