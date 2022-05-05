package org.lufengxue.user.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SqlPrinter implements Log {

    private static final String PREFIX_LOG = "[SQL LOG] ";
    private static final String KEY_SQL = "Preparing:";
    private static final String KEY_PARAM = "Parameters:";
    private static final String KEY_ERR = "### SQL:";
    private static final String KEY_CAUSE = "### Cause:";
    private static final String KEY_ROW = "Row:";
    private static final String KEY_COLUMNS = "Columns:";
    private static final String KEY_TOTAL = "Total:";

    private static final String REGEX = "(?<=(\\)|null)),\\s?";
    private static final String NULL_STR = "null";
    private static final String STR_TYPE = "(String)";

    /**
     * 打印等级:
     * 0 打印: 错误
     * 1 打印: 错误, SQL, 响应行数, SQL耗时
     * 2 打印: 错误, SQL, 响应行数, SQL耗时, 连接创建, 连接关闭, SQL结果, xml文件
     */
    private static int LEVEL = 1;

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    public SqlPrinter(String clazz) {
    }

    @Override
    public boolean isDebugEnabled() {
        return LEVEL > 0;
    }

    @Override
    public boolean isTraceEnabled() {
        return LEVEL > 0;
    }

    @Override
    public void error(String s, Throwable e) {
        log.error(PREFIX_LOG.concat(s), e);
    }

    @Override
    public void error(String s) {
        log.error(PREFIX_LOG.concat(s));
    }

    @Override
    public void debug(String s) {
        String sql = null;
        try {

            // 打印: 错误
            if (s.contains(KEY_ERR) && s.contains(KEY_CAUSE)) {
                sql = popSQL();
                String[] split = s.split("\n");
                for (String str : split) {
                    if (str.contains(KEY_ERR) || str.contains(KEY_CAUSE)) {
                        String logs = str.replaceAll("\\s+", " ");
                        log.error(PREFIX_LOG.concat(logs));
                    }
                }
                return;
            }

            // 打印: SQL 和 参数
            if (LEVEL > 0) {
                // SQL
                if (s.contains(KEY_SQL)) {
                    sql = s.substring(s.indexOf(KEY_SQL) + KEY_SQL.length());
                    pushSQL(sql.trim());
                    pushTime(System.currentTimeMillis());
                    return;
                }

                // 参数
                if (s.contains(KEY_PARAM)) {
                    sql = popSQL();
                    int countParam = count(sql, "?");
                    if (countParam == 0) {
                        log.info(PREFIX_LOG.concat("==>    ").concat(sql));
                        return;
                    }
                    String subParam = s.substring(s.indexOf(KEY_PARAM) + KEY_PARAM.length());
                    String trimParam = subParam.trim();
                    String[] params = convertParamArr(trimParam);
                    if (countParam > params.length) {
                        params = Arrays.copyOf(params, countParam);
                    }
                    String sqlFormat = sql.replace("?", "%s");
                    log.info(PREFIX_LOG.concat("==>    ").concat(String.format(sqlFormat, params)));
                    return;
                }

                // 响应行数
                if (s.contains(KEY_TOTAL)) {
                    String executeTime = String.format(", Execute Time: %d", System.currentTimeMillis() - popTime());
                    log.info(PREFIX_LOG.concat(s).concat(executeTime));
                    return;
                }

                // 打印: 创建连接, 销毁连接, mapper.xml等
                if (LEVEL > 1) {
                    log.info(PREFIX_LOG.concat(s));
                }
            }
        } catch (Exception e) {
            log.error("{} 打印错误: {}\n, SQL: {}\n参数: {}", PREFIX_LOG, e.getMessage(), sql, s, e);
        }
    }


    @Override
    public void trace(String s) {
        // 返回字段, 结果集
        if (LEVEL > 1 && (s.contains(KEY_ROW) || s.contains(KEY_COLUMNS)) ) {
            log.info(PREFIX_LOG.concat(s));
            return;
        }
    }


    @Override
    public void warn(String s) {
        log.info(PREFIX_LOG.concat(s));
    }


    /**
     * 转换参数
     */
    private String[] convertParamArr(String s) {
        String[] split = s.split(REGEX);
        for (int i = 0; i < split.length; i++) {
            String sp = split[i];
            String param;
            if (NULL_STR.equals(sp)) {
                param = NULL_STR;
            } else {
                param = sp.substring(0, sp.lastIndexOf("("));
            }
            if (sp.endsWith(STR_TYPE)) {
                split[i] = "\"".concat(param).concat("\"");
            } else {
                split[i] = param;
            }
        }
        return split;
    }


    /**
     * 存入SQL
     */
    private void pushSQL(String sql) {
        getCache().put("sql", sql);
    }


    /**
     * 取出SQL
     */
    private String popSQL() {
        return (String) getCache().remove("sql");
    }


    /**
     * 存入时间
     */
    private void pushTime(Long time) {
        getCache().put("time", time);
    }


    /**
     * 取出时间
     */
    private Long popTime() {
        return (Long) getCache().remove("time");
    }


    /**
     * 不为空的缓存
     */
    private Map<String, Object> getCache() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }


    /**
     * 统计参数个数
     */
    private static int count(String str, String sub) {
        int index = -1, count = 0;
        while ((index = str.indexOf(sub, index + 1)) != -1) {
            count++;
        }
        return count;
    }

}

