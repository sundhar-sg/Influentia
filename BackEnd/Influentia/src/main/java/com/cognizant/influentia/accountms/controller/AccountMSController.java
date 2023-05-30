package com.cognizant.influentia.accountms.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.influentia.accountms.dto.AccountTypeDTO;
import com.cognizant.influentia.accountms.dto.UserSocialAccountsDTO;
import com.cognizant.influentia.accountms.entity.UserSocialAccounts;
import com.cognizant.influentia.accountms.service.AccountMSService;
import com.cognizant.influentia.exception.ResourceQuotaExceededException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/accounts")
@Validated
@Slf4j
public class AccountMSController {

	@Autowired
	AccountMSService accountMSService;
	
	@GetMapping("/types")
	public ResponseEntity<List<AccountTypeDTO>> getAllSocialAccountTypes() {
		log.info("Fetched all the types of allowed social accounts for working with the application");
		return new ResponseEntity<List<AccountTypeDTO>>(accountMSService.getAllAccountTypes(), HttpStatus.FOUND);
	}
	
	@PostMapping("/addsocialaccount")
	public ResponseEntity<UserSocialAccounts> addNewSocialAccount(@RequestBody @Valid UserSocialAccountsDTO userAccountDTO) throws ResourceQuotaExceededException {
		return new ResponseEntity<UserSocialAccounts>(accountMSService.addNewSocialAccount(userAccountDTO), HttpStatus.OK);
	}
	
	@GetMapping("/{username}/socialaccounts")
	public ResponseEntity<List<UserSocialAccountsDTO>> getSocialAccountsBasedOnUsername(@PathVariable("username") String username) {
		try {
			return new ResponseEntity<List<UserSocialAccountsDTO>>(accountMSService.socialAccountsRegisteredForUser(username), HttpStatus.FOUND);
		} catch(NoSuchElementException ex) {
			log.error("There are no social accounts registered under the provided username : {}", username);
			throw new NoSuchElementException("There are no social accounts registered under the provided username: " +username);
		}
	}
	
	@DeleteMapping("/{username}/{accountID}/remove")
	public ResponseEntity<String> removeSocialAccount(@PathVariable("username") String username, @PathVariable("accountID") int accountID) {
		if(accountMSService.removeSocialAccountByUser(accountID, username)) {
			return new ResponseEntity<String>("Successfully removed a social account for the provided username", HttpStatus.OK);
		}
		log.error("Failed to remove the mentioned social account for the username");
		throw new NoSuchElementException("Failed to remove the mentioned social account for the username as the requested resource is not found");
	}
}