/**
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **/
package de.behr.searchadapter.widget;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.behr.searchadapter.search.ISearchSupport;

/**
 *
 * @author Johannes Behr
 *
 */
public abstract class SearchAdapter extends Composite {

	private Text textfield;
	// private CLabel popupButton;
	private Shell popupShell;
	private Composite compositeOfPopupShell;
	private Composite parent;
	private Listener focusOutListener;
	private Text searchControl;
	private int style;
	private ISearchSupport searchSupport;
	private List<?> elementsToSearch;
	private String searchString;
	private boolean buttonAvailable = true;
	private SearchAdapterUtil searchAdapterUtil;

	/**
	 * @param parent
	 * @param style
	 * @param searchSupport
	 */
	public SearchAdapter(Composite parent, int style,
			ISearchSupport searchSupport) {
		super(parent, style);
		this.parent = parent;
		this.style = style;
		this.searchSupport = searchSupport;
		createComponents();
	}

	public SearchAdapter(Composite parent, int style) {
		this(parent, style, null);
	}

	public SearchAdapter(Composite parent) {
		super(parent, SWT.NONE);
		this.parent = parent;
		this.style = SWT.NONE;
		createComponents();
	}

	/**
	 * Creates all components needed for SearchSupport
	 */
	private void createComponents() {
		if (checkOverridePrivateMethods()) {
			SWTException exception = new SWTException(
					"Do not override this method, otherwise bad things will happen..");
			throw exception;
		}
		textfield = new Text(this, style);
		textfield.setMessage("Search");
		GridLayout layout = createLayout();
		this.setLayout(layout);
		popupShell = new Shell(this.getDisplay(), SWT.ON_TOP);
		popupShell.setLayout(new FillLayout());
		popupShell.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		compositeOfPopupShell = new Composite(popupShell, SWT.NONE);
		compositeOfPopupShell.setLayout(new GridLayout(1, false));
		popupShell.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		popupShell.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				textfield.setEnabled(true);

			}

			@Override
			public void focusGained(FocusEvent e) {
				// noting to do
			}
		});
		searchControl = new Text(compositeOfPopupShell, SWT.BORDER);
		setSearchText(searchControl);
	}

	private GridLayout createLayout() {
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginLeft = 0;
		layout.marginTop = 0;
		return layout;
	}

	/**
	 * Add your own searchField {@link org.eclipse.swt.widgets.Text}
	 *
	 * If searchText == null no searchfield will be available
	 *
	 * @param searchText
	 *
	 */
	public void setSearchText(Text searchText) {
		this.searchControl = searchText;
		focusOutListener = addFocusOutListener(
				compositeOfPopupShell.getDisplay(), searchText);
		if (searchText != null) {
			searchText.addListener(SWT.FocusOut, focusOutListener);
			searchAdapterUtil = new SearchAdapterUtil(textfield, popupShell,
					compositeOfPopupShell, searchText);
		}
		textfield.addListener(SWT.FocusOut, focusOutListener);
		addKeyDownListener(searchText);
		addTextModifyListener();
		addKeyDownListener();
	}

	/**
	 * Add KeydownListener to searchField
	 *
	 * @param event
	 *            event to listen
	 * @param listener
	 *            listenerImpl
	 */
	public void addKeyDownListenerToSearchText(int event, Listener listener) {
		if (searchControl == null) {
			throw new IllegalArgumentException("you didn't set a searchText");
		}
		searchControl.addListener(event, listener);
	}

	private void addKeyDownListener() {
		textfield.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (checkOverridePrivateMethods()) {
					SWTException exception = new SWTException(
							"Do not override this method, otherwise bad things will happen..");
					throw exception;
				}
				if (e.keyCode == SWT.ESC) {
					popupShell.setVisible(false);
				} else {
					popupShell.setVisible(true);
					if (searchControl != null) {
						searchControl.setFocus();
						if (searchControl instanceof Text) {
							searchControl.setText(textfield.getText());
							searchString = textfield.getText();
							// set the cursor to the right position
							searchControl.setSelection(textfield.getSelection());
						}
					}
				}

			}
		});
		searchControl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!searchControl.isDisposed()) {
					searchString = searchControl.getText();
				}
			}
		});
	}

	private void addTextModifyListener() {
		getTextfield().addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				popupVisibleAction();
			}
		});

	}

	protected void popupVisibleAction() {
		if (checkOverridePrivateMethods()) {
			SWTException exception = new SWTException(
					"Do not override this method, otherwise bad things will happen..");
			throw exception;
		}
		searchAdapterUtil.calculateNewBounds();
	};

	private Listener addFocusOutListener(final Display display,
			final Control popupControl) {

		if (checkOverridePrivateMethods()) {
			SWTException exception = new SWTException(
					"Do not override this method, otherwise bad things will happen..");
			throw exception;
		}
		Listener focusOutListener = addFocusOutListener(display);
		addListener(SWT.FocusOut, focusOutListener);
		return focusOutListener;
	}

	public abstract Listener addFocusOutListener(final Display display);

	/**
	 * Overwriting this Method allows to override private methods you do this on
	 * your own risk!
	 *
	 * @return true if its no custom subclass
	 */
	protected boolean checkOverridePrivateMethods() {
		String classname = this.getClass().getName();
		int index = classname.lastIndexOf('.');
		return classname.substring(0, index + 1).equals(
				"de.behr.searchadapter.widgets.combo");
	}

	public abstract void addKeyDownListener(Control control);

	public Text getTextfield() {
		return textfield;
	}

	public Shell getPopupShell() {
		return popupShell;
	}

	public Text getSearchText() {
		return searchControl;
	}

	public Composite getCompositeOfPopupShell() {
		return compositeOfPopupShell;
	}

	@Override
	public Composite getParent() {
		return parent;
	}

	public void setSearchSupport(ISearchSupport searchSupport) {
		this.searchSupport = searchSupport;
	}

	public boolean isButtonAvailable() {
		return buttonAvailable;
	}

	public List<?> getElementsToSearch() {
		return elementsToSearch;
	}

	public void setElementsToSearch(List<?> elementsToSearch) {
		this.elementsToSearch = elementsToSearch;
	}

	public ISearchSupport getSearchSupport() {
		return searchSupport;
	}

}
