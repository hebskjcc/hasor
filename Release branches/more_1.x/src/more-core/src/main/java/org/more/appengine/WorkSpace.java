/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.more.appengine;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
/**
* 
* @version : 2013-1-7
* @author 赵永春 (zyc@byshell.org)
*/
public class WorkSpace {
    private Settings settings = null;
    /**创建{@link WorkSpace}类型对象。*/
    protected WorkSpace(Settings settings) {
        if (settings == null)
            throw new NullPointerException();
        this.settings = settings;
    }
    /**获取{@link Settings}全局设置对象。*/
    protected Settings getSettings() {
        return this.settings;
    }
    /**获取程序工作目录（绝对路径）。*/
    public String getWorkDir() {
        return this.getSettings().getDirectoryPath("workspace.workDir");
    };
    /**获取数据文件目录（绝对路径）。*/
    public String getDataDir() {
        String workDir = getWorkDir();
        String dataDir = this.getSettings().getDirectoryPath("workspace.dataDir");
        boolean absolute = this.getSettings().getBoolean("workspace.dataDir.absolute");
        if (absolute == false)
            return str2path(new File(workDir, dataDir).getAbsolutePath());
        else
            return str2path(new File(dataDir).getAbsolutePath());
    };
    /**获取临时数据文件目录。*/
    public String getTempDir() {
        String workDir = getWorkDir();
        String tempDir = this.getSettings().getDirectoryPath("workspace.tempDir");
        boolean absolute = this.getSettings().getBoolean("workspace.tempDir.absolute");
        if (absolute == false)
            return str2path(new File(workDir, tempDir).getAbsolutePath());
        else
            return str2path(new File(tempDir).getAbsolutePath());
    };
    /**获取缓存目录。*/
    public String getCacheDir() {
        String workDir = getWorkDir();
        String cacheDir = this.getSettings().getDirectoryPath("workspace.cacheDir");
        boolean absolute = this.getSettings().getBoolean("workspace.cacheDir.absolute");
        if (absolute == false)
            return str2path(new File(workDir, cacheDir).getAbsolutePath());
        else
            return str2path(new File(cacheDir).getAbsolutePath());
    };
    /**获取数据文件目录，自动将name属性添加到返回值中。*/
    public String getDataDir(String name) {
        if (StringUtils.isBlank(name) == true)
            return getDataDir();
        else
            return str2path(new File(getDataDir(), name).getAbsolutePath());
    };
    /**获取临时数据文件目录，自动将name属性添加到返回值中。*/
    public String getTempDir(String name) {
        if (StringUtils.isBlank(name) == true)
            return getTempDir();
        else
            return str2path(new File(getTempDir(), name).getAbsolutePath());
    };
    /**获取缓存目录，自动将name属性添加到返回值中。*/
    public String getCacheDir(String name) {
        if (StringUtils.isBlank(name) == true)
            return getCacheDir();
        else
            return str2path(new File(getCacheDir(), name).getAbsolutePath());
    };
    //-----------------------------------------------------------------
    private File tempFileDirectory = null;
    /**在临时目录下创建一个不重名的临时文件返回，该临时文件会在虚拟机正常退出之后连同其所在目录一同删除。*/
    public File createTempFile() throws IOException {
        if (this.tempFileDirectory == null) {
            this.tempFileDirectory = new File(this.getTempDir("tempFile"));
            this.tempFileDirectory.deleteOnExit();
        }
        long markTime = System.currentTimeMillis();
        String atPath = long2path(markTime, 2000);
        String fileName = "work_" + String.valueOf(markTime) + ".tmp";
        File tmpFile = new File(new File(this.tempFileDirectory, atPath), fileName);
        tmpFile.createNewFile();
        return tmpFile;
    };
    private static String str2path(String oriPath) {
        int length = oriPath.length();
        if (oriPath.charAt(length - 1) == File.separatorChar)
            return oriPath;
        else
            return oriPath + File.separatorChar;
    };
    private static String long2path(long a, int size) {
        StringBuffer buffer = new StringBuffer();
        long b = size;
        long c = a;
        do {
            long m = a % b;
            c = a / b;
            a = c;
            buffer.append(m + File.separator);
        } while (c > 0);
        return buffer.toString();
    };
}