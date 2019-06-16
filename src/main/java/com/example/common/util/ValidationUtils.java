package com.example.common.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * 校验工具
 * @author zhangyy326
 */
public class ValidationUtils {

    /**
     * 使用hibernate的注解来进行验证
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validate(Object obj) {
        Set<ConstraintViolation<Object>> validate = validator.validate(obj);
        if (validate.size() > 0) {
            throw new ValidationException(String.valueOf(validate.iterator().next().getMessage()));
        }
    }
    /*
    *   @AssertTrue	用于boolean字段，该字段只能为true
        @AssertFalse	该字段的值只能为false
        @CreditCardNumber	对信用卡号进行一个大致的验证
        @DecimalMax	只能小于或等于该值
        @DecimalMin	只能大于或等于该值
        @Digits(integer=,fraction=)	检查是否是一种数字的整数、分数,小数位数的数字
        @Email	检查是否是一个有效的email地址
        @Future	检查该字段的日期是否是属于将来的日期
        @Length(min=,max=)	检查所属的字段的长度是否在min和max之间,只能用于字符串
        @Max	该字段的值只能小于或等于该值
        @Min	该字段的值只能大于或等于该值
        @NotNull	不能为null
        @NotBlank	不能为空，检查时会将空格忽略
        @NotEmpty	不能为空，这里的空是指空字符串
        @Null	检查该字段为空
        @Past	检查该字段的日期是在过去
        @Pattern(regex=,flag=)	被注释的元素必须符合指定的正则表达式
        @Range(min=,max=,message=)	被注释的元素必须在合适的范围内
        @Size(min=, max=)	检查该字段的size是否在min和max之间，可以是字符串、数组、集合、Map等
        @URL(protocol=,host,port)	检查是否是一个有效的URL，如果提供了protocol，host等，则该URL还需满足提供的条件
        @Valid	该注解主要用于字段为一个包含其他对象的集合或map或数组的字段，或该字段直接为一个其他对象的引用，这样在检查当前对象的同时也会检查该字段所引用的对象
*/
}
