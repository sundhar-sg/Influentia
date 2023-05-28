package com.cognizant.influentia.accountms.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "usersocialaccounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialAccounts {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "socialaccounttype_id")
	private SocialAccountTypes socialAccountTypeId;
	
	@Column(name = "loginid", nullable = false, updatable = true)
	private String loginID;
	
	@Column(name = "encryptedpassword", nullable = false, updatable = true)
	private String encryptedPassword;
	
	@Column(name = "username", nullable = false, updatable = true)
	private String userName;
	
	@Column(name = "subscriptionname", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(Pro|Basic)$", message = "The Subscription Name when inserting a new Social Account can be either Pro (or) Basic")
	private String subscriptionName;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
	@JoinColumn(name = "socialaccount_id")
	private List<SocialAccountTracker> accountActivities;
}