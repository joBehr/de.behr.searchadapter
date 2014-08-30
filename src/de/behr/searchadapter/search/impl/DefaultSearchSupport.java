/**
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **/
package de.behr.searchadapter.search.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.behr.searchadapter.search.ISearchSupport;

public class DefaultSearchSupport implements ISearchSupport {

	private List<Object> elementsToSearch;

	@Override
	public List<?> search(String searchString) {
		if (elementsToSearch == null || elementsToSearch.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		if (searchString == null || searchString.isEmpty()) {
			return elementsToSearch;
		}
		List<String> returnedList = new ArrayList<>();
		for (Object search : elementsToSearch) {
			if (((String) search).toLowerCase().contains(searchString)) {
				returnedList.add(search.toString());
			}
		}
		return returnedList;
	}

	@Override
	public void setElementsToSearch(List<?> elementsToSearch) {
		this.elementsToSearch = Collections.unmodifiableList(elementsToSearch);
	}
}
