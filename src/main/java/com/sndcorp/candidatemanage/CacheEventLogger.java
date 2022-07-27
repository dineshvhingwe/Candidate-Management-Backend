package com.sndcorp.candidatemanage;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CacheEventLogger implements CacheEventListener<Object, Object> {

	@Override
	public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
		log.debug("Cache Update Notification: Key: {} , Old: {}, New: {}", cacheEvent.getKey(),
				cacheEvent.getOldValue(), cacheEvent.getNewValue() == null ? " " : cacheEvent.getNewValue());
	}
}