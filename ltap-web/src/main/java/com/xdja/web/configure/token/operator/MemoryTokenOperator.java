package com.xdja.web.configure.token.operator;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xdja.web.configure.token.TokenConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MemoryTokenOperator extends AbstractTokenOperator{

	private static LoadingCache<String, String> CONTAINER;
	
	public MemoryTokenOperator(final TokenConfig config) {
		super(config);

		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
		if(config.isAutoDelay()){
			builder.expireAfterAccess(config.getExpiredTimeInMinutes(), TimeUnit.MINUTES);
		} else {
			builder.expireAfterWrite(config.getExpiredTimeInMinutes(), TimeUnit.MINUTES);
		}
		CONTAINER = builder.build(new CacheLoader<String, String>(){

			@Override
			public String load(String key) throws Exception {
				return null;
			}});
	}
	
	@Override
	public String add(String value) {
		String key = super.config.getKeyGenerator().get();
		CONTAINER.put(key, value);
		return key;
	}

	@Override
	public String get(String token) {
		return CONTAINER.getIfPresent(token);
	}
	
	@Override
	public List<String> values() {
		return new ArrayList<String>(CONTAINER.asMap().values());
	}
	
	@Override
	public Map<String, String> all() {
		return CONTAINER.asMap();
	}

	@Override
	public boolean delay(String token) {
		try {
			CONTAINER.get(token);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean invalidate(String token) {
		CONTAINER.invalidate(token);
		return true;
	}

}
