package aufg1;

import java.math.BigInteger;
import de.medieninf.ads.ADSTool;

/**
 * Programm zum Generieren eines Public Key aus einem Private Key
 * @author Patrik Pezelj, Benedikt Ringlein
 *
 */

public class Aufg1 {

	public static void main(String[] args) {
		
		// Ueberpruefen, ob der erste Parameter angegeben wurde
		if(args.length > 0){
			
			// Privaten Schlüssel einlesen
			if(ADSTool.fileExists(args[0])){
				
				BigInteger[] privateKey = ADSTool.readBigIntegerArray(args[0]);
				
				// Ueberpruefen, ob der Schluessel valide ist
				if(isPrivateKeyValid(privateKey)){
					System.out.println("Der Private Key in '" + args[0] + "' ist gueltig!");
					
					// Ueberpruefen, ob der zweite Parameter angegeben wurde
					if(args.length >= 2){
						
						// Public Key generieren
						BigInteger[] publicKey = getPublicKey(privateKey);
						
						if(!ADSTool.fileExists(args[1])){
							
							// Public Key in entsprechende Datei schreiben
							ADSTool.saveBigIntegerArray(args[1], publicKey);
						}else{
							
							// Ueberpruefen, ob der Inhalt der Datei mit dem Public Key uebereinstimmt
							if(keysMatch(publicKey, ADSTool.readBigIntegerArray(args[1]))){
								System.out.println("Der Public Key in '" + args[1] + "' passt zum Private Key!");
							}else{
								System.out.println("Der Public Key in der angegebenen Datei '" + args[1] + "' passt nicht zum Private Key!");
							}
						}
					}else{
						System.out.println("Bitte Datei angeben, in die der Public Key gespeichert werden soll!");
					}
					
				}else{
					System.out.println("Der Private Key in '" + args[0] + "' ist ungueltig!");
				}
				
			}else{
				System.out.println("Die angegebene Datei fuer den privaten Schluessel konnte nicht gefunden werden.");
			}
			
		}else{
			System.out.println("Syntax: aufg1.jar <PrivaterSchluessel> [<OeffentlicherSchluessel>]");
		}
	}
	
	/**
	 * Ueberprueft, ob ein Private Key valide ist
	 * @param key Der zu ueberpruefende private Key
	 * @return True, wenn der Private Key valide ist, sonst false
	 */
	private static boolean isPrivateKeyValid(BigInteger[] key){
		
		//r muss teilerfremd zu q sein
		if(key[key.length-1].mod(key[key.length-2])==BigInteger.ZERO
				|| key[key.length-2].mod(key[key.length-1])==BigInteger.ZERO){
			return false;
		}
		
		// r muss groesser als die summe der Folge sein
		BigInteger sum = BigInteger.ZERO;
		for (int i = 0; i < key.length - 2; i++) {
			sum = sum.add(key[i]);
		}
		if(key[key.length-2].compareTo(sum) <= 0){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Generiert einen Public Key zu einem gegebenen Private Key
	 * @param key Der Private Key, zu dem ein Public Key generiert werden soll
	 * @return Der entsprechende Public Key
	 */
	private static BigInteger[] getPublicKey(BigInteger[] key){
		
		// q und r werden nicht benoetigt
		BigInteger[] publicKey = new BigInteger[key.length - 2];
		
		// Jede einzelne Komponente umrechnen
		for (int i = 0; i < publicKey.length; i++) {
			publicKey[i] = key[i].multiply(key[key.length-1]).mod(key[key.length-2]);
		}
		return publicKey;
	}
	
	/**
	 * Ermittelt, ob zwei gegebene Keys identisch sind 
	 * @param generatedKey Erster Key (z.B. ein generierter Key)
	 * @param loadedKey Zweiter Key (z.B. ein aus der Datei geladener Key)
	 * @return True, wenn die Keys identisch sind, sont false
	 */
	private static boolean keysMatch(BigInteger[] generatedKey, BigInteger[] loadedKey){
		
		// Die Keys muessen die gleiche Laenge haben
		if(generatedKey.length != loadedKey.length){
			return false;
		}
		
		// Alle Elemente der Keys muessen uebereinstimmen
		for (int i = 0; i < loadedKey.length; i++) {
			if(!generatedKey[i].equals(loadedKey[i])){
				return false;
			}
		}
		
		return true;
	}

}
