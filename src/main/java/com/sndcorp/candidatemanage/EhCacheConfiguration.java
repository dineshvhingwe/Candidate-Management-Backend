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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EhCacheConfiguration {

	@Bean
	public CacheManager ehcacheManager() throws URISyntaxException {
		CachingProvider provider = Caching.getCachingProvider();
		CacheManager cacheManager = provider.getCacheManager();

		CacheConfigurationBuilder<SimpleKey, List> configurationBuilder = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(SimpleKey.class, List.class,
						ResourcePoolsBuilder.heap(1000).offheap(5, MemoryUnit.MB))
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(2)));

		CacheEventListenerConfigurationBuilder asynchronousListener = CacheEventListenerConfigurationBuilder
				.newEventListenerConfiguration(new CacheEventLogger(), EventType.CREATED, EventType.UPDATED,
						EventType.REMOVED, EventType.EXPIRED)
				.unordered().asynchronous();

		// create caches we need
		cacheManager.createCache("candidatesByTagsCache", Eh107Configuration
				.fromEhcacheCacheConfiguration(configurationBuilder.withService(asynchronousListener)));
		log.debug("Created Cache Manager successfully: {}", cacheManager);
		return cacheManager;
	}

}
