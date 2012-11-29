package cz.zcu.kiv.eegdatabase.logic.controller.article;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 29.11.12
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class AddArticleValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ArticleCommand.class);
    }

    @Override
    public void validate(Object command, Errors errors) {
        ArticleCommand ac = (ArticleCommand) command;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "required.field");

        if (ac.getTitle().length() > 150) {
            errors.rejectValue("title", "invalid.articleLength");
        }

    }
}
