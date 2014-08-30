/**
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **/
package de.behr.searchadapter.converter.impl;

import de.behr.searchadapter.converter.IConverter;

public class DefaultConverter implements IConverter {

	@Override
	public Object convertFrom(Object from) {
		if (from == null) {
			return "";
		}
		return from.toString();
	}

	@Override
	public Object convertTo(Object to) {
		if (to == null) {
			return "";
		}
		return to.toString();
	}

}
