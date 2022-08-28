
package com.algderno.util;

/**
 *
 * This class helps to manipulate the CheckBox's state of the TreeView's questions.
 *
 * @author José Lucas dos Santos da Silva
 * 	(And some parts of external code ready).
 *
 */

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;

public class SetCheckBoxTreeView<T> {

	private Set<TreeItem<T>> selected;

	public SetCheckBoxTreeView() {

		selected = new HashSet<>();

	}

	public void listenerStateCheckBox(CheckBoxTreeItem<T> itens) {

		itens.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), (CheckBoxTreeItem.TreeModificationEvent<T> evt) -> {

			CheckBoxTreeItem<T> item = evt.getTreeItem();

			if (evt.wasIndeterminateChanged()) {

				if (item.isIndeterminate()) {
					selected.remove(item);
				} else if (item.isSelected()) {
					selected.add(item);
				}

			} else if (evt.wasSelectionChanged()) {

				if (item.isSelected()) {
					selected.add(item);
				} else {
					selected.remove(item);
				}

			}

		});

	}

	public void listenerValueChange(CheckBoxTreeItem<T> itens) {

		itens.addEventHandler(TreeItem.childrenModificationEvent(), (TreeItem.TreeModificationEvent<T> evt) -> {

			if (evt.wasAdded())
				for (TreeItem<T> added : evt.getAddedChildren())
					addSubtree(selected, (CheckBoxTreeItem<T>) added);

			if (evt.wasRemoved())
				for (TreeItem<T> removed : evt.getRemovedChildren())
					removeSubtree(selected, (CheckBoxTreeItem<T>) removed);

		});

	}

	@SuppressWarnings("hiding")
	private <T> void removeSubtree(Collection<TreeItem<T>> collection, CheckBoxTreeItem<T> item) {

		if (item.isSelected())
			collection.remove(item);
		else if (!item.isIndeterminate() && !item.isIndependent())
			return;

		for (TreeItem<T> child : item.getChildren())
			removeSubtree(collection, (CheckBoxTreeItem<T>) child);

	}

	@SuppressWarnings("hiding")
	private <T> void addSubtree(Collection<TreeItem<T>> collection, CheckBoxTreeItem<T> item) {

		if (item.isSelected())
			collection.add(item);
		else if (!item.isIndeterminate() && !item.isIndependent())
			return;

		for (TreeItem<T> child : item.getChildren())
			addSubtree(collection, (CheckBoxTreeItem<T>) child);

	}
/*
	@SuppressWarnings({ "hiding", "unused" })
	private <T> void print(CheckBoxTreeItem<T> item) {

		if (item.isSelected())
			System.out.println(item.getValue());
		else if (!item.isIndeterminate() && !item.isIndependent())
			return;

		for (TreeItem<T> child : item.getChildren())
			print((CheckBoxTreeItem<T>) child);

	}*/

}

