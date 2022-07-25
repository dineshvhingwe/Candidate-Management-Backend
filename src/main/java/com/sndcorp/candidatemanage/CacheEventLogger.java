package com.sndcorp.candidatemanage;

import java.util.List;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CacheEventLogger implements CacheEventListener<SimpleKey, List> {

	@Override
	public void onEvent(CacheEvent<? extends SimpleKey, ? extends List> cacheEvent) {
		log.debug("Cache Update Notification: Key: {} , Old: {}", cacheEvent.getKey(),
				cacheEvent.getOldValue());
		if (null != cacheEvent.getNewValue() && !cacheEvent.getNewValue().isEmpty()) {
			cacheEvent.getNewValue().forEach((candidate)-> log.debug("new value: {}", candidate));
		}
	}

}