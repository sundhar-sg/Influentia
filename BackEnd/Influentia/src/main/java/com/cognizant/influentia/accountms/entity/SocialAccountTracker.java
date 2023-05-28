package com.cognizant.influentia.accountms.entity;

import java.util.Date;

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
	@JoinColumn(name = "socialaccount_id")
	private UserSocialAccounts accountID;
	
	@Column(name = "action_date", nullable = false, updatable = true)
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private Date date;
	
	@Column(name = "action", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(AccountAdded|AccountRemoved|AccountPasswordChanged)$", message = "The Action message of any operation done in a social account can only be either Account Added (or) Account Removed (or) Account Password Changed")
	private String action;
}