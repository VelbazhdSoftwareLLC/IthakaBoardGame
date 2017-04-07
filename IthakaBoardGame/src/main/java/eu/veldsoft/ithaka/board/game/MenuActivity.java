package eu.veldsoft.ithaka.board.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import eu.veldsoft.ithaka.board.game.model.Mode;

/**
 * Lobby menu screen.
 *
 * @author Todor Balabanov
 */
public class MenuActivity extends Activity {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		findViewById(R.id.single_game).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", Mode.SINGLE_PLAYER);
						  startActivity(intent);
					  }
				  }
		);

		findViewById(R.id.double_game).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", Mode.TWO_PLAYERS);
						  startActivity(intent);
					  }
				  }
		);

		//TODO https://github.com/JimSeker/bluetooth/tree/master/blueToothDemo/app/src/main/java/edu/cs4730/btDemo
		findViewById(R.id.join_game).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", Mode.CLIENT_MULTIPLAYER);
						  startActivity(intent);
					  }
				  }
		);

		findViewById(R.id.create_game).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", Mode.SERVER_MULTIPLAYER);
						  startActivity(intent);
					  }
				  }
		);

		findViewById(R.id.exit_game).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  MenuActivity.this.finish();
					  }
				  }
		);
	}
}
