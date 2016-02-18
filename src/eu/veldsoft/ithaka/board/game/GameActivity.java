package eu.veldsoft.ithaka.board.game;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class GameActivity extends Activity {

	private Board board = new Board();

	private SoundPool sounds = null;

	private int clickId = -1;

	private int finishId = -1;

	private ImageView pieces[][] = new ImageView[Board.COLS][Board.ROWS];

	private View.OnClickListener onPiceClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (board.isGameOver() == true) {
				return;
			}

			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[i].length; j++) {
					if (pieces[i][j] == view) {
						board.click(i, j);
					}
				}
			}

			sounds.play(clickId, 0.99f, 0.99f, 0, 0, 1);
			updateViews();
		}
	};

	private void updateViews() {
		Piece values[][] = board.getPieces();
		boolean selected[][] = board.getSelection();

		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				if (selected[i][j] == true) {
					pieces[i][j].setAlpha(0.5F);
				} else {
					pieces[i][j].setAlpha(1.0F);
				}

				switch (values[i][j]) {
				case BLUE:
					pieces[i][j].setImageResource(R.drawable.blue);
					break;
				case GREEN:
					pieces[i][j].setImageResource(R.drawable.green);
					break;
				case ORANGE:
					pieces[i][j].setImageResource(R.drawable.orange);
					break;
				case FUCHSIA:
					pieces[i][j].setImageResource(R.drawable.fuchsia);
					break;
				case EMPTY:
					pieces[i][j].setImageResource(R.drawable.white);
					break;
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		clickId = sounds.load(this, R.raw.schademans_pipe9, 1);
		finishId = sounds.load(this, R.raw.game_sound_correct, 1);

		pieces[0][0] = (ImageView) findViewById(R.id.piece00);
		pieces[0][1] = (ImageView) findViewById(R.id.piece01);
		pieces[0][2] = (ImageView) findViewById(R.id.piece02);
		pieces[0][3] = (ImageView) findViewById(R.id.piece03);
		pieces[1][0] = (ImageView) findViewById(R.id.piece10);
		pieces[1][1] = (ImageView) findViewById(R.id.piece11);
		pieces[1][2] = (ImageView) findViewById(R.id.piece12);
		pieces[1][3] = (ImageView) findViewById(R.id.piece13);
		pieces[2][0] = (ImageView) findViewById(R.id.piece20);
		pieces[2][1] = (ImageView) findViewById(R.id.piece21);
		pieces[2][2] = (ImageView) findViewById(R.id.piece22);
		pieces[2][3] = (ImageView) findViewById(R.id.piece23);
		pieces[3][0] = (ImageView) findViewById(R.id.piece30);
		pieces[3][1] = (ImageView) findViewById(R.id.piece31);
		pieces[3][2] = (ImageView) findViewById(R.id.piece32);
		pieces[3][3] = (ImageView) findViewById(R.id.piece33);

		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				pieces[i][j].setOnClickListener(onPiceClick);
			}
		}

		board.reset();
		updateViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_option_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_game:
			break;
		case R.id.help:
			startActivity(new Intent(GameActivity.this, HelpActivity.class));
			break;
		case R.id.about:
			startActivity(new Intent(GameActivity.this, AboutActivity.class));
			break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		sounds.release();
		sounds = null;
	}
}
