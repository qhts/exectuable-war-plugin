package com.newera.plugin.wrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.IOUtil;

/**
 * 
 * war转jar插件
 */
@Mojo(name = "tomcatwrapper")
public class TomcatWrapperMojo extends AbstractMojo {

	/**
	 * 打包后资源目录
	 */
	@Parameter(defaultValue = "${project.build.directory}/${war.name}.jar")
	private File jar;
	/**
	 * 打包后资源文件
	 */
	@Parameter(defaultValue = "${project.build.directory}/${war.name}.war")
	private File war;
	/**
	 * tomcat一些参数的设置
	 */
	@Parameter(defaultValue = "warName=${war.name}")
	private String tomcatSettings;
	/**
	 * 是否删除原有war
	 */
	@Parameter(defaultValue = "true")
	private boolean deleteWar;

	public void execute() throws MojoExecutionException {
		try {
			// 将打包后的war文件加到jar中
			if (war.exists()) {
				// 打包
				packageJar("tomcat.jar", jar, war);
				// 删除原有war文件
				if (deleteWar) {
					File f = new File(war.getAbsolutePath());
					f.delete();
				}
			} else {
				throw new Exception("文件缺失");
			}
		} catch (Exception e) {
			throw new MojoExecutionException("war转jar时出现错误", e);
		}
	}

	public void packageJar(String source, File dest, File append) throws Exception {
		ZipInputStream s = null;
		ZipOutputStream d = null;
		InputStream warStream = null;
		try {
			InputStream in = TomcatWrapperMojo.class.getClassLoader().getResourceAsStream(source);
			if (in != null) {
			} else {
				throw new Exception("文件为空");
			}
			d = new ZipOutputStream(new FileOutputStream(dest));
			s = new ZipInputStream(in);
			ZipEntry ze = null;
			while ((ze = s.getNextEntry()) != null) {
				getLog().debug("copy: " + ze.getName());
				d.putNextEntry(ze);
				if (!ze.isDirectory()) {
					IOUtil.copy(s, d);
				}
				d.closeEntry();
			}
			// 添加war文件
			ZipEntry e = new ZipEntry(append.getName());
			getLog().info("append: " + append.getName());
			d.putNextEntry(e);
			warStream = new FileInputStream(append);
			IOUtil.copy(warStream, d);
			d.closeEntry();
			// 添加tomcat.properties
			ZipEntry te = new ZipEntry("tomcat.properties");
			getLog().info("append: tomcat.properties");
			d.putNextEntry(te);
			StringBuffer sb = new StringBuffer();
			for (String kv : tomcatSettings.split(",")) {
				sb.append(kv.trim());
				sb.append(System.getProperty("line.separator"));
			}
			d.write(sb.toString().getBytes());
			d.closeEntry();
		} finally {
			try {
				if (s != null) {
					s.close();
				}
				if (d != null) {
					d.close();
				}
				if (warStream != null) {
					warStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}