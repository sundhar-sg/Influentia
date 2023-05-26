package com.cognizant.influentia.contentms.dto;

import java.util.*;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDTO {

	@NotEmpty
	private Date fromDate;
	
	@NotEmpty
	private Date toDate;
	
	@NotEmpty
	private List<String> socialAccounts;
	
	@NotEmpty
	private Map<String, Integer> postsCount;
}