package com.siwiak.java;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

@ManagedBean
@SessionScoped
public class Logger implements Serializable {

	private static final long serialVersionUID = 1L;

	private String login;
	private String password;
	private String repassword;
	private String firstName;
	private String lastName;
	private String eMail;
	public UserAccount user;

	@PostConstruct
	public void init() {
		user = new UserAccount();
	}

	public void createUser() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myDatabase");
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		user.setLogin(login);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.seteMail(eMail);

		TypedQuery<UserAccount> query = entityManager.createQuery("SELECT u FROM UserAccount u where u.login='"
				+ user.getLogin() + "' or u.eMail='" + user.geteMail() + "'", UserAccount.class);
		try {
			if (query.getSingleResult().getLogin().equals(user.getLogin())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login already exist!", ""));
			}
			if (query.getSingleResult().geteMail().equals(user.geteMail())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is already busy!", ""));
			}
		} catch (NoResultException e) {
			if (repassword.equals(user.getPassword())) {
				entityManager.getTransaction().begin();
				entityManager.persist(user);
				entityManager.getTransaction().commit();
				entityManager.close();
				entityManagerFactory.close();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Account has been created. Return to the homepage and log in.", ""));
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords are not the same!", ""));
			}
		}
	}

	public String loginUser() {

		if (login == "" | password == "") {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Login and password are required"));
			return null;
		} else {
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myDatabase");
			EntityManager entityManager = entityManagerFactory.createEntityManager();

			TypedQuery<UserAccount> query = entityManager.createQuery(
					"SELECT u FROM UserAccount u where u.login='" + login + "' and u.password='" + password + "'",
					UserAccount.class);
			try {
				user = query.getSingleResult();
			} catch (NoResultException e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Invalid login or password"));
				return "/login.xhtml?faces-redirect=true";
			}
			entityManager.close();
			entityManagerFactory.close();
			return "welcome.xhtml?faces-redirect=true";
		}
	}

	public void remindPassword() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myDatabase");
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		TypedQuery<UserAccount> query = entityManager
				.createQuery("SELECT u FROM UserAccount u where u.eMail='" + eMail + "'", UserAccount.class);

		try {

			Mail mail = new Mail();
			mail.setMessageRecipient(eMail);
			mail.setMessageSubject("Password recovery");
			mail.setMessageContent("Dear " + query.getSingleResult().getFirstName() + "," + "\n\n Your Password is: "
					+ query.getSingleResult().getPassword());
			mail.sendMessage();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Password has been sent to your email.", ""));

		} catch (NoResultException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "The mail entered does not belong to any user!", ""));
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
}
