package com.sndcorp.candidatemanage.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.repo.CandidateRepository;
import com.sndcorp.candidatemanage.security.jwt.JwtUtils;
import com.sndcorp.candidatemanage.security.models.JwtResponse;
import com.sndcorp.candidatemanage.security.models.LoginRequest;
import com.sndcorp.candidatemanage.security.models.MessageResponse;
import com.sndcorp.candidatemanage.security.models.Role;
import com.sndcorp.candidatemanage.security.models.SignupRequest;
import com.sndcorp.candidatemanage.security.models.User;
import com.sndcorp.candidatemanage.security.repository.UserRepository;
import com.sndcorp.candidatemanage.security.services.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = "http://localhost:4200") For angular
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CandidateRepository candidateRepo;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		log.info("Inside login(). Authentication: {}", authentication.getDetails());
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		//Set authentication in context to access is later
		//SecurityContextHolder.getContext().setAuthentication(authentication);

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), Role.valueOf(roles.get(0))));
	}

	@PostMapping("/register")
	@Transactional(value = TxType.REQUIRES_NEW)
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		log.info("got request {}", signUpRequest);
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		// Create new user's account
		User user = new User();
		user.setUsername(signUpRequest.getUsername());
		user.setPassword(encoder.encode(signUpRequest.getPassword()));
		user.setRole(Role.ROLE_USER);

		if (candidateRepo.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		if (candidateRepo.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		userRepository.save(user);
		Candidate candidate = new Candidate();
		candidate.setName(signUpRequest.getName());
		candidate.setSurname(signUpRequest.getSurname());
		candidate.setPhone(signUpRequest.getPhone());
		candidate.setEmail(signUpRequest.getEmail());
		candidate.setGender(signUpRequest.getGender());
		candidate.setUsername(signUpRequest.getUsername());
		candidateRepo.save(candidate);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
