package com.sndcorp.candidatemanage;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
//@EnableScheduling
@Slf4j
public class SchedulingConfiguration {

	@Scheduled(cron = "50 * * * * ?")  // Every 50th sec
	@CacheEvict(value = "candidatesByTagsCache", allEntries = true)
	public void deleteCache() {
	  // this method can be empty
		log.warn(":: EVECTING :: candidatesByTagsCache ");
	}
}
