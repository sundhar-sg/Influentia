package com.cognizant.influentia.contentms.entity;

import java.time.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userposts")
@Data
public class UserPosts {
	
	// Define Fields
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "postedon", nullable = false, updatable = true)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private Date postedOn;
	
	@Column(name = "isscheduledpost", nullable = false, updatable = true)
	@JsonProperty
	private boolean isScheduledPost;
	
	@Column(name = "publishedondate", nullable = false, updatable = true)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private LocalDate publishedOnDate;
	
	@Column(name = "publishedontime", nullable = false, updatable = true)
	@JsonFormat(pattern = "HH:mm:ss", timezone = "IST")
	private LocalTime publishedOnTime;
	
	@Column(name = "posttype", nullable = false, updatable = true)
	private String postType;
	
	@Column(name = "postcontenttext")
	private String postContentText;
	
	@Column(name = "postattachmenturl")
	private String postAttachmentURL;
	
	@Column(name = "poststatus", nullable = false, updatable = true)
	private String postStatus;
	
	@Column(name = "username", nullable = false, updatable = true)
	private String username;
	
	@Column(name = "socialnetworktype", nullable = false, updatable = true)
	private String socialNetworkType;

	// Define Constructor
	public UserPosts() {
		
	}
	
	public UserPosts(Date postedOn, boolean isScheduledPost, LocalDate publishedOnDate, LocalTime publishedOnTime, String postType,
			String postContentText, String postAttachmentURL, String postStatus, String username,
			String socialNetworkType) {
		super();
		this.postedOn = postedOn;
		this.isScheduledPost = isScheduledPost;
		this.publishedOnDate = publishedOnDate;
		this.publishedOnTime = publishedOnTime;
		this.postType = postType;
		this.postContentText = postContentText;
		this.postAttachmentURL = postAttachmentURL;
		this.postStatus = postStatus;
		this.username = username;
		this.socialNetworkType = socialNetworkType;
	}
	
	// Generate toString() method
	@Override
	public String toString() {
		return "UserPosts [id=" + id + ", postedOn=" + postedOn + ", isScheduledPost=" + isScheduledPost
				+ ", publishedOnDate=" + publishedOnDate + ", publishedOnTime=" + publishedOnTime + ", postType="
				+ postType + ", postContextText=" + postContentText + ", postAttachmentURL=" + postAttachmentURL
				+ ", postStatus=" + postStatus + ", username=" + username + ", socialNetworkType=" + socialNetworkType
				+ "]";
	}
	
}