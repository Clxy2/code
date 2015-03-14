package cn.clxy.codes.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * 暗号化ユーティリティ。<br>
 * 日本のソース(20120117)を流用する。
 * @author ota_keiji_gn
 */
public final class CryptUtil {

	private static final String KEY = "e2a99ec9f3a63d9d";
	private static final String BLOWFISH = "Blowfish";

	/**
	 * ハッシュ化した文字列を返しますs
	 * @param value 変換する文字列
	 * @return 変換後の文字列
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String value) {

		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes());
			byte[] digest = md.digest();
			for (int i = 0; i < digest.length; i++) {
				if ((0xff & digest[i]) < 0x10) {
					sb.append("0" + Integer.toHexString((0xFF & digest[i])));
				} else {
					sb.append(Integer.toHexString(0xFF & digest[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
		}
		return sb.toString();
	}

	/**
	 * 暗号化を行う
	 * @param value 暗号化するデータ
	 * @return 暗号化したデータ
	 */
	public static String encrypt(String value) {

		SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), BLOWFISH);
		Cipher cipher;
		byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance(BLOWFISH);
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			encrypted = cipher.doFinal(value.getBytes());
			return new String(Hex.encodeHex(encrypted));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 復号化を行う。
	 * @param value 復号化するデータ
	 * @return 復号化後のデータ、復号化出来ない場合はnull
	 */
	public static String decrypt(String value) {

		SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), BLOWFISH);
		Cipher cipher;
		byte[] encrypted = null;
		try {
			encrypted = Hex.decodeHex(value.toCharArray());
			cipher = Cipher.getInstance(BLOWFISH);
			cipher.init(Cipher.DECRYPT_MODE, skey);
			return new String(cipher.doFinal(encrypted));
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {

		System.out.println(md5("clxystudio"));
		System.out.println(decrypt(encrypt("clxystudio")));
	}

	private CryptUtil() {
	}
}