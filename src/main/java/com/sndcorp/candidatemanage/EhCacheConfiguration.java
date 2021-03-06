package com.sndcorp.candidatemanage;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sndcorp.candidatemanage.security.services.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EhCacheConfiguration {

	@Bean
	public CacheManager ehcacheManager() throws URISyntaxException {
		CachingProvider provider = Caching.getCachingProvider();
		CacheManager cacheManager = provider.getCacheManager();


		CacheEventListenerConfigurationBuilder asynchronousListener = CacheEventListenerConfigurationBuilder
				.newEventListenerConfiguration(new CacheEventLogger(), EventType.CREATED, EventType.UPDATED,
						EventType.REMOVED, EventType.EXPIRED)
				.unordered().asynchronous();

		// create caches as we need
		CacheConfigurationBuilder<SimpleKey, List> configurationBuilder = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(SimpleKey.class, List.class,
						ResourcePoolsBuilder.heap(1000).offheap(5, MemoryUnit.MB))
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(2)));

		cacheManager.createCache("candidatesByTagsCache", Eh107Configuration
				.fromEhcacheCacheConfiguration(configurationBuilder.withService(asynchronousListener)));
		

		
		CacheConfigurationBuilder<SimpleKey, UserDetailsImpl> userDetailsCacheConfigurationBuilder = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(SimpleKey.class, UserDetailsImpl.class,
						ResourcePoolsBuilder.heap(1000).offheap(5, MemoryUnit.MB))
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(2)));

		// create caches as we need
		cacheManager.createCache("userDetailsCache", Eh107Configuration
				.fromEhcacheCacheConfiguration(userDetailsCacheConfigurationBuilder.withService(asynchronousListener)));
		
		log.debug("Created Cache Manager successfully: {}", cacheManager);
		return cacheManager;
	}

}
