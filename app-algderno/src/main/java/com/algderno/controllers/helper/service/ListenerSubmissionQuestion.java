package com.algderno.controllers.helper.service;

/**
*
* This interface action when Question are updated in Submission.
*
* @author José Lucas dos Santos da Silva
*
*/

import com.algderno.models.Question;

public interface ListenerSubmissionQuestion {

	public void changed(Question q);
	
}
