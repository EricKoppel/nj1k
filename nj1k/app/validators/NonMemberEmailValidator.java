package validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import models.UserEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NonMemberEmailValidator implements ConstraintValidator<NonMemberEmail, String> {

	private static final Logger logger = LoggerFactory.getLogger(NonMemberEmailValidator.class);
	
	@Override
	public void initialize(NonMemberEmail nonMemberEmail) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		int rowCount = UserEntity.find.where().eq("email", value).findRowCount();
		
		logger.debug("Validating e-mail address does {}exist in database", rowCount > 0 ? "" : "not ");
		
		return rowCount == 0;
	}
}
