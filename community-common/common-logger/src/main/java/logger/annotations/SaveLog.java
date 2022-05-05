package logger.annotations;



import logger.enums.ModuleNames;

import java.lang.annotation.*;

/**
 * @author Alvin
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SaveLog {

    ModuleNames moduleName();

    String logName();

    String logInfo();

}
