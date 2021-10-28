package com.example.webpractice.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.UUID;

/**
 * 文件工具类
 *
 * @Author MengYuxin
 * @Date 2021/10/26 11:43
 */

@Slf4j
public class FileUtil {


    /**
     * 随机生成UUID
     *
     * @return
     */
    public static synchronized String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }


    /**
     * 拼写路径
     *
     * @param args 多个参数
     * @return 完整路径
     */
    public static String jointPath(Object... args) {
        return jointPathInArr(args);
    }

    /**
     * 拼写路径成为字符串
     *
     * @param args
     * @return 完整路径
     */
    private static String jointPathInArr(Object[] args) {
        StringBuilder pathBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            pathBuilder.append(args[i]);
            if (i != args.length - 1) {
                pathBuilder.append(File.separator);
            }
        }
        return pathBuilder.toString();
    }


    /**
     * 删除文件的方法
     *
     * @param path 路径
     * @return 删除是否成功
     */
    public static boolean deleteDirRecursion(String path) {
        return deleteDirRecursion(path, "");
    }

    private static boolean deleteDirRecursion(String path, String empty) {
        File file = new File(path);
        //文件是否存在
        if (!file.exists()) {
            log.error("要删除的目标路径不存在");
            return false;
        }
        boolean flag = true;
        //删除文件
        if (file.isFile()) {
            flag = file.delete();
            return flag;
        }
        // 删除文件夹中的所有文件包括子目录
        System.out.println(empty + "--- try to delete dir " + path);
        File[] files = file.listFiles();
        for (File value : files) {
            // 删除子文件
            if (value.isFile()) {
                System.out.println(empty + "   --- try to delete file " + value.getAbsolutePath());
                File subFile = new File(value.getAbsolutePath());
                flag = subFile.delete() && flag;
                if (!flag) System.err.println(empty + "   --- failed to delete file " + path);
                else System.out.println(empty + "   --- succeed to delete file " + path);
            }
            // 删除子目录
            else if (value.isDirectory()) {
                flag = deleteDirRecursion(value.getAbsolutePath(), "   ") && flag;
            }
        }
        if (!flag) {

            System.err.println(empty + "--- failed to delete all sub files of dir " + path);
            return false;
        }
        // 删除当前目录
        if (file.delete()) {
            System.out.println(empty + "--- succeed to delete dir " + path);
            return true;
        } else {
            System.err.println(empty + "--- failed to delete dir " + path);
            return false;
        }
    }
}
