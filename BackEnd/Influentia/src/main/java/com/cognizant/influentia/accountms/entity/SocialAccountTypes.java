package com.cognizant.influentia.accountms.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "socialaccounttypes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccountTypes {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "accountType", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(Facebook|Instagram|LinkedIn|Twitter|YouTube)$", message = "The account type for specifying the social account type can only be either Facebook (or) Instagram (or) LinkedIn (or) Twitter (or) YouTube")
	private String accountType;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false, mappedBy = "socialAccountTypeId")
	private List<UserSocialAccounts> listOfAccounts;
}