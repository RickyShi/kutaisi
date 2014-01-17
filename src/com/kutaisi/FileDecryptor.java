package com.kutaisi;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class FileDecryptor
{
   private static String filename;
   private static String password;
   private static FileInputStream inFile;
   private static FileOutputStream outFile;

	 public static String removeCharAt(String s, int pos) {
		   return s.substring(0,pos)+s.substring(pos+1);
		}
   
   public static void decrypt(String fileName, String passwd) throws Exception
   {
	   filename = fileName;
	   password = passwd;

      inFile = new FileInputStream(filename);
      //
      String ext = filename.substring(filename.length()-4, filename.length());
	  if (ext.equals(".enc"))
	  {
		  for (int i=0;i<4;i++)
			  filename = removeCharAt(filename, filename.length()-1);
	  }
	  else
		  filename += "dec";
	  //
	  outFile = new FileOutputStream(filename);

      PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
      SecretKeyFactory keyFactory =
          SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey passwordKey = keyFactory.generateSecret(keySpec);

      // Read in the previouly stored salt and set the iteration count.
      byte[] salt = new byte[8];
      inFile.read(salt);
      int iterations = 100;

      PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

      // Create the cipher and initialize it for decryption.
      Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
      cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);


      byte[] input = new byte[64];
      int bytesRead;
      while ((bytesRead = inFile.read(input)) != -1)
      {
         byte[] output = cipher.update(input, 0, bytesRead);
         if (output != null)
            outFile.write(output);
      }

      byte[] output = cipher.doFinal();
      if (output != null)
         outFile.write(output);

      inFile.close();
      outFile.flush();
      outFile.close();
  }
}

