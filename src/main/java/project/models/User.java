package project.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table(	name = "users",
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@OneToOne(cascade=ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="account_id", nullable=true)
	private Account account;

	@OneToMany(cascade=ALL, mappedBy="user")
	@JsonManagedReference
	private List<Basket> baskets = new ArrayList<>();

	@ManyToMany(cascade=ALL, fetch = FetchType.LAZY)
	@JoinTable(	name = "user_strategies",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "strategy_id"))
	private List<Strategy> strategies = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String username, String email, String password) {
		Account account = new Account();
		account.setFunds(new BigDecimal(5000));
		account.setUser(this);

		Basket basket = new Basket();
		basket.setName("default");
		basket.setUser(this);

		this.username = username;
		this.email = email;
		this.password = password;
		this.account = account;
		this.baskets.add(basket);
	}
}
