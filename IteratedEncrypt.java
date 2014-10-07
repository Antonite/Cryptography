import edu.rit.util.Hex;

public class IteratedEncrypt {

	public static void main(String[] args) {
		
		int n = 5;
		String sKeyU = "";
		String sKeyL = "";
		String plainText = "";
		
		if(args.length == 4){
			try{
				sKeyU = args[0];
				sKeyL += args[1];
				plainText = args[2];
				n = Integer.parseInt(args[3]);
			}catch(Exception e){
				System.err.println("Invalid Arguments");
				System.exit(0);
			}
		}else{
			 System.err.println("Invalid Arguments");
			 System.exit(0);
		}
		
		long ciphertext = Hex.toLong(plainText);

		long uKey = Hex.toLong(sKeyU);
		long lKey = Hex.toLong(sKeyL);
		
		long[] key = {uKey, lKey};
		
		for (int i=0;i<n;i++){
			ciphertext = Encrypt.blockEncrypt(ciphertext,key);
		}
		
		int hexLen = Long.toHexString(ciphertext).length();
		String output = "";
		
		if(hexLen<16){
			for (int j=0;j<16-hexLen;j++){
				output+="0";
			}
		}

		output+=Long.toHexString(ciphertext);
		
		System.out.println(output);

	}

}
