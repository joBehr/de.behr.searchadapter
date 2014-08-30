/**
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **/
package de.behr.searchadapter.search;

import java.util.List;

public interface ISearchSupport {

	List<?> search(String searchString);

	void setElementsToSearch(List<?> elementsToSearch);
}
