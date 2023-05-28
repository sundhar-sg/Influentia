package com.cognizant.influentia.contentms.dto;

import java.time.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
public class UserPostsDTO {
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private Date postedOn;
	
	@NotNull
	@JsonProperty
	private boolean isScheduledPost;
	
    @Future(message = "Published date must be a future date")
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private LocalDate publishedOnDate;
	
    @Future(message = "Published time must be a future time")
	@JsonFormat(pattern = "HH:mm:ss", timezone = "IST")
	private LocalTime publishedOnTime;
    
    @Future(message = "Published timestamp must be a future timestamp")
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime publishedOnTimestamp;
	
	@NotEmpty
	@Pattern(regexp = "^(?i)(Image|Video|Text)$", message = "The Post Type must always be in any one of Image, Video and Text")
	private String postType;
	
	private String postContentText;
	
	private String postAttachmentURL;
	
	@NotEmpty
	@Pattern(regexp = "^(?i)(Scheduled|Cancelled)$", message = "The Post Status must be either Scheduled or Cancelled")
	private String postStatus;
	

	@NotEmpty
	private String username;
	
	@NotEmpty
	@Pattern(regexp = "^(?i)(Facebook|LinkedIn|Instagram|YouTube|Twitter)$", message = "The Social Network type must be any one of Facebook, Instagram, LinkedIn, YouTube and Twitter")
	private String socialNetworkType;
	
	public UserPostsDTO(Date postedOn, boolean isScheduledPost, LocalDate publishedOnDate, LocalDateTime publishedOnTimestamp, String postType, String postContentText, String postAttachmentURL, String postStatus, String username, String socialNetworkType) {
		super();
		this.postedOn = postedOn;
		this.isScheduledPost = isScheduledPost;
		this.publishedOnDate = publishedOnDate;
		this.publishedOnTimestamp = publishedOnTimestamp;
		this.postType = postType;
		this.postContentText = postContentText;
		this.postAttachmentURL = postAttachmentURL;
		this.postStatus = postStatus;
		this.username = username;
		this.socialNetworkType = socialNetworkType;
	}
}