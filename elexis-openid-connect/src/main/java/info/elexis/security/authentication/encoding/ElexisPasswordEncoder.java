package info.elexis.security.authentication.encoding;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;

public class ElexisPasswordEncoder extends BasePasswordEncoder {

	private final Logger logger = LoggerFactory.getLogger(ElexisPasswordEncoder.class);

	private PasswordEncryptionService pes = new PasswordEncryptionService();

	@Override
	public String encodePassword(String rawPass, Object salt) {
		String[] split = rawPass.split("\\|");
		if (split.length == 2) {
			rawPass = split[0];
			salt = split[1];
		}

		try {
			if (salt == null) {
				salt = pes.generateSaltAsHexString();
			}
			byte[] data = String.valueOf(salt).getBytes();
			byte[] encryptedPassword = pes.getEncryptedPassword(rawPass, data);
			return new String(encryptedPassword);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			logger.error("encodePassword", e);
		}
		return null;
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		String[] split = encPass.split("\\|");
		if (split.length == 2) {
			encPass = split[0];
			salt = split[1];
		}
		try {
			return pes.authenticate(rawPass, encPass, (String) salt);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | DecoderException e) {
			logger.error("isPasswordValid", e);
		}
		return false;
	}

}
