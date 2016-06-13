package eu.veldsoft.ithaka.board.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import eu.veldsoft.ithaka.board.game.model.PlayingMode;
import eu.veldsoft.ithaka.board.game.model.Util;

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

		((Button) findViewById(R.id.single_game)).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", PlayingMode.SINGLE_PLAYER);
						  startActivity(intent);
					  }
				  }
		);

		((Button) findViewById(R.id.double_game)).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", PlayingMode.TWO_PLAYERS);
						  startActivity(intent);
					  }
				  }
		);

		//TODO https://github.com/JimSeker/bluetooth/tree/master/blueToothDemo/app/src/main/java/edu/cs4730/btDemo
		((Button) findViewById(R.id.join_game)).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", PlayingMode.CLIENT_MULTIPLAYER);
						  startActivity(intent);
					   }
				  }
		);

		((Button) findViewById(R.id.create_game)).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", PlayingMode.SERVER_MULTIPLAYER);
						  startActivity(intent);
					  }
				  }
		);

		((Button) findViewById(R.id.exit_game)).setOnClickListener(
				  new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  MenuActivity.this.finish();
					  }
				  }
		);
	}
}
