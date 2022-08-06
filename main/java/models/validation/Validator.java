package models.validation;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author YONGGYO
 *
 */
public interface Validator {
	/**
	 * 유효성 검사
	 * 
	 * @param {HttpServletRequest} request
	 * @param {Map<String, String>} checkFields 체크 항목 
	 * @throws {RuntimeException}
	 */
	default void requiredCheck(HttpServletRequest request, Map<String, String> checkFields) {
		if (checkFields == null || checkFields.size() == 0) {
			return;
		}
		
		for (Map.Entry<String, String> entry : checkFields.entrySet()) {
			String key = entry.getKey();
			String msg = entry.getValue();
			String value = request.getParameter(key);
			if (value == null || value.isBlank()) {
				throw new ValidationException(msg);
			}
		}
	}
}