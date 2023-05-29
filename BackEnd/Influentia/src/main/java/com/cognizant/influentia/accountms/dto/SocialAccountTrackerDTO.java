package com.cognizant.influentia.accountms.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccountTrackerDTO {

	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private Date action_date;
	
	@NotNull
	@Pattern(regexp = "^(?i)(AccountAdded|AccountRemoved|AccountPasswordChanged)$", message = "The Action message of any operation done in a social account can only be either Account Added (or) Account Removed (or) Account Password Changed")
	private String action;
}