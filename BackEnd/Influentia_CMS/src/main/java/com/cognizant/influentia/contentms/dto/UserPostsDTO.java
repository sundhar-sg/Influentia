package com.cognizant.influentia.contentms.dto;

import java.util.*;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostsDTO {
	
	@NotEmpty
	@Id
	private int id;
	
	@NotEmpty
	@CreatedDate
	private Date postedOn;
	
	@NotEmpty
	private boolean isScheduledPost;
	
	@NotEmpty
	@FutureOrPresent
	@Temporal(TemporalType.DATE)
	private Date publishedOnDate;
	
	@NotEmpty
	@FutureOrPresent
	@Temporal(TemporalType.TIME)
	private Date publishedOnTime;
	
	@NotEmpty
	@Pattern(regexp = "^((?i)Image|Video|Text(?-i))$", message = "The Post Type must always be in any one of Image, Video and Text")
	private String postType;
	
	private String postContentText;
	
	private String postAttachmentURL;
	
	@NotEmpty
	@Pattern(regexp = "^((?i)Scheduled|Cancelled(?-i))$", message = "The Post Status must be either Scheduled or Cancelled")
	private String postStatus;
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	@Pattern(regexp = "^((?i)Facebook|Instagram|Youtube|LinkedIn|Twitter(?-i))$", message = "The Social Network type must be any one of Facebook, Instagram, LinkedIn, YouTube and Twitter")
	private String socialNetworkType;
}