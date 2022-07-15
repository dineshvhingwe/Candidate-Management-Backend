package com.sndcorp.candidatemanage.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.sndcorp.candidatemanage.security.models.Role;;

@Data
@AllArgsConstructor
public class JwtResponse {
	private String accessToken;
	private String username;
	private Role role;
}
