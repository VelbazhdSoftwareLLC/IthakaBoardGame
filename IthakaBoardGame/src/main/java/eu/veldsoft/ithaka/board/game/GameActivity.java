package eu.veldsoft.ithaka.board.game;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.Move;
import eu.veldsoft.ithaka.board.game.model.Piece;
import eu.veldsoft.ithaka.board.game.model.PlayingMode;
import eu.veldsoft.ithaka.board.game.model.Util;

/**
 * Main application screen.
 * 
 * @author Todor Balabanov
 */
public class GameActivity extends Activity {

	/**
	 * UI holds the object of the game engine.
	 */
	private Board board = new Board();

	/**
	 * Type of game, which is played.
	 */
	PlayingMode mode = PlayingMode.SINGLE_PLAYER;

	/**
	 * Pool with sounds to be played.
	 */
	private SoundPool sounds = null;

	/**
	 * Identifier of the click sound.
	 */
	private int clickId = -1;

	/**
	 * Identifier of the click sound.
	 */
	private int wrongId = -1;

	/**
	 * Identifier of wrong click sound.
	 */
	private int finishId = -1;
	
	/**
	 * Helping two-dimensional array with references to the image views.
	 */
	private ImageView pieces[][] = new ImageView[Board.COLS][Board.ROWS];

	/**
	 * Helping reference for who is on turn.
	 */
	private TextView playerOnTurn = null;

	/**
	 * Helper for the AI thread.
	 */
	private final Handler handler = new Handler();

	/**
	 * AI playing thread.
	 */
	private Runnable ai = new Runnable() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			/*
			 * If the game is over there is no move to play.
			 */
			if (board.isGameOver() == true) {
				return;
			}

			/*
			 * If some other player have to play there is no move to generate.
			 */
			if (board.getTurn() % 2 != 1) {
				return;
			}

			/*
			 * Do not search for valid move if there is no any.
			 */
			if (board.hasValidMove() == false) {
				return;
			}

			/*
			 * Selection of random move.
			 */
			Move move = null;
			do {
				move = new Move(Util.PRNG.nextInt(Board.COLS),
						Util.PRNG.nextInt(Board.ROWS),
						Util.PRNG.nextInt(Board.COLS),
						Util.PRNG.nextInt(Board.ROWS));
			} while (board.isValid(move) == false);

			/*
			 * Do computer move.
			 */
			boolean result = board.click(move.getStartX(), move.getStartY());
			updateViews();

			/*
			 * The opponents should see the selected piece.
			 */
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}

			result = board.click(move.getEndX(), move.getEndY());
			updateViews();

			/*
			 * End of the game conditions check.
			 */
			if (board.checkForGameOver() == true) {
				board.setGameOver(true);

				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
			} else {
				board.next();
			}
			updateViews();
		}
	};

	/**
	 * On piece click listener object.
	 */
	private View.OnClickListener onPiceClick = new View.OnClickListener() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onClick(View view) {
			/*
			 * If the game is over there is no move to play.
			 */
			if (board.isGameOver() == true) {
				return;
			}

			/*
			 * If some other player have to play there is no move to generate.
			 */
			if (mode == PlayingMode.SINGLE_PLAYER && board.getTurn() % 2 != 0) {
				return;
			}

			/*
			 * Find which piece was clicked.
			 */
			boolean result = false;
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[i].length; j++) {
					if (pieces[i][j] != view) {
						continue;
					}

					result = board.click(i, j);
					if (result == true) {
						sounds.play(clickId, 0.99f, 0.99f, 0, 0, 1);
					} else {
						sounds.play(wrongId, 0.99f, 0.99f, 0, 0, 1);
					}
				}
			}

			updateViews();

			/*
			 * End of the game conditions check.
			 */
			if (board.checkForGameOver() == true) {
				board.setGameOver(true);

				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
			} else if (result == true && board.isTurnOver() == true) {
				board.next();

				if(mode == PlayingMode.SINGLE_PLAYER) {
					handler.postDelayed(ai, 500);
				}
			}
			updateViews();
		}
	};

	/**
	 * Update all visual components.
	 */
	private void updateViews() {
		/*
		 * Show player turn.
		 */
		playerOnTurn.setText("" + getResources().getString(R.string.player_lable) + (board.getTurn() % Board.NUMBER_OF_PLAYERS + 1));

		Piece values[][] = board.getPieces();
		boolean selected[][] = board.getSelection();

		/*
		 * Select images to draw. Alpha is used to mark the selected piece.
		 */
		for (int i = 0; i < pieces.length && i < selected.length
				&& i < values.length; i++) {
			for (int j = 0; j < pieces[i].length && j < selected[i].length
					&& j < values[i].length; j++) {
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

		/*
		 * Visualize game over conditions.
		 */
		if (board.isGameOver() == true) {
			/*
			 * Distinct win combination pieces.
			 */
			boolean winners[][] = board.winners();
			for (int i = 0; i < pieces.length && i < winners.length; i++) {
				for (int j = 0; j < pieces[i].length && j < winners[i].length; j++) {
					if (winners[i][j] == true) {
						continue;
					}

					pieces[i][j].setAlpha(0.3F);
				}
			}

			/*
			 * Display game over message.
			 */
			Toast.makeText(this,
					getResources().getString(R.string.game_over_message),
					Toast.LENGTH_LONG).show();

			/*
			 * Play game over sound.
			 */
			sounds.play(finishId, 0.99f, 0.99f, 0, 0, 1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		/*
		 * Obtain activity parameters.
		 */
		mode = (PlayingMode) getIntent().getSerializableExtra("mode");

		/*
		 * Load sounds.
		 */
		sounds = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		clickId = sounds.load(this, R.raw.schademans_pipe9, 1);
		finishId = sounds.load(this, R.raw.game_sound_correct, 1);
		wrongId = sounds.load(this, R.raw.cancel, 1);

		/*
		 * Obtain reference.
		 */
		playerOnTurn = (TextView) findViewById(R.id.player_on_turn);

		/*
		 * Load images.
		 */
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

		/*
		 * Attach of the piece click listener object.
		 */
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				pieces[i][j].setOnClickListener(onPiceClick);
			}
		}

		board.reset();
		updateViews();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_option_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_game:
			board.reset();
			sounds.play(finishId, 0.99f, 0.99f, 0, 0, 1);
			updateViews();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		sounds.release();
		sounds = null;
	}
}
