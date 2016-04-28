package eu.veldsoft.ithaka.board.game;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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
import java.util.UUID;

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
				  startActivity(new Intent(MenuActivity.this, GameActivity.class));
			  }
			}
		);

		((Button) findViewById(R.id.create_game)).setOnClickListener(
			new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  //TODO https://github.com/JimSeker/bluetooth/tree/master/blueToothDemo/app/src/main/java/edu/cs4730/btDemo
			  }
			}
		);

		((Button) findViewById(R.id.create_game)).setOnClickListener(
			new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
				  if (adapter == null) {
					  Toast.makeText(MenuActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();
					  return;
				  }

				  new Thread(new Runnable() {
					  @Override
					  public void run() {
						  BluetoothSocket socket = null;
						  try {
							  BluetoothServerSocket server = adapter.listenUsingRfcommWithServiceRecord(Util.BLUETOOTH_NAME, Util.BLUETOOTH_UUID);
							  socket = server.accept();
							  BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							  PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

							  boolean done = false;
							  while (done = false) {
								  //TODO Do the real communication.
								  Thread.sleep(100);
							  }

							  out.close();
							  in.close();
							  socket.close();
							  server.close();
						  } catch (IOException e) {
							  Toast.makeText(MenuActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();
						  } catch (InterruptedException e) {
							  Toast.makeText(MenuActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();
						  }
					  }
				  }).start();
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
