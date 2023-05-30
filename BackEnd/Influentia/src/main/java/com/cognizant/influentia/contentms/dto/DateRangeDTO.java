package com.cognizant.influentia.contentms.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateRangeDTO {

	@NotEmpty
	@NotNull
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private LocalDate fromDate;
	
	@NotEmpty
	@NotNull
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private LocalDate toDate;
}