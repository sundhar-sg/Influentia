package com.cognizant.influentia.contentms.dto;

import java.util.*;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateRangeDTO {

	@NotEmpty
	private Date fromDate;
	
	@NotEmpty
	private Date toDate;
}