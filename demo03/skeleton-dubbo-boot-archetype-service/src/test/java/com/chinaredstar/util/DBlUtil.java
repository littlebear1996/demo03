package com.chinaredstar.util;

import com.chinaredstar.perseus.utils.PropertiesUtil;
import com.ryantenney.metrics.spring.IPUtils;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

/**
 * @author:杨果
 * @date:16/8/22 下午8:15
 *
 * Description:
 *
 */
public class DBlUtil {
    private static Logger logger = LoggerFactory.getLogger(DBlUtil.class);
    private static StringBuilder connUrl = new StringBuilder();
    private static StringBuilder createSql = new StringBuilder();
    private static StringBuilder dropSql = new StringBuilder();
    private static StringBuilder structuralPath = new StringBuilder();
    private static StringBuilder dataPath = new StringBuilder();
    private static StringBuilder dbConfigPath = new StringBuilder();
    private static String ip = IPUtils.getLocalAddress().getHostAddress().replaceAll("\\.", "_");

    static {
        Map<String, String> map = PropertiesUtil.getProperty("db.properties");
        connUrl.append(map.get("master.connection.prefix"))
                .append(map.get("master.connection.postfix"))
                .append("&user=")
                .append(map.get("master.username"))
                .append("&password=")
                .append(map.get("master.password"));


        createSql.append("create database ").append(ip);
        dropSql.append("drop database ").append(ip);

        String projectPath = DBlUtil.class.getClassLoader().getResource("").getPath().replace("target/test-classes/", "");
        structuralPath.append(projectPath).append("doc/structure.sql");
        dataPath.append(projectPath).append("doc/data.sql");
        dbConfigPath.append(DBlUtil.class.getClassLoader().getResource("").getPath()).append("db.properties");
        setProperty(dbConfigPath.toString(), "db.name", ip);
    }

    private static void createDatabase() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection
                    (connUrl.toString());

            Statement statement = connection.createStatement();
            statement.executeUpdate(createSql.toString());


            runScript(structuralPath.toString());
        } catch (Exception e) {
            logger.error("数据库结构创建失败:{}", e.toString());
            return;
        }
        logger.info("数据库结构创建成功");
    }


    private static void dropDatabase() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection
                    (connUrl.toString());

            Statement statement = connection.createStatement();
            statement.executeUpdate(dropSql.toString());
        } catch (Exception e) {
            logger.error("数据库删除失败:{}", e.toString());
            return;
        }
        logger.info("数据库删除成功");
    }

    private static void mockData() {
        runScript(dataPath.toString());
        logger.info("数据加载完成");
    }

    private static void runScript(String filePath) {
        try {
            StringBuilder script = new StringBuilder();
            script.append("SET NAMES utf8;\n");
            script.append("SET FOREIGN_KEY_CHECKS = 0;\n");
            script.append("use ").append(ip).append(";\n");

            Connection connection = DriverManager.getConnection(connUrl.toString());
            FileReader fileReader = new FileReader(new File(filePath));
            byte[] bytes = IOUtils.toByteArray(fileReader);
            script.append(new String(bytes));

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.toString().getBytes());
            InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);

            ScriptRunner runner = new ScriptRunner(connection);
            runner.runScript(inputStreamReader);
        } catch (Exception e) {
            logger.error("脚本执行失败:{}", e.toString());
            return;
        }
        logger.info("脚本执行成功");
    }

    public static void setProperty(String filePath, String key, String value) {
        try {
            File file = new File(filePath);
            Properties properties = new Properties();
            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(filePath);
            properties.setProperty(key, value);
            properties.store(outputStream, "Update \'" + key + "\' value");
            outputStream.close();
        } catch (Exception e) {
            logger.info("设置config文件失败:{}", e.toString());
        }
    }

    public static void main(String[] args) throws SQLException {
        dropDatabase();
        createDatabase();
        mockData();
    }
}
