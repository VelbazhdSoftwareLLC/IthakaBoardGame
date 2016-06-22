package eu.veldsoft.ithaka.board.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	 * It is used in client bluetooth code.
	 */
	private Set<BluetoothDevice> devices = null;

	/**
	 * It is used in client bluetooth code.
	 */
	private BluetoothDevice device = null;

	/**
	 * It is used in client bluetooth code.
	 */
	private List<CharSequence> items = null;

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
			if (board.getTurn() % board.NUMBER_OF_PLAYERS != 1) {
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
	private View.OnClickListener onPeiceClick = new View.OnClickListener() {
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
			if (mode == PlayingMode.SINGLE_PLAYER && board.getTurn() % board.NUMBER_OF_PLAYERS != 0) {
				return;
			}

			/*
			 * Match client in multiplayer mode.
			 */
			if (mode == PlayingMode.CLIENT_MULTIPLAYER && board.getTurn() % board.NUMBER_OF_PLAYERS != 0) {
				return;
			}

			/*
			 * Match server in multiplayer mode.
			 */
			if (mode == PlayingMode.SERVER_MULTIPLAYER && board.getTurn() % board.NUMBER_OF_PLAYERS != 1) {
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
	 * On create helper function for client bluetooth functions.
	 */
	private void onClientCreate() {
	  /*
		* Select one of more devices.
		*/
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			Toast.makeText(GameActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();
			return;
		}

	  /*
		* If there is no server device multiplayer game is not possible.
		*/
		devices = adapter.getBondedDevices();
		if (devices.size() <= 0) {
			Toast.makeText(GameActivity.this, R.string.server_is_not_available, Toast.LENGTH_SHORT).show();
			return;
		}

		items = new ArrayList<CharSequence>();
		for (BluetoothDevice d : devices) {
			items.add(d.getName() + " : " + d.getAddress());
		}

	  /*
		* Select devices from a dialg window.
		*/
		AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
		builder.setTitle(R.string.choose_server_title);
		builder.setSingleChoiceItems(items.toArray(new CharSequence[items.size()]), -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int index) {
				dialog.dismiss();
				int i = 0;
				for (BluetoothDevice d : devices) {
					if (i == index) {
						device = d;
						break;
					}
					i++;
				}
				//TODO Move the thread here.
			}
		});

		builder.create().show();

	  /*
		* Just in case.
		*/
		adapter.cancelDiscovery();

	  /*
		* Client thread.
		*/
		new Thread(new Runnable() {
			private final static long CLIENT_SLEEP_INTERVAL = 100;

			@Override
			public void run() {
			  /*
				* Waiting for server device to be selected.
				*/
				while (device == null) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
				}

				try {
					BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(Util.BLUETOOTH_UUID);
					//TODO socket.isConnected()
					socket.connect();

				  /*
					* Connection refused by the remote server.
					*/
					if (socket == null) {
						Toast.makeText(GameActivity.this, R.string.server_is_not_available, Toast.LENGTH_SHORT).show();
						return;
					}

					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					//TODO Do the real communication
					boolean done = false;
					while (done == false) {
						/*
						 * Wait for the human player to play.
						 */
						if (board.isTurnOver() == false) {
							Thread.sleep(CLIENT_SLEEP_INTERVAL);
							continue;
						}

						out.println(board.lastMoveCoordiantes());
						out.flush();

						String tokens[] = {};
						do {
							Thread.sleep(CLIENT_SLEEP_INTERVAL);
							final String line = in.readLine();
							tokens = line.split("\\s+");
						} while(tokens.length != 4);
						//TODO If there is no 4 integer numbers used as coordinates do something smarter.

						board.click(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
						GameActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateViews();
							}});
						board.click(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
						GameActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateViews();
							}});
						board.turnFinish();
						board.next();
						GameActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateViews();
							}});

						/*
						 * When the game is over stop the communication.
						 */
						if(board.isGameOver() == true) {
							done = true;
						}
					}
					GameActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							updateViews();
						}});

					in.close();
					out.close();
					socket.close();
				} catch (final IOException e) {
					GameActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(GameActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();}});
				} catch (final InterruptedException e) {
					GameActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(GameActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();}});
				}
			}
		}).start();
	}

	/**
	 * On create helper function for server bluetooth functions.
	 */
	private void onServerCreate() {
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			Toast.makeText(GameActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();
			return;
		}

	  /*
		* Just in case.
		*/
		adapter.cancelDiscovery();

	  /*
		* Server thread.
		*/
		new Thread(new Runnable() {
			private final static long SERVER_SLEEP_INTERVAL = 100;

			@Override
			public void run() {
				BluetoothSocket socket = null;
				try {
					BluetoothServerSocket server = adapter.listenUsingRfcommWithServiceRecord(Util.BLUETOOTH_NAME, Util.BLUETOOTH_UUID);
					socket = server.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

					boolean done = false;
					while (done == false) {
						//TODO Do the real communication.
						final String line = in.readLine();
						String tokens[] = line.split("\\s+");

						//TODO If there is no 4 integer numbers used as coordinates do something smarter.
						if(tokens.length != 4) {
							Thread.sleep(SERVER_SLEEP_INTERVAL);
							continue;
						}

						board.click(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
						GameActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateViews();
							}});
						board.click(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
						GameActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateViews();
							}});
						board.turnFinish();
						board.next();
						GameActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateViews();
							}});

						/*
						 * Wait for the human player to play.
						 */
						while(board.isTurnOver() == false) {
							Thread.sleep(SERVER_SLEEP_INTERVAL);
						}

						out.println(board.lastMoveCoordiantes());
						out.flush();

						if(board.isGameOver() == true) {
							done = true;
						}
					}
					GameActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							updateViews();
						}});

					out.close();
					in.close();
					socket.close();
					server.close();
				} catch (IOException e) {
					Toast.makeText(GameActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();
				} catch (InterruptedException e) {
					Toast.makeText(GameActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();
				}
			}
		}).start();
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
				pieces[i][j].setOnClickListener(onPeiceClick);
			}
		}

		board.reset();

		switch(mode) {
			case CLIENT_MULTIPLAYER:
				onClientCreate();
				break;
			case SERVER_MULTIPLAYER:
				onServerCreate();
				break;
		}

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
