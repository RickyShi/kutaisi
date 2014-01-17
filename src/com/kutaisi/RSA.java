package com.kutaisi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Random;

import javax.crypto.Cipher;

public class RSA {

	public static String GenerateKeyPair(String pk, String rk)
	{		
		try{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.genKeyPair();

			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
			  RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
			  RSAPrivateKeySpec.class);

			saveToFile(pk, pub.getModulus(),pub.getPublicExponent());
			saveToFile(rk, priv.getModulus(),priv.getPrivateExponent());
			
			return "success";
		}catch(Exception e){
			return e.getMessage();
		}
	}

	public static void saveToFile(String fileName, BigInteger mod, BigInteger exp)
	{
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
				oos.writeObject(mod);
				oos.writeObject(exp);
				oos.flush();
				oos.close();
		}catch(Exception e){
			e.getMessage();
		}
	}

	public static PublicKey readPublicFromFile(String fileName)
	{
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
				BigInteger m = (BigInteger)ois.readObject();
				BigInteger e = (BigInteger)ois.readObject();
			    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			    KeyFactory fact = KeyFactory.getInstance("RSA");
			    PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		}catch(Exception e){
			e.getMessage();
			return null;
		}
	}
	
	public static PrivateKey readPrivateFromFile(String fileName)
	{
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
				BigInteger m = (BigInteger)ois.readObject();
				BigInteger e = (BigInteger)ois.readObject();
			    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			    KeyFactory fact = KeyFactory.getInstance("RSA");
			    PrivateKey privKey = fact.generatePrivate(keySpec);
			return privKey;
		}catch(Exception e){
			e.getMessage();
			return null;
		}
	}
	
	static byte[] rsaEncrypt(String fileName, byte[] data)
	{
		byte[] cipherData=null;
		//Encrypt
		try{
			PublicKey pubKey = readPublicFromFile(fileName);
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, pubKey);
				cipherData = cipher.doFinal(data);
			return cipherData;
		}catch(Exception e){
			return null;
		}
	}
	static byte[] rsaDecrypt(String fileName, byte[] cipherData)
	{
		byte[] data=null;
		try{
			//Decrypt
			PrivateKey privKey = readPrivateFromFile(fileName);
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, privKey);
				data = cipher.doFinal(cipherData);
			return data;
		}catch(Exception e){
			return null;
		}
	}
	static byte[] generateSessionKey()
    {
    	int key_size = 20; //245 - max
    	Random rand = new Random();
  		byte[] temp = new byte[key_size];
  		
  		for (int i=0; i<temp.length; i++)
  			temp[i] = (byte)(rand.nextInt(74)+48);

  		return temp;
    }
	
	static String saveSessionKey(byte[] data, String fileName)
	{
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
				oos.writeObject(data);
				oos.flush();
				oos.close();
			return "success";
		}catch(Exception e){
			return e.getMessage();
		}
		
	}
	static byte[] readSessionKey(String fileName)
	{
		try{
			byte[] cipherData=null;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
				cipherData = (byte[])ois.readObject();
			ois.close();
			return cipherData;
		}catch(Exception e){
			return null;
		}
	}
	
}
