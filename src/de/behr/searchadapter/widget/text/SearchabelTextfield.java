/**
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **/
package de.behr.searchadapter.widget.text;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.behr.searchadapter.search.ISearchSupport;
import de.behr.searchadapter.search.impl.DefaultSearchSupport;
import de.behr.searchadapter.widget.SearchAdapter;

/**
 * @author Johannes Behr
 *
 */
public class SearchabelTextfield extends SearchAdapter {

	private ListViewer searchElementViewer;

	public SearchabelTextfield(Composite parent, int style) {
		super(parent, style, new DefaultSearchSupport());
		searchElementViewer = new ListViewer(getCompositeOfPopupShell(),
				SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		searchElementViewer.getControl().setLayoutData(layoutData);
		getSearchText().addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (getSearchSupport() != null) {
					searchElementViewer.setInput(getSearchSupport().search(
							getSearchText().getText()));
				}
			}
		});

	}

	@Override
	public void setSearchSupport(ISearchSupport searchSupport) {
		super.setSearchSupport(searchSupport);
	}

	public void setContentProvider(IContentProvider contentProvider) {
		searchElementViewer.setContentProvider(contentProvider);
	}

	@Override
	public void addKeyDownListener(Control control) {
	}

	public void setLabelProvider(LabelProvider labelProvider) {
		searchElementViewer.setLabelProvider(labelProvider);
	}

	public void setInput(java.util.List<Object> input) {
		setElementsToSearch(input);
		searchElementViewer.setInput(input);
	}

	@Override
	public Listener addFocusOutListener(final Display display) {
		Listener focusOutListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						if (display.isDisposed()) {
							return;
						}
						Control focusControl = display.getFocusControl();

						if (focusControl == null
								|| focusControl != getSearchText()
								&& focusControl != getTextfield()
								&& focusControl.getParent() != getPopupShell()
								&& focusControl != getCompositeOfPopupShell()
								&& !(focusControl != searchElementViewer
										.getList())) {
							// check popupShell is not disposed
							if (!getPopupShell().isDisposed()) {
								System.err.println(System
										.identityHashCode(focusControl) == System
										.identityHashCode(searchElementViewer
												.getList()));
								getPopupShell().setVisible(false);
							}
						}
					}
				});

			}

		};
		return focusOutListener;
	}
}
