package com.knowledge_farm.util;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@PropertySource("classpath:datasource.properties")
public class DatabaseBackup extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String sqlFileName = "farm_db1_copy_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + ".sql";
        backup(sqlFileName);
    }

	public void backup(String sql) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("datasource.properties");  
        Properties pros = new Properties();  
        try {  
            pros.load(inputStream);  
        } catch (IOException e1) {  
            e1.printStackTrace();  
        } finally {  
            try {  
                inputStream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        // 这里是读取的属性文件，也可以直接使用  
        String username = pros.getProperty("dataSource.username");// 用户名  
        String password = pros.getProperty("dataSource.password");// 密码  
        String mysqlpaths = pros.getProperty("mysqlpath");// mysqldump.exe文件路径  
        String address = pros.getProperty("address");// 数据库地址  
        String databaseName = pros.getProperty("databaseName");// 要备份的数据库名  
//        String tableName = pros.getProperty("backTable");// 要备份的表名（可选）  
        String sqlpath = pros.getProperty("sql");// 备份出来的sql地址,我这放到D盘下了  
  
        File backupath = new File(sqlpath);  
        if (!backupath.exists()) { // 如果有备份过的文件覆盖掉  
            backupath.mkdir();  
        }  
        StringBuffer sb = new StringBuffer();  
  
        sb.append(mysqlpaths);  
        sb.append("mysqldump ");  
        sb.append("--opt ");  
        sb.append("-h ");  
        sb.append(address);  
        sb.append(" ");  
        sb.append("--user=");  
        sb.append(username);  
        sb.append(" ");  
        sb.append("--password=");  
        sb.append(password);  
        sb.append(" ");  
        sb.append("--lock-all-tables=true ");  
        sb.append("--result-file=");  
        sb.append(sqlpath);  
        sb.append(sql);  
        sb.append(" ");  
        //sb.append("--default-character-set=utf8 ");  
        sb.append("--default-character-set=gb2312 ");//注意你的mysql数据库设置的字符集，要与这设置的一致，否则汉字会乱码。
        sb.append(databaseName);  
        sb.append(" ");  
//        sb.append(tableName);  
        //System.out.println(sb);  
        Runtime cmd = Runtime.getRuntime();  
        try {  
            Process p = cmd.exec(sb.toString());  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }

}
