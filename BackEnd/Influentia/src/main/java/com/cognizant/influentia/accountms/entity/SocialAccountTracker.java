package com.cognizant.influentia.accountms.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "socialaccounttracker")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccountTracker {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "useraccount_id", nullable = true)
	private UserSocialAccounts accountID;
	
	@Column(name = "action_date", nullable = false, updatable = true)
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private Date date;
	
	@Column(name = "action", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(AccountAdded|AccountPasswordChanged|AccountRemoved)$", message = "The allowed actions to be taken on the social accounts can be either AccountAdded, AccountPasswordChanged (or) AccountRemoved")
	private String action;
}