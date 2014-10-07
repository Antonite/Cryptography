import edu.rit.util.Hex;

public class IteratedEncrypt {

	public static void main(String[] args) {
			
					int n = 0;
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
																																																																				
																																																																						//key lists
																																																																								long[] upper = new long[101];
																																																																										long[] lower = new long[101];
																																																																												
																																																																														long[] akey = {uKey,lKey};
																																																																																for (int i=1;i<102;i++){
																																																																																			keySchedule(akey,i);
																																																																																						upper[i-1] = akey[0];
																																																																																									lower[i-1] = akey[1];
																																																																																											}
																																																																																													
																																																																																															//affine list
																																																																																																	int[] affine = new int[256];
																																																																																																			for(int i=0;i<256;i++){
																																																																																																						affine[i] = (i*215 + 98)%256;
																																																																																																								}
																																																																																																										
																																																																																																												for (int r=0;r<n;r++){
																																																																																																															
																																																																																																															//			do 100 rounds
																																																																																																																		for (int i=1;i<101;i++){
																																																																																																																						ciphertext ^= upper[i-1];
																																																																																																																								        int t0 = affine[(int)(ciphertext >>> 24 & 255)];
																																																																																																																											        int t1 = (affine[(int)(ciphertext >>> 56 & 255)] + t0) & 255;
																																																																																																																														        int t2 = affine[(int)(ciphertext >>> 16 & 255)];
																																																																																																																																	        int t3 = (affine[(int)(ciphertext >>> 48 & 255)] + t2) & 255;
																																																																																																																																				        int t4 = affine[(int)(ciphertext >>> 8 & 255)];
																																																																																																																																							        int t5 = (affine[(int)(ciphertext >>> 40 & 255)] + t4) & 255;
																																																																																																																																										        int t6 = affine[(int)(ciphertext & 255)];
																																																																																																																																													        int t7 = (affine[(int)(ciphertext >>> 32 & 255)] + t6) & 255;
																																																																																																																																																        ciphertext = 0;
																																																																																																																																																			        ciphertext |= t1;
																																																																																																																																																						        ciphertext <<= 8;
																																																																																																																																																									        ciphertext |= t1^t0;
																																																																																																																																																												        ciphertext <<= 8;
																																																																																																																																																															        ciphertext |= t3;
																																																																																																																																																																		        ciphertext <<= 8;
																																																																																																																																																																					        ciphertext |= t3^t2;
																																																																																																																																																																								        ciphertext <<= 8;
																																																																																																																																																																											        ciphertext |= t5;
																																																																																																																																																																														        ciphertext <<= 8;
																																																																																																																																																																																	        ciphertext |= t5^t4;
																																																																																																																																																																																				        ciphertext <<= 8;
																																																																																																																																																																																							        ciphertext |= t7;
																																																																																																																																																																																										        ciphertext <<= 8;
																																																																																																																																																																																													        ciphertext |= t7^t6;
																																																																																																																																																																																																	}
																																																																																																																																																																																																				
																																																																																																																																																																																																							ciphertext ^= upper[100];

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
																																																																																																																																																																																																																																							

																																																																																																																																																																																																																																								public static void keySchedule(long[] key, int round){
																																																																																																																																																																																																																																										
																																																																																																																																																																																																																																										//		long[] akey = {key[0],key[1]};

																																																																																																																																																																																																																																												//bit shift
																																																																																																																																																																																																																																														long maskedu = (key[0] & 0x1ffffffL);
																																																																																																																																																																																																																																																long maskedl = (key[1] & 0x1ffffffL);
																																																																																																																																																																																																																																																		
																																																																																																																																																																																																																																																				key[0] >>>= 25;
																																																																																																																																																																																																																																																						key[1] >>>= 25;
																																																																																																																																																																																																																																																								maskedl <<= 39;
																																																																																																																																																																																																																																																										maskedu <<= 39;
																																																																																																																																																																																																																																																												key[0] ^= maskedl;
																																																																																																																																																																																																																																																														key[1] ^= maskedu;
																																																																																																																																																																																																																																																																

																																																																																																																																																																																																																																																																		
																																																																																																																																																																																																																																																																				//convert to bytes
																																																																																																																																																																																																																																																																						byte [] sbytes = new byte[8];   
																																																																																																																																																																																																																																																																								for(int i= 0; i < 8; i++){   
																																																																																																																																																																																																																																																																											sbytes[7 - i] = (byte)(key[0] >>> (i*8));   
																																																																																																																																																																																																																																																																													}  
																																																																																																																																																																																																																																																																														    
																																																																																																																																																																																																																																																																														    		byte [] lbytes = new byte[8];   
																																																																																																																																																																																																																																																																																		for(int i= 0; i < 8; i++){   
																																																																																																																																																																																																																																																																																					lbytes[7 - i] = (byte)(key[1] >>> (i*8));   
																																																																																																																																																																																																																																																																																							}  
																																																																																																																																																																																																																																																																																								    

																																																																																																																																																																																																																																																																																								    //		System.out.println(Long.toBinaryString(key[0]));
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
																																																																																																																																																																																																																																																																																																																																																																																
																																																																																																																																																																																																																																																																																																																																																																																		key[0] = ukeyState;
																																																																																																																																																																																																																																																																																																																																																																																				key[1] = lkeyState;
																																																																																																																																																																																																																																																																																																																																																																																					}

																																																																																																																																																																																																																																																																																																																																																																																					}

