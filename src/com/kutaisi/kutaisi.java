package com.kutaisi;

import com.kutaisi.R.drawable;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class kutaisi extends Activity {
	public static String path = Environment.getExternalStorageDirectory() + "/";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText et1 = (EditText)findViewById(R.id.EditText01);
        Button b1 = (Button)findViewById(R.id.Button01);
        Button b2 = (Button)findViewById(R.id.Button02);
        Button b3 = (Button)findViewById(R.id.Button03);
        Button b4 = (Button)findViewById(R.id.Button04);
        
        //generate key pairs
        b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					String status = RSA.GenerateKeyPair(path + "public.key", path + "private.key");
					if (status.equals("success"))
						alert("Key Pair has been generated", "Public - Private Key have been sucessfully stored on your SD card");
				}catch(Exception e){
					alert("Problem generating key pair " , e.getMessage());
				}
			}
		});
        
        //Encrypt File
        b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String fileName = path + et1.getText().toString();
				if (utils.check_file(fileName))
				{
					try{
							//generate sess. key, encrypt with public key, save to file
							byte[] data = RSA.generateSessionKey();
							byte[] cipherData = RSA.rsaEncrypt(path+"public.key", data);
							RSA.saveSessionKey(cipherData, path + "session.key");
							//encrypt file
							
							String sk = new String(data);
							FileEncryptor.encrypt(fileName, sk);
					alert("File Successfully Encrypted", 
						"File has been successfully encrypted, and stored at "
							+ path + fileName + 
						"\n\n We have also generated temporary secret key, you need to send it with file" +
						"\n\n Session Key is stored at: " + path + "session.key"
					);
					
					}catch(Exception e){
						alert("Problem Encrypting", "Could not encrypt file. "+e.getMessage());
					}
				}
				else
					alert("File Does Not Exist", fileName + " - Such file does not exist, please enter correct file name");
			}
		});
        
        //Decrypt File
        b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String fileName = path + et1.getText().toString();
				if (utils.check_file(fileName))
				{
					if (utils.check_file(path + "session.key"))
					{
						try{
							byte[] data = RSA.readSessionKey(path + "session.key");
							data = RSA.rsaDecrypt(path + "private.key", data);
							String sk = new String(data);
							
							FileDecryptor.decrypt(fileName, sk);
							alert("File Successfully Decrypted",
								fileName + " has been successfully decrypted."
									);
							
						}catch(Exception e){
							alert("Problem decrypting", "Problem decrypting. " + e.getMessage());
						}
					}
					else
						alert("Session Key not found","Program could not find temporary secret key. File can not be decrypted");
				}
				else
					alert("File Does Not Exist", fileName + " - Such file does not exist, please enter correct file name");
				
				
			}
		});
        
        //Quit
        b4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				kutaisi.this.finish();
			}
		});
        
        et1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (et1.getText().toString().equals("Enter Filename Here..."))
					et1.setText("");
			}
		});

    }//main
 
	//custom alert dialog box
    public void alert(String title, String message)
    {
    	//set up dialog
		final Dialog dialog = new Dialog(kutaisi.this);
	    dialog.setContentView(R.layout.custom_message);
	    dialog.setTitle(title);
	    dialog.setCancelable(true);
	    
	    //set up text
	    TextView text = (TextView) dialog.findViewById(R.id.TextView01);
	    text.setText(message); 

	    //set up image view
	    ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
	    img.setImageResource(R.drawable.android_focused);

	    //set up button
	    Button button = (Button) dialog.findViewById(R.id.Button01);
	    button.setOnClickListener(new View.OnClickListener() {
	    @Override
	        public void onClick(View v) {
	    		dialog.hide();
	        }
	    });
	    dialog.show();
    }

}