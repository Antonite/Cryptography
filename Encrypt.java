import java.nio.ByteBuffer;

import edu.rit.util.Hex;

public class Encrypt {

	public static void main(String[] args) {
		String sKey = "";
		String plainText = "";
		
		sKey = args[0];
		plainText = args[1];
		
		String upperKey = sKey.substring(0,16);
		String lowerKey = sKey.substring(16,32);
		
		long ciphertext = Hex.toLong(plainText);

		long uKey = Hex.toLong(upperKey);
		long lKey = Hex.toLong(lowerKey);
		
		long[] key = {uKey, lKey};
		
		long returned = blockEncrypt(ciphertext,key);
		
		String output = "";
		int hexLen = Long.toHexString(returned).length();

		if(hexLen<16){
			for (int j=0;j<16-hexLen;j++){
				output+="0";
			}
		}

		output+=Long.toHexString(returned);
		
		System.out.println(output);

	}
	
	public static long blockEncrypt(long text, long[] key){

		long ciphertext = text;
		
//		do 100 rounds
		for (int i=1;i<101;i++){
			key = keySchedule(key,i);
			ciphertext = roundEncrypt(ciphertext, key[0]);
		}
			
		key = keySchedule(key,101);
		ciphertext = ciphertext^key[0];
		
		return ciphertext;
		
	}
	
	public static long roundEncrypt(long text, long key){
		
		long cipherText = text^key;

		
		//split into bytes
//		byte[] cipherState = ByteBuffer.allocate(8).putLong(cipherText).array();
		byte [] cipherState = new byte[8];   
		for(int i= 0; i < 8; i++){   
			cipherState[7 - i] = (byte)(cipherText >>> (i*8));   
		}  
		
	    //affine function
		for(int i=0;i<8;i++){
			cipherState[i] = (byte) ((cipherState[i]*215 + 98)%256);
		}
		
		//shuffle & mix
		byte[] cipherShuffle = {
				(byte) ((cipherState[0]+cipherState[4])%256),
				cipherState[4],
				(byte) ((cipherState[1]+cipherState[5])%256),
				cipherState[5],
				(byte) ((cipherState[2]+cipherState[6])%256),
				cipherState[6],
				(byte) ((cipherState[3]+cipherState[7])%256),
				cipherState[7]
		};
		
		cipherShuffle[1]=(byte) (cipherShuffle[0]^cipherShuffle[1]);
		cipherShuffle[3]=(byte) (cipherShuffle[2]^cipherShuffle[3]);
		cipherShuffle[5]=(byte) (cipherShuffle[4]^cipherShuffle[5]);
		cipherShuffle[7]=(byte) (cipherShuffle[6]^cipherShuffle[7]);
		

		for (int i = 0; i < cipherShuffle.length; i++)
		{
			cipherText = (cipherText << 8) + (cipherShuffle[i] & 0xff);
		}
		
		return cipherText;
		
	}
	
	public static long[] keySchedule(long[] key, int round){
		
		long[] akey = {key[0],key[1]};

		//bit shift
		long maskedu = (akey[0] & 0x1ffffffL);
		long maskedl = (akey[1] & 0x1ffffffL);
		
		akey[0] >>>= 25;
		akey[1] >>>= 25;
		maskedl <<= 39;
		maskedu <<= 39;
		akey[0] ^= maskedl;
		akey[1] ^= maskedu;
		

		byte [] sbytes = new byte[8];   
		for(int i= 0; i < 8; i++){   
			sbytes[7 - i] = (byte)(akey[0] >>> (i*8));   
		}  
	    
		byte [] lbytes = new byte[8];   
		for(int i= 0; i < 8; i++){   
			lbytes[7 - i] = (byte)(akey[1] >>> (i*8));   
		}  
	    
	    //affine
		for(int i=0;i<8;i++){
			sbytes[i] = (byte) ((byte)(sbytes[i]*215 + 98)%256);
		}
		
		
		//shuffle & mix
		byte[] pBytes = {
				(byte) ((sbytes[0]+sbytes[4])%256),
				sbytes[4],
				(byte) ((sbytes[1]+sbytes[5])%256),
				sbytes[5],
				(byte) ((sbytes[2]+sbytes[6])%256),
				sbytes[6],
				(byte) ((sbytes[3]+sbytes[7])%256),
				sbytes[7]
		};
		
		pBytes[1]=(byte) (pBytes[0]^pBytes[1]);
		pBytes[3]=(byte) (pBytes[2]^pBytes[3]);
		pBytes[5]=(byte) (pBytes[4]^pBytes[5]);
		pBytes[7]=(byte) (pBytes[6]^pBytes[7]);

		
		lbytes[7]+=round;
		
		long ukeyState = 0;
		for (int i = 0; i < pBytes.length; i++)
		{
			ukeyState = (ukeyState << 8) + (pBytes[i] & 0xff);
		}
		long lkeyState = 0;
		for (int i = 0; i < pBytes.length; i++)
		{
			lkeyState = (lkeyState << 8) + (lbytes[i] & 0xff);
		}
		
		akey[0] = ukeyState;
		akey[1] = lkeyState;
		
		return akey;
	}

}
