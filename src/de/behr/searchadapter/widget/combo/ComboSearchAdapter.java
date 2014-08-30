/**
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **/
package de.behr.searchadapter.widget.combo;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.behr.searchadapter.widget.SearchAdapter;

/**
 * @author Johannes Behr
 *
 */
public class ComboSearchAdapter extends SearchAdapter {

	private CLabel popupButton;

	public ComboSearchAdapter(Composite parent, int style) {
		super(parent, style);
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("icons/button.png");
		Image image = new Image(getDisplay(), input);
		popupButton = new CLabel(this, SWT.NONE);
		popupButton.setImage(image);
		popupButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				popupVisibleAction();
				getSearchText().setFocus();
			}
		});
	}

	@Override
	public void addKeyDownListener(Control control) {
		// TODO Auto-generated method stub

	}

	public CLabel getPopupButton() {
		return popupButton;
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
								|| focusControl != getTextfield()
								&& focusControl.getParent() != getPopupShell()
								|| !(focusControl instanceof org.eclipse.swt.widgets.List)) {
							// check popupShell is not disposed
							if (!getPopupShell().isDisposed()) {
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
