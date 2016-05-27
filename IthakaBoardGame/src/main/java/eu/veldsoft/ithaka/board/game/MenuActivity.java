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
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
						  //TODO Implement two players game on a single device.
						  Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						  intent.putExtra("mode", PlayingMode.TWO_PLAYERS);
						  startActivity(intent);
					  }
				  }
		);

		//TODO https://github.com/JimSeker/bluetooth/tree/master/blueToothDemo/app/src/main/java/edu/cs4730/btDemo
		((Button) findViewById(R.id.join_game)).setOnClickListener(
				  new View.OnClickListener() {
					  private Set<BluetoothDevice> devices = null;
					  private BluetoothDevice device = null;
					  private List<CharSequence> items = null;

					  @Override
					  public void onClick(View view) {
						  /*
							* Select one of more devices.
							*/
						  final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
						  if (adapter == null) {
							  Toast.makeText(MenuActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();
							  return;
						  }

						  /*
							* If there is no server device multiplayer game is not possible.
							*/
						  devices = adapter.getBondedDevices();
						  if (devices.size() <= 0) {
							  Toast.makeText(MenuActivity.this, R.string.server_is_not_available, Toast.LENGTH_SHORT).show();
							  return;
						  }

						  items = new ArrayList<CharSequence>();
						  for (BluetoothDevice d : devices) {
							  items.add(d.getName() + " : " + d.getAddress());
						  }

						  /*
							* Select devices from a dialg window.
						   */
						  AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
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
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, "Test point 5 ...", Toast.LENGTH_SHORT).show();}});
								  /*
									* Waiting for server device to be selected.
								   */
								  while (device == null) {
									  try {
										  Thread.sleep(300);
									  } catch (InterruptedException e) {
									  }
								  }
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, "Test point 6 ...", Toast.LENGTH_SHORT).show();}});

								  try {
									  BluetoothSocket socket = device.createRfcommSocketToServiceRecord(Util.BLUETOOTH_UUID);
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, "Test point 7 ...", Toast.LENGTH_SHORT).show();}});
										//TODO socket.isConnected()
									  socket.connect();
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, "Test point 8 ...", Toast.LENGTH_SHORT).show();}});

									  /*
										* Connection refused by the remote server.
									   */
									  if (socket == null) {
										  Toast.makeText(MenuActivity.this, R.string.server_is_not_available, Toast.LENGTH_SHORT).show();
										  return;
									  }
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, "Test point 9 ...", Toast.LENGTH_SHORT).show();}});

									  PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
									  BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, "Test point 10 ...", Toast.LENGTH_SHORT).show();}});

									  boolean done = false;
									  while (done = false) {
										  //TODO Do the real communication.
										  Thread.sleep(CLIENT_SLEEP_INTERVAL);
									  }
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, "Test point 11 ...", Toast.LENGTH_SHORT).show();}});

									  in.close();
									  out.close();
									  socket.close();
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, "Test point 12 ...", Toast.LENGTH_SHORT).show();}});
								  } catch (final IOException e) {
			  							MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();}});
final StringWriter sw;
e.printStackTrace( new PrintWriter(  sw = new StringWriter() ) );
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, ""+e+"\n"+sw.toString(), Toast.LENGTH_LONG).show();}});
								  } catch (final InterruptedException e) {
									  MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, R.string.bluetooth_is_not_available, Toast.LENGTH_SHORT).show();}});
MenuActivity.this.runOnUiThread(new Runnable() {@Override public void run() {Toast.makeText(MenuActivity.this, ""+e, Toast.LENGTH_LONG).show();}});
								  } catch (NoSuchMethodException e) {
									  e.printStackTrace();
								  } catch (IllegalAccessException e) {
									  e.printStackTrace();
								  } catch (InvocationTargetException e) {
									  e.printStackTrace();
								  }
							  }
						  }).start();
Toast.makeText(MenuActivity.this, "Test point 13 ...", Toast.LENGTH_SHORT).show();
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
									  while (done = false) {
										  //TODO Do the real communication.
										  Thread.sleep(SERVER_SLEEP_INTERVAL);
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
