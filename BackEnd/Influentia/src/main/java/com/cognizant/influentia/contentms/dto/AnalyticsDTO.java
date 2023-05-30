package com.cognizant.influentia.contentms.dto;


import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDTO {

	@NotEmpty
	@NotNull
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private LocalDate fromDate;
	
	@NotEmpty
	@NotNull
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private LocalDate toDate;
	
	@NotEmpty
	@NotNull
	private List<String> socialAccounts;
	
	@NotEmpty
	@NotNull
	private Map<String, Integer> postsCount;
}