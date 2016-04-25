package eu.veldsoft.ithaka.board.game;

import android.app.Activity;
import android.os.Bundle;

/**
 * Game rules and how to play information screen.
 * 
 * @author Todor Balabanov
 */
public class HelpActivity extends Activity {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}
}
