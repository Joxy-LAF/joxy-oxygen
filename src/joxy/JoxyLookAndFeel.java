package joxy;

import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.text.DefaultEditorKit;

import joxy.utils.Output;
import joxy.utils.Utils;



/**
 * This class is the main class of the Joxy Look and Feel.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyLookAndFeel extends BasicLookAndFeel {

	//-- CONSTANTS ------------------------------------------------------------
	public static final int ANIMATION_NONE = 0;
	public static final int ANIMATION_HOVER = 1;
	public static final int ANIMATION_FOCUS = 2;
	public static final int ANIMATION_ENABLED = 3;
	
	//-- VARIABLES ------------------------------------------------------------
	/** Define the version of Joxy */
	private final String version = "0.0.1 pre-alpha";
	/** Whether Joxy already has been installed in the UIManager. */
	private static boolean isInstalled = false;
	
	//-- PUBLIC METHODS -------------------------------------------------------
	@Override
	public String getDescription() {
		return "The Joxy Look and Feel (v" + version + "): integration with KDE Oxygen";
	}

	@Override
	public String getID() {
		return "Joxy";
	}

	@Override
	public String getName() {
		return "Joxy";
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return true;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}
	
	@Override
	public UIDefaults getDefaults() {
		// This method calls initClassDefaults(), initSystemColorDefaults()
		// and initComponentDefaults() in that order.
		UIDefaults defs = super.getDefaults();
		
		// for testing purposes
		Output.debug(defs);
		
		return defs;
	}
	
	/**
	 * {@inheritDoc}
	 * TODO: We should support this, but when we do, we will probably use
	 *       native methods and hence, "do not support" it after all.
	 *       I think we should support it and use native methods. [tca 17-nov-2011]
	 */
	@Override
	public boolean getSupportsWindowDecorations() {
		return false;
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		if (!isInstalled) {
			isInstalled = true;

			UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo(
				"Joxy", "joxy.JoxyLookAndFeel"));
		}
	}
	
	//-- PROTECTED METHODS ----------------------------------------------------
	/**
	 * {@inheritDoc}
	 * First helper method to be called by getDefaults().
	 */
	@Override
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);
		
		// Set the classes to use (from Joxy) instead of the default ones
		table.putDefaults(new Object[] {
				"ButtonUI", "joxy.JoxyButtonUI",
				"CheckBoxUI", "joxy.JoxyCheckBoxUI",
				"FileChooserUI", "joxy.JoxyFileChooserUI",
				"LabelUI", "joxy.JoxyLabelUI",
				"MenuUI", "joxy.JoxyMenuUI",
				"MenuBarUI", "joxy.JoxyMenuBarUI",
				"MenuItemUI", "joxy.JoxyMenuItemUI",
				"OptionPaneUI", "joxy.JoxyOptionPaneUI",
				"PanelUI", "joxy.JoxyPanelUI",
				"PopupMenuUI", "joxy.JoxyPopupMenuUI",
				"PopupMenuSeparatorUI", "joxy.JoxySeparatorUI",
				"ProgressBarUI", "joxy.JoxyProgressBarUI",
				"RadioButtonUI", "joxy.JoxyRadioButtonUI",
				"RootPaneUI", "joxy.JoxyRootPaneUI",
				"ScrollPaneUI", "joxy.JoxyScrollPaneUI",
				"SeparatorUI", "joxy.JoxySeparatorUI",
				"SliderUI", "joxy.JoxySliderUI",
				"TabbedPaneUI", "joxy.JoxyTabbedPaneUI",
				"TextAreaUI", "joxy.JoxyTextAreaUI",
				"TextFieldUI", "joxy.JoxyTextFieldUI",
				"ToggleButtonUI", "joxy.JoxyToggleButtonUI",
				"ToolBarUI", "joxy.JoxyToolBarUI",
				"ViewportUI", "joxy.JoxyViewportUI"
		});
		
		//Output.debug(UIManager.getUI(new JFileChooser()));
	}
	
	/**
	 * {@inheritDoc}
	 * Second helper method to be called by getDefaults().
	 */
	@Override
	protected void initSystemColorDefaults(UIDefaults table) {
		super.initSystemColorDefaults(table);
		// Set the colors to use (either KDE color theme or default Joxy theme)
		// in stead of the default colors
		table.putDefaults(Utils.getKDEColorMap());
	}
	
	/**
	 * {@inheritDoc}
	 * Third and last helper method to be called by getDefaults().
	 */
	@Override
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);
		
		// [ws] FIXME! Dit is noodzakelijk, omdat de superaanroep hierboven de helft
		// weer overschrijft...
		table.putDefaults(Utils.getKDEColorMap());
		// Here, we can initialize all sorts of default values for many widgets.
		// For example, borders, icons and other properties can be set.
		table.putDefaults(Utils.getKDEPropertiesMap());
		
		setupActions(table);
	}
	
	/**
	 * Copied from NapkinLookAndFeel.java. TODO customize
	 * @param table
	 */
    private static void setupActions(UIDefaults table) {
        //!! Should get actions from the native L&F for all map defaults
        Object fieldInputMap = new UIDefaults.LazyInputMap(new Object[]{
                "ctrl C", DefaultEditorKit.copyAction,
                "ctrl V", DefaultEditorKit.pasteAction,
                "ctrl X", DefaultEditorKit.cutAction,
                "COPY", DefaultEditorKit.copyAction,
                "PASTE", DefaultEditorKit.pasteAction,
                "CUT", DefaultEditorKit.cutAction,
                "shift LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift RIGHT", DefaultEditorKit.selectionForwardAction,
                "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
                "ctrl LEFT", DefaultEditorKit.previousWordAction,
                "ctrl KP_LEFT", DefaultEditorKit.previousWordAction,
                "ctrl RIGHT", DefaultEditorKit.nextWordAction,
                "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,
                "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
                "ctrl shift KP_LEFT",
                DefaultEditorKit.selectionPreviousWordAction,
                "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,
                "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
                "ctrl A", DefaultEditorKit.selectAllAction,
                "HOME", DefaultEditorKit.beginLineAction,
                "END", DefaultEditorKit.endLineAction,
                "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                "shift END", DefaultEditorKit.selectionEndLineAction,
                "typed \010", DefaultEditorKit.deletePrevCharAction,
                "DELETE", DefaultEditorKit.deleteNextCharAction,
                "RIGHT", DefaultEditorKit.forwardAction,
                "LEFT", DefaultEditorKit.backwardAction,
                "KP_RIGHT", DefaultEditorKit.forwardAction,
                "KP_LEFT", DefaultEditorKit.backwardAction,
                "ENTER", JTextField.notifyAction,
                "ctrl BACK_SLASH", "unselect"
                /*DefaultEditorKit.unselectAction*/,
                "control shift O", "toggle-componentOrientation"
                /*DefaultEditorKit.toggleComponentOrientation*/
        });
        
        Object multilineInputMap = new UIDefaults.LazyInputMap(new Object[]{
                "ctrl C", DefaultEditorKit.copyAction,
                "ctrl V", DefaultEditorKit.pasteAction,
                "ctrl X", DefaultEditorKit.cutAction,
                "COPY", DefaultEditorKit.copyAction,
                "PASTE", DefaultEditorKit.pasteAction,
                "CUT", DefaultEditorKit.cutAction,
                "shift LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift RIGHT", DefaultEditorKit.selectionForwardAction,
                "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
                "ctrl LEFT", DefaultEditorKit.previousWordAction,
                "ctrl KP_LEFT", DefaultEditorKit.previousWordAction,
                "ctrl RIGHT", DefaultEditorKit.nextWordAction,
                "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,
                "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
                "ctrl shift KP_LEFT",
                DefaultEditorKit.selectionPreviousWordAction,
                "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,
                "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
                "ctrl A", DefaultEditorKit.selectAllAction,
                "HOME", DefaultEditorKit.beginLineAction,
                "END", DefaultEditorKit.endLineAction,
                "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                "shift END", DefaultEditorKit.selectionEndLineAction,

                "UP", DefaultEditorKit.upAction,
                "KP_UP", DefaultEditorKit.upAction,
                "DOWN", DefaultEditorKit.downAction,
                "KP_DOWN", DefaultEditorKit.downAction,
                "PAGE_UP", DefaultEditorKit.pageUpAction,
                "PAGE_DOWN", DefaultEditorKit.pageDownAction,
                "shift PAGE_UP", "selection-page-up",
                "shift PAGE_DOWN", "selection-page-down",
                "ctrl shift PAGE_UP", "selection-page-left",
                "ctrl shift PAGE_DOWN", "selection-page-right",
                "shift UP", DefaultEditorKit.selectionUpAction,
                "shift KP_UP", DefaultEditorKit.selectionUpAction,
                "shift DOWN", DefaultEditorKit.selectionDownAction,
                "shift KP_DOWN", DefaultEditorKit.selectionDownAction,
                "ENTER", DefaultEditorKit.insertBreakAction,
                "typed \010", DefaultEditorKit.deletePrevCharAction,
                "DELETE", DefaultEditorKit.deleteNextCharAction,
                "RIGHT", DefaultEditorKit.forwardAction,
                "LEFT", DefaultEditorKit.backwardAction,
                "KP_RIGHT", DefaultEditorKit.forwardAction,
                "KP_LEFT", DefaultEditorKit.backwardAction,
                "TAB", DefaultEditorKit.insertTabAction,
                "ctrl BACK_SLASH", "unselect"
                /*DefaultEditorKit.unselectAction*/,
                "ctrl HOME", DefaultEditorKit.beginAction,
                "ctrl END", DefaultEditorKit.endAction,
                "ctrl shift HOME", DefaultEditorKit.selectionBeginAction,
                "ctrl shift END", DefaultEditorKit.selectionEndAction,
                "ctrl T", "next-link-action",
                "ctrl shift T", "previous-link-action",
                "ctrl SPACE", "activate-link-action",
                "control shift O", "toggle-componentOrientation"
                /*DefaultEditorKit.toggleComponentOrientation*/
        });

        Object[] actionDefaults = {
                // these are just copied from Metal L&F -- no values in Basic L&F
                //!! Should get input maps from the native L&F for all map defaults
                "TextField.focusInputMap", fieldInputMap,
                "PasswordField.focusInputMap", fieldInputMap,
                "TextArea.focusInputMap", multilineInputMap,
                "TextPane.focusInputMap", multilineInputMap,
                "EditorPane.focusInputMap", multilineInputMap,
        };

        table.putDefaults(actionDefaults);
    }
}
