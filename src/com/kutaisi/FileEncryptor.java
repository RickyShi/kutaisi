package com.kutaisi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class FileEncryptor
{
   private static String filename;
   private static String password;
   private static FileInputStream inFile;
   private static FileOutputStream outFile;

   public static void encrypt(String fileName, String passwd) throws Exception
   {
      filename = fileName;
      password = passwd;

      inFile = new FileInputStream(filename);
      outFile = new FileOutputStream(filename + ".enc");

      PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
      SecretKeyFactory keyFactory =
          SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey passwordKey = keyFactory.generateSecret(keySpec);
      
      byte[] salt = new byte[8];
      Random rnd = new Random();
      rnd.nextBytes(salt);
      int iterations = 100;

      //Create the parameter spec for this salt and interation count
      PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

      // Create the cipher and initialize it for encryption.
      Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
      cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);

      // Need to write the salt to the (encrypted) file.
      // salt is needed when reconstructing the key for decryption.

      outFile.write(salt);

      // Read the file and encrypt its bytes.
      byte[] input = new byte[64];
      int bytesRead;
      while ((bytesRead = inFile.read(input)) != -1)
      {
         byte[] output = cipher.update(input, 0, bytesRead);
         if (output != null) outFile.write(output);
      }

      byte[] output = cipher.doFinal();
      if (output != null) outFile.write(output);

      inFile.close();
      outFile.flush();
      outFile.close();

  }
}

