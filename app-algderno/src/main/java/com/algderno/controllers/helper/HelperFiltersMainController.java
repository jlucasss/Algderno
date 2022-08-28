
package com.algderno.controllers.helper;

import java.util.function.Predicate;

import com.algderno.controllers.FiltersMainController;
import com.algderno.models.Group;
import com.algderno.models.Question;

/**
* 
* This class serves to configure the filters of the TableView of Question on the Main screen.
*
* @author José Lucas dos Santos da Silva 
*
*/

import com.algderno.util.logger.SimpleLogger;

import javafx.scene.control.TreeItem;

public class HelperFiltersMainController {

	private int min, max;
	private EnumStatus status;
	@SuppressWarnings("unused")
	private SimpleLogger logger;

	public HelperFiltersMainController(SimpleLogger logger) {
		this.min = FiltersMainController.MIN;
		this.max = FiltersMainController.MAX;
		this.status = EnumStatus.NONE;
		this.logger = logger;
	}

	public Predicate<TreeItem<Group<?>>> createPredicate(String searchText) {
		return group -> {
			if (searchText == null || searchText.isEmpty()) 
				return resultsFilters(group.getValue());
			return searchFindsQuestions(group.getValue(), searchText) && resultsFilters(group.getValue());
		};
	}

	public Predicate<TreeItem<Group<?>>> createPredicate() {
		return group -> {
			return resultsFilters(group.getValue());
		};
	}

	private boolean resultsFilters(Group<?> group) {
		return searchFindsRuntimeMinMaxQuestions( ((Question)group) ) &&
					searchFindsCorrectQuestions( ((Question)group) );
	}

	/* Types of filters */

	private boolean searchFindsQuestions(Group<?> group, String searchText) {
		return group.getName().contains(searchText);
	}

	private boolean searchFindsRuntimeMinMaxQuestions(Question question) {
		return question.getLastRuntime() >= this.min && 
			question.getLastRuntime() <= this.max;
	}

	private boolean searchFindsCorrectQuestions(Question question) {

		switch (this.status) {
			case CORRECT:
				return question.isResultCorrect();
			case INCORRECT:
				return !question.isResultCorrect();
			case NONE:
				return true;
		}

		return true;

	}

	/* Getters and Setters */

	public void setMin(int min) {
		this.min = min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}

	public void setStatus(EnumStatus status) {
		this.status = status;
	}

	public EnumStatus getStatus() {
		return this.status;
	}

	/* Enum Status */

	public static enum EnumStatus {

		NONE("None"),
			CORRECT("Correct"),
			INCORRECT("Incorrect");

		private String content;

		EnumStatus(String content) {
			this.content = content;
		}

		public String toString() {
			return this.content;
		}

	}

}

