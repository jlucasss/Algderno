package com.algderno.controllers.helper.service;

import com.algderno.models.Group;

public interface ListenerGroup<T extends Group<?>> {

	public void changed(T group);
	
}
