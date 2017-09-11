package com.supermap.egispservice.reverse.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;


/**
  * @ClassName: LuceneUtils
  * @Description: 
  *   调用Lucene的相关工具
  * @author huanghuasong
  * @date 2016-2-17 下午3:11:35
  *
 */
@Component
public class LuceneUtils {

	
	private static final Logger LOGGER = Logger.getLogger(LuceneUtils.class);
	
	private  IndexSearcher searcher;
	private Directory directory;
	
	public LuceneUtils(){
		initSearcher();
	}
	
	/**
	  * @Title: initSearcher
	  * @Description: 
	  *     初始化IndexSearcher        
	  * @author huanghuasong
	  * @date 2016-2-17 下午3:23:36
	 */
	private void initSearcher(){
		try {
			LOGGER.info("## start load index[dir=" + Config.INDEX_DIR
					+ ",name=" + Config.INDEX_NAME + "] ...");
			
			Path path = FileSystems.getDefault().getPath(Config.INDEX_DIR+Config.INDEX_NAME);
			directory = FSDirectory.open(path);
			DirectoryReader ireader = DirectoryReader.open(directory);
			LOGGER.info("## index count : "+ireader.numDocs());
			searcher = new IndexSearcher(ireader);
			LOGGER.info("## load index success ...");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	  * @Title: getIndexSearcher
	  * @Description: 
	  *      获取IndexSearcher
	  * @return       
	  * @author huanghuasong
	  * @date 2016-2-17 下午3:30:17
	 */
	public IndexSearcher getIndexSearcher(){
		return this.searcher;
	}
	
	
}
