package com.supermap.egispservice.area.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;




/**
 * 打包目录及其内部目录和文件为zip
 * 
 * @author wangmeng
 * 
 */
public class ZipExtend {
	private static Logger LOGGER = Logger.getLogger(ZipExtend.class);

	private OutputStream out = null;
	private BufferedOutputStream bos = null;
	private ZipArchiveOutputStream zaos = null;
	private String zipFileName = null;

	public ZipExtend(String zipPath) {
		this.zipFileName = zipPath;
	}

	public void createZipOut() throws FileNotFoundException, IOException {
		File f = new File(zipFileName);
		out = new FileOutputStream(f);
		bos = new BufferedOutputStream(out);
		zaos = new ZipArchiveOutputStream(bos);
		zaos.setEncoding("GBK");

	}

	public void closeZipOut() throws Exception {
		zaos.flush();
		zaos.close();

		bos.flush();
		bos.close();

		out.flush();
		out.close();
	}

	/**
	 * 把一个目录打包到zip文件中的某目录
	 * 
	 * @param dirpath
	 *            目录绝对地址
	 * @param pathName
	 *            zip中目录
	 */
	public void packToolFiles(String dirpath, String zipInnerPath) throws FileNotFoundException, IOException {
		if (StringUtils.isNotEmpty(zipInnerPath)) {
			zipInnerPath = zipInnerPath + File.separator;
		}
		packToolFiles(zaos, dirpath, zipInnerPath);
	}

	/**
	 * 把一个目录打包到一个指定的zip文件中
	 * 
	 * @param dirpath
	 *            目录绝对地址
	 * @param zipInnerPath
	 *            zip文件抽象地址
	 */
	private void packToolFiles(ZipArchiveOutputStream zaos, String dirpath, String zipInnerPath) throws FileNotFoundException, IOException {

		ByteArrayOutputStream tempbaos = new ByteArrayOutputStream();
		BufferedOutputStream tempbos = new BufferedOutputStream(tempbaos);

		File dir = new File(dirpath);
		// 返回此绝对路径下的文件
		File[] files = dir.listFiles();
		if (files == null || files.length < 1) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			// 判断此文件是否是一个文件夹
			if (files[i].isDirectory()) {
				packToolFiles(zaos, files[i].getAbsolutePath(), zipInnerPath + files[i].getName() + File.separator);
			} else {
				zaos.putArchiveEntry(new ZipArchiveEntry(zipInnerPath + files[i].getName()));
				IOUtils.copy(new FileInputStream(files[i].getAbsolutePath()), zaos);
				zaos.closeArchiveEntry();

			}

		}

		tempbaos.flush();
		tempbaos.close();

		tempbos.flush();
		tempbos.close();
	}

	/**
	 * 把一个zip文件解压到一个指定的目录中
	 * 
	 * @param zipPath
	 *            zip文件抽象地址
	 * @param outFolder
	 *            目录绝对地址
	 * @throws ParameterException
	 * @throws IOException
	 */
	public static void unZipToFolder(String zipPath, String outFolder) throws Exception {
		File zipfile = new File(zipPath);
		if (zipfile.exists()) {
			try {
				String zipFileName = zipfile.getName();
				if (!zipFileName.toLowerCase().endsWith(".zip")) {
					throw new Exception("非zip格式文件");
				}

				outFolder = outFolder + File.separator;
				FileUtils.forceMkdir(new File(outFolder));

				ZipFile zf = new ZipFile(zipfile, "GBK");
				Enumeration<ZipArchiveEntry> zipArchiveEntrys = zf.getEntries();
				while (zipArchiveEntrys.hasMoreElements()) {
					ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) zipArchiveEntrys.nextElement();
					if (zipArchiveEntry.isDirectory()) {
						FileUtils.forceMkdir(new File(outFolder + zipArchiveEntry.getName() + File.separator));
					} else {
						InputStream in = zf.getInputStream(zipArchiveEntry);
						OutputStream out = FileUtils.openOutputStream(new File(outFolder + zipArchiveEntry.getName()));
						IOUtils.copy(in, out);

						// 释放资源，否则会引起windows上文件独占
						IOUtils.closeQuietly(in);
						IOUtils.closeQuietly(out);
					}
				}
			} catch (ZipException e) {
				LOGGER.error("", e);
				throw e;
			} catch (IOException e) {
				LOGGER.error("", e);
				throw e;
			}
		} else {
			String info = "指定的解压文件不存在：" + zipPath;
			LOGGER.info(info);
			throw new Exception(info);
		}
	}

	public static void main(String[] args) throws Exception {
		String srcPath = "d:/aaa";
		String zipPath = "d:/trackEncounter.zip";

		ZipExtend zip = new ZipExtend(zipPath);
		zip.createZipOut();
		zip.packToolFiles(srcPath, "");
		zip.closeZipOut();

		zipPath = "d:/test/导入桌面数据.zip";
		String outFolder = "d:/test";
		ZipExtend.unZipToFolder(zipPath, outFolder);
	}
	// public static void main(String[] args) throws FileNotFoundException,
	// Exception {
	// Main main = new Main("c:/test1.zip");
	// main.createZipOut();
	// main.packToolFiles("C:/test", "");
	// main.closeZipOut();
	//
	// main = new Main("c:/test2.zip");
	// main.createZipOut();
	// main.packToolFiles("C:/test", "test");
	// main.closeZipOut();
	//
	// Main.unZipToFolder("c:/test1.zip", "c:/test1");
	//
	// Main.unZipToFolder("c:/test2.zip", "c:/test2");
	// }
}
