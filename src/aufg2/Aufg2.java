package aufg2;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;

import de.medieninf.ads.ADSTool;

/**
 * Das Programm kann Dateien ver- und entschluesseln
 * @author Patrik Pezelj, Benedikt Ringlein
 *
 */

public class Aufg2 {
	
	public static void main(String[] args) {
		
//		//TEST
//		BitSet b = new BitSet(8);
//		b.set(1);
//		b.set(2);
//		b.set(7);
//		System.out.println(encryptData(new BitSet[]{b}, new BigInteger[]{
//				BigInteger.valueOf(559),
//				BigInteger.valueOf(898),
//				BigInteger.valueOf(256),
//				BigInteger.valueOf(750),
//				BigInteger.valueOf(245),
//				BigInteger.valueOf(829),
//				BigInteger.valueOf(356),
//				BigInteger.valueOf(333)
//		})[0]);
//		if(b!=null)return;
		
//		// TEST 2
//		BigInteger[] a = {BigInteger.valueOf(428)};
//		BigInteger[] b = {
//				BigInteger.valueOf(2),
//				BigInteger.valueOf(5),
//				BigInteger.valueOf(17),
//				BigInteger.valueOf(42),
//				BigInteger.valueOf(67),
//				BigInteger.valueOf(137),
//				BigInteger.valueOf(289),
//				BigInteger.valueOf(666),
//				BigInteger.valueOf(999),
//				BigInteger.valueOf(779)
//		};
//		System.out.println(decryptData(a, b)[0]);
//		if(a!=null)return;
		
		
		boolean encrypt;
		BigInteger[] key;
		BitSet[] data;
		BigInteger[] result, dataInt;
		
		// Es muessen vier Parameter uebergeben werden
		if(args.length >= 4){
			
			// Ermitteln, ob ver- oder entschluesselt werden soll
			if(args[0].equalsIgnoreCase("encrypt")){
				encrypt = true;
			}else if(args[0].equalsIgnoreCase("decrypt")){
				encrypt = false;
			}else{
				System.out.println("Der erste Parameter muss 'encrypt' oder 'decrpyt' sein!");
				return;
			}
			
			//Key einlesen
			if(ADSTool.fileExists(args[1])){
				key = ADSTool.readBigIntegerArray(args[1]);
			}else{
				System.out.println("Die Datei mit dem " + (encrypt?"Public":"Private") + " Key wurde nicht gefunden!");
				return;
			}
			
			// Daten einlesen, verarbeiten und speichern
			if(ADSTool.fileExists(args[2])){
				if(encrypt){
					data = ADSTool.toBitSetArray(ADSTool.pad(ADSTool.readByteArray(args[2]),key.length), key.length/8);
					result = encryptData(data, key);
					ADSTool.saveBigIntegerArray(args[3], result);
					System.out.println("Datei in " + args[3] + " verschluesselt.");
				}else{
					dataInt = ADSTool.readBigIntegerArray(args[2]);
					data = decryptData(dataInt, key);
					ADSTool.saveByteArray(args[3], ADSTool.depad(ADSTool.toByteArray(data, (key.length - 2)/8)));
				}
			}else{
				System.out.println("Die Datei mit den zu verschluesselnden Daten wurde nicht gefunden!");
				return;
			}
			
		}else{
			System.out.println("Syntax: aufg2.jar {encrypt|decrypt} <{publicKey|privateKey}> <inputFile> <outputFile>");
		}
	}
	
	private static BigInteger[] encryptData(BitSet[] data, BigInteger[] publicKey){
		BigInteger[] result = new BigInteger[data.length];
		Arrays.fill(result, BigInteger.ZERO);
		for (int i = 0; i < data.length; i++) {
			for (int j = data[i].nextSetBit(0); j >= 0; j = data[i].nextSetBit(j+1)) {
				result[i] = result[i].add(publicKey[publicKey.length-1-j]);
			}
		}
			
		return result;
	}
	
	private static BitSet[] decryptData(BigInteger[] data, BigInteger[] privateKey){
		// TODO
		
		BitSet[] result = new BitSet[data.length];
		for (int i = 0; i < data.length; i++) {
			result[i] = new BitSet(privateKey.length);
			BigInteger sum = data[i].multiply(privateKey[privateKey.length - 1].modInverse(privateKey[privateKey.length - 2])).mod(privateKey[privateKey.length - 2]);
			for (int j = privateKey.length - 3; j >= 0; j--) {
				//System.out.print(privateKey[j]+" ");//
				if(privateKey[j].compareTo(sum) <= 0){
					//System.out.println("is in!");//
					sum = sum.subtract(privateKey[j]);
					result[i].set(privateKey.length-3-j);
				}//else System.out.println("is not in!");//
			}
			//System.out.println(data[i]);//
			//System.out.println(result[i].toString());//
		}
		return result;
	}

}
