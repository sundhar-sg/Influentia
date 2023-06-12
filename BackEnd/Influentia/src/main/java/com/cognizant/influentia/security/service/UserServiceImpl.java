package com.cognizant.influentia.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.influentia.security.dto.RoleDTO;
import com.cognizant.influentia.security.dto.SignupDTO;
import com.cognizant.influentia.security.dto.UserDTO;
import com.cognizant.influentia.security.entity.Role;
import com.cognizant.influentia.security.entity.User;
import com.cognizant.influentia.security.repository.UserRepository;
import com.cognizant.influentia.subscriptionms.dto.NewUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.service.SubscriptionMSService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubscriptionMSService subscriptionMSService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid Username or password");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return this.userRepository.findByUsername(username);
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public UserDTO addNewUser(SignupDTO signupDTO, List<RoleDTO> roleDTO) {
		// TODO Auto-generated method stub
		User newUser = new User();
		UserDTO userDTO = new UserDTO(signupDTO.getUsername(), signupDTO.getPassword());
		newUser.setUsername(signupDTO.getUsername());
		newUser.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
		newUser.setFirstName(signupDTO.getFirstname());
		newUser.setLastName(signupDTO.getLastname());
		newUser.setEnabled(true);
		List<Role> assignedRoles = new ArrayList<>();
		for(RoleDTO role : roleDTO) {
//			role.setName("ROLE_" +role.getName().toUpperCase());
//			this.roleRepository.save(this.modelMapper.map(role, Role.class));
			assignedRoles.add(this.modelMapper.map(role, Role.class));
		}
		newUser.setRoles(assignedRoles);
		NewUserSubscriptionDTO subscriptionDTO = new NewUserSubscriptionDTO(signupDTO.getPlanID(), signupDTO.getUsername(), signupDTO.getValidityDuration(), signupDTO.getPaymentMode());
		this.subscriptionMSService.purchaseSubscription(subscriptionDTO);
		this.userRepository.save(newUser);
		return userDTO;
	}
}