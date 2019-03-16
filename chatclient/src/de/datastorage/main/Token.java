package de.datastorage.main;

public class Token
{
//	private byte[] token;
//
//	public Token(byte[] token)
//	{
//		this.token = token;
//	}
//	
//	/**
//	 * idk why you would ever use that
//	 * @return
//	 */
//	public long[] getToken()
//	{
//		long[] returnvalue = new long[(token.length - token.length % 8) / 8 + token.length % 8 != 0 ? 1 : 0];
//		if ((token.length % 8) != 0)
//		{
//			returnvalue[0] = 
//					((long) token[0] << 56) 
//					| (token.length % 8 > 1 ? (((long) token[1] & 0xFF) << 48) : 0)
//					| (token.length % 8 > 2 ? (((long) token[2] & 0xFF) << 40) : 0)
//					| (token.length % 8 > 3 ? (((long) token[3] & 0xFF) << 32) : 0)
//					| (token.length % 8 > 4 ? (((long) token[4] & 0xFF) << 24) : 0)
//					| (token.length % 8 > 5 ? (((long) token[5] & 0xFF) << 16) : 0)
//					| (token.length % 8 > 6 ? (((long) token[6] & 0xFF) << 8) : 0)
//					| (token.length % 8 > 7 ? (((long) token[7] & 0xFF) << 0) : 0);
//		}
//		for (int i1 = 1; i1 <= returnvalue.length; i1++)
//		{
//			returnvalue[(token.length % 8) == 0 ? i1 - 1 : i1] = 
//					((long) token[(i1 - 1) * 8 + token.length % 8] << 56)
//					| (((long) token[(i1 - 1) * 8 + token.length % 8 + 1] & 0xFF) << 48)
//					| (((long) token[(i1 - 1) * 8 + token.length % 8 + 2] & 0xFF) << 40)
//					| (((long) token[(i1 - 1) * 8 + token.length % 8 + 3] & 0xFF) << 32)
//					| (((long) token[(i1 - 1) * 8 + token.length % 8 + 4] & 0xFF) << 24)
//					| (((long) token[(i1 - 1) * 8 + token.length % 8 + 5] & 0xFF) << 16)
//					| (((long) token[(i1 - 1) * 8 + token.length % 8 + 6] & 0xFF) << 8)
//					| (((long) token[(i1 - 1) * 8 + token.length % 8 + 7] & 0xFF) << 0);
//		}
//		return returnvalue;
//	}
//	
//	public byte[] getBytes()
//	{
//		return token;
//	}
	
	private String token;
	
	public Token(String token)
	{
		this.token = token;
	}
	
	public String getToken()
	{
		return token;
	}
}
