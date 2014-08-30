/**
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **/
package de.behr.searchadapter.widget;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SearchAdapterUtil {
	// theorie is if you know why, but nothing works - practice is if everything
	// works fine but you dunno why - this variables are practice
	public int offsetY = 7;
	public int offsetX = 7;
	public int marginOfPopupShell = 10;

	public int popupHight = 150;
	private Control controlToCalculateDirection;
	private Shell popupShell;
	private Composite compositeOfControl;
	private Control control;

	/**
	 * @param controlToCalculateDirection
	 *            Control dessen Position bestimmt ob sich das Popup nach oben
	 *            oder unten öffnet
	 * @param popupShell
	 * @param compositeOfControl
	 * @param control
	 */
	public SearchAdapterUtil(Control controlToCalculateDirection,
			Shell popupShell, Composite compositeOfControl, Control control) {
		this.controlToCalculateDirection = controlToCalculateDirection;
		this.popupShell = popupShell;
		this.compositeOfControl = compositeOfControl;
		this.control = control;
	}

	/**
	 * Errechnet die Position und die Ausrichtung des Popups
	 */
	public void calculateNewBounds() {
		popupShell.setVisible(true);
		Rectangle controlBounds = compositeOfControl.getDisplay().map(
				controlToCalculateDirection, null, control.getBounds());
		int heightOfDisplay = controlToCalculateDirection.getDisplay()
				.getBounds().height;

		Rectangle rectangleOfShell = controlToCalculateDirection.getDisplay()
				.map(controlToCalculateDirection, null,
						controlToCalculateDirection.getBounds());
		int yPosOfShell = rectangleOfShell.y + popupHight;
		if (heightOfDisplay > yPosOfShell) {
			popupShell.setBounds(controlBounds.x - offsetX, controlBounds.y
					- offsetY + controlBounds.height, controlBounds.width
					+ marginOfPopupShell, popupHight);
		} else {
			Rectangle newBounds = new Rectangle(controlBounds.x - offsetX,
					controlBounds.y - offsetY
							- controlToCalculateDirection.getBounds().y
							- popupHight + 3, controlBounds.width
							+ marginOfPopupShell
					// small
					// gap,
					, popupHight);
			popupShell.setBounds(newBounds);
		}
	}
}