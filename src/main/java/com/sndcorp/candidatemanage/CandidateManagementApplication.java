package com.sndcorp.candidatemanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CandidateManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CandidateManagementApplication.class, args);
	}

	/*
	@Bean
	CommandLineRunner runner(CandiateService userService) {
		return args -> {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				Candidate candidate = objectMapper.readValue(new File("./src/main/resources/data.json"),
						Candidate.class);

				userService.addCandidate(candidate);
				System.out.println("Candidate Saved!");
			} catch (IOException e) {
				System.out.println("Unable to save Candidate " + e.getMessage());
			}
		};
	} */

}
